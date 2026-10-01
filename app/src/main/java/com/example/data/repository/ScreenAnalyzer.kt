package com.example.data.repository

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.example.data.model.AnalysisResult
import com.example.data.model.BrawlMap
import com.example.data.model.DraftState
import com.example.data.model.GameMode
import com.example.data.model.PickTurn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

object ScreenAnalyzer {

    private const val TAG = "ScreenAnalyzer"

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(25, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .writeTimeout(25, TimeUnit.SECONDS)
        .build()

    suspend fun analyzeScreenshot(bitmap: Bitmap, apiKey: String): AnalysisResult = withContext(Dispatchers.IO) {
        val cleanKey = apiKey.trim()

        if (cleanKey.isBlank() || cleanKey == "MY_GEMINI_API_KEY") {
            // No API key configured: do NOT invent fake brawlers!
            val emptyDraft = DraftState(
                map = null,
                mode = GameMode.BRAWL_BALL,
                allyPicks = emptyList(),
                enemyPicks = emptyList(),
                allyBans = emptyList(),
                enemyBans = emptyList()
            )
            return@withContext AnalysisResult(
                draftState = emptyDraft,
                recommendations = emptyList(),
                detectionConfidence = 0.0f,
                detectedSummary = "Gemini API Anahtarı Tanımlı Değil",
                isSuccess = false,
                requiresApiKey = true,
                errorMessage = "Karakterlerin ekran görüntüsünden %100 doğru algılanması için Gemini API anahtarı gereklidir. Lütfen Ayarlar sekmesinden ücretsiz Gemini API anahtarınızı girin veya aşağıdaki 'Manuel Düzenle' seçeneğiyle maçtaki karakterleri hemen seçin."
            )
        }

        try {
            // First try gemini-2.5-flash
            val cloudResult = callGeminiVision(bitmap, cleanKey, "gemini-2.5-flash")
            if (cloudResult != null) {
                return@withContext cloudResult
            }

            // Fallback to gemini-3.5-flash if needed
            val fallbackResult = callGeminiVision(bitmap, cleanKey, "gemini-3.5-flash")
            if (fallbackResult != null) {
                return@withContext fallbackResult
            }

            return@withContext AnalysisResult(
                draftState = DraftState(),
                recommendations = emptyList(),
                detectionConfidence = 0.0f,
                detectedSummary = "Ekran Analiz Edilemedi",
                isSuccess = false,
                errorMessage = "Görsel analizi yanıt vermedi. İnternet bağlantınızı veya Gemini API anahtarınızı kontrol edin."
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error in analyzeScreenshot", e)
            return@withContext AnalysisResult(
                draftState = DraftState(),
                recommendations = emptyList(),
                detectionConfidence = 0.0f,
                detectedSummary = "Hata",
                isSuccess = false,
                errorMessage = "Ekran analizi hatası: ${e.localizedMessage ?: "Bilinmeyen hata"}"
            )
        }
    }

    private suspend fun callGeminiVision(bitmap: Bitmap, apiKey: String, modelName: String): AnalysisResult? = withContext(Dispatchers.IO) {
        try {
            val base64Image = bitmapToBase64(bitmap)
            val prompt = """
                You are an expert Brawl Stars Ranked / Competitive Draft analyzer.
                Analyze this Brawl Stars screen capture with extreme precision.

                BRAWL STARS DRAFT RULES & LAYOUT:
                1. MAP & GAME MODE:
                   Look at the top center banner of the screen.
                   - Detect the exact Map name (in English or Turkish, e.g. "Shooting Star", "Center Stage", "Super Beach", "Safe Zone", "Out in the Open", "Hard Rock Mine", "Flaring Phoenix", "Belle's Rock").
                   - Detect the Game Mode: one of BRAWL_BALL, GEM_GRAB, HEIST, BOUNTY, HOT_ZONE, KNOCKOUT, WIPEOUT.

                2. BRAWLER DETECTION (EXTREMELY STRICT - DO NOT GUESS OR HALLUCINATE):
                   - ALLY TEAM (Blue Side / Left): Look at the 3 player cards on the left column.
                     List ONLY the brawlers that have actually been selected/locked in.
                     If a card is empty, silhouette, or says "Picking..." / "Seçiyor...", DO NOT add any brawler!
                   - ENEMY TEAM (Red Side / Right): Look at the 3 player cards on the right column.
                     List ONLY the brawlers that have actually been selected/locked in.
                     If a card is empty, silhouette, or says "Picking..." / "Seçiyor...", DO NOT add any brawler!
                     CRITICAL: If an enemy has NOT picked Mortis, DO NOT output mortis! If no enemy has picked yet, return an empty array []!
                   - BANS: Look at the 6 ban portrait icons at the bottom.
                     List the banned brawlers. If none are banned, return an empty array [].

                JSON Output Format:
                {
                   "mapName": "Name of the detected map or empty string",
                   "gameMode": "BRAWL_BALL | GEM_GRAB | HEIST | BOUNTY | HOT_ZONE | KNOCKOUT | WIPEOUT",
                   "allyPicks": ["brawler_id"],
                   "enemyPicks": ["brawler_id"],
                   "allyBans": ["brawler_id"],
                   "enemyBans": ["brawler_id"],
                   "pickTurn": "FIRST_PICK | SECOND_PICK | COUNTER_PICK | LAST_PICK",
                   "confidence": 0.95
                }

                Canonical IDs: piper, angelo, belle, brock, mandy, nani, clancy, colette, colt, rico, mortis, kenji, cordelius, melodie, mico, crow, leon, fang, draco, frank, buster, meg, el_primo, larry_lawrie, tick, dynamike, barley, gale, moe, charlie, gene, byron, max, sandy, kit, poco, shelly, bull, surge, edgar, buzz, tara, chester, gray, stu, emz, amber, lou.

                Output ONLY valid JSON without markdown formatting.
            """.trimIndent()

            val requestJson = JSONObject().apply {
                val contents = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val parts = JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                            put(JSONObject().apply {
                                put("inlineData", JSONObject().apply {
                                    put("mimeType", "image/jpeg")
                                    put("data", base64Image)
                                })
                            })
                        }
                        put("parts", parts)
                    }
                    put(contentObj)
                }
                put("contents", contents)

                val genConfig = JSONObject().apply {
                    put("responseMimeType", "application/json")
                    put("temperature", 0.05)
                }
                put("generationConfig", genConfig)
            }

            val requestBody = requestJson.toString().toRequestBody("application/json".toMediaType())
            val url = "https://generativelanguage.googleapis.com/v1beta/models/$modelName:generateContent?key=$apiKey"

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                Log.w(TAG, "Gemini $modelName returned error: ${response.code}")
                return@withContext null
            }

            val responseBody = response.body?.string() ?: return@withContext null
            val rootJson = JSONObject(responseBody)
            val candidates = rootJson.optJSONArray("candidates") ?: return@withContext null
            val candidate = candidates.optJSONObject(0) ?: return@withContext null
            val parts = candidate.optJSONObject("content")?.optJSONArray("parts") ?: return@withContext null
            val jsonText = parts.optJSONObject(0)?.optString("text") ?: return@withContext null

            val parsedJson = JSONObject(jsonText.replace("```json", "").replace("```", "").trim())
            val mapName = parsedJson.optString("mapName", "")
            val modeStr = parsedJson.optString("gameMode", "BRAWL_BALL")
            val allyPicks = sanitizeBrawlerList(jsonArrayToStringList(parsedJson.optJSONArray("allyPicks")))
            val enemyPicks = sanitizeBrawlerList(jsonArrayToStringList(parsedJson.optJSONArray("enemyPicks")))
            val allyBans = sanitizeBrawlerList(jsonArrayToStringList(parsedJson.optJSONArray("allyBans")))
            val enemyBans = sanitizeBrawlerList(jsonArrayToStringList(parsedJson.optJSONArray("enemyBans")))
            val confidence = parsedJson.optDouble("confidence", 0.90).toFloat()

            val mode = GameMode.fromString(modeStr)
            val matchedMap = BrawlDatabase.maps.firstOrNull {
                it.name.contains(mapName, ignoreCase = true) || mapName.contains(it.id, ignoreCase = true)
            } ?: BrawlDatabase.maps.firstOrNull { it.mode == mode }

            val draftState = DraftState(
                map = matchedMap,
                mode = mode,
                allyPicks = allyPicks,
                enemyPicks = enemyPicks,
                allyBans = allyBans,
                enemyBans = enemyBans,
                pickTurn = if (enemyPicks.size > allyPicks.size) PickTurn.COUNTER_PICK else PickTurn.FIRST_PICK
            )

            val recs = DraftAnalysisEngine.analyzeAndRecommend(draftState)
            val enemySummary = if (enemyPicks.isNotEmpty()) "Rakip: ${enemyPicks.joinToString { it.replaceFirstChar(Char::titlecase) }}" else "Rakip henüz seçmedi (İlk Pick)"
            val summary = "Harita: ${matchedMap?.name ?: "Tespit Edildi"} | $enemySummary"

            return@withContext AnalysisResult(
                draftState = draftState,
                recommendations = recs,
                detectionConfidence = confidence,
                detectedSummary = summary,
                isSuccess = true
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing Gemini response", e)
            return@withContext null
        }
    }

    private fun sanitizeBrawlerList(list: List<String>): List<String> {
        return list.mapNotNull { raw ->
            val clean = raw.lowercase().trim().replace(" ", "_").replace("-", "_")
            val brawler = BrawlDatabase.getBrawlerById(clean)
            brawler?.id ?: if (clean.isNotBlank()) clean else null
        }.distinct()
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val maxDim = 1280
        val scale = if (bitmap.width > maxDim || bitmap.height > maxDim) {
            val ratio = bitmap.width.toFloat() / bitmap.height.toFloat()
            if (ratio > 1) {
                Bitmap.createScaledBitmap(bitmap, maxDim, (maxDim / ratio).toInt(), true)
            } else {
                Bitmap.createScaledBitmap(bitmap, (maxDim * ratio).toInt(), maxDim, true)
            }
        } else {
            bitmap
        }

        val outputStream = ByteArrayOutputStream()
        scale.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }

    private fun jsonArrayToStringList(array: JSONArray?): List<String> {
        if (array == null) return emptyList()
        val list = mutableListOf<String>()
        for (i in 0 until array.length()) {
            val item = array.optString(i)
            if (item.isNotBlank()) list.add(item.lowercase().trim().replace(" ", "_"))
        }
        return list
    }
}
