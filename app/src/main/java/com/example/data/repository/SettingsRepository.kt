package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.AppSettings
import com.example.data.model.BubbleSize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("brawl_pick_prefs", Context.MODE_PRIVATE)

    private val _settingsFlow = MutableStateFlow(loadSettings())
    val settingsFlow: StateFlow<AppSettings> = _settingsFlow.asStateFlow()

    private fun loadSettings(): AppSettings {
        val sizeName = prefs.getString("bubble_size", BubbleSize.NORMAL.name) ?: BubbleSize.NORMAL.name
        val bubbleSize = try { BubbleSize.valueOf(sizeName) } catch (e: Exception) { BubbleSize.NORMAL }
        val alpha = prefs.getFloat("bubble_alpha", 0.95f)
        val count = prefs.getInt("recommendation_count", 3)
        val autoAnalyze = prefs.getBoolean("auto_analyze", false)
        val darkTheme = prefs.getBoolean("dark_theme", true)
        val geminiApiKey = prefs.getString("gemini_api_key", "") ?: ""

        return AppSettings(
            bubbleSize = bubbleSize,
            bubbleAlpha = alpha,
            recommendationCount = count,
            autoAnalyze = autoAnalyze,
            darkTheme = darkTheme,
            geminiApiKey = geminiApiKey
        )
    }

    fun getEffectiveGeminiApiKey(): String {
        val userKey = _settingsFlow.value.geminiApiKey.trim()
        if (userKey.isNotBlank()) return userKey
        val buildKey = com.example.BuildConfig.GEMINI_API_KEY.trim()
        if (buildKey.isNotBlank() && buildKey != "MY_GEMINI_API_KEY") return buildKey
        return ""
    }

    fun updateGeminiApiKey(key: String) {
        val clean = key.trim()
        prefs.edit().putString("gemini_api_key", clean).apply()
        _settingsFlow.value = _settingsFlow.value.copy(geminiApiKey = clean)
    }

    fun updateBubbleSize(size: BubbleSize) {
        prefs.edit().putString("bubble_size", size.name).apply()
        _settingsFlow.value = _settingsFlow.value.copy(bubbleSize = size)
    }

    fun updateBubbleAlpha(alpha: Float) {
        val clamped = alpha.coerceIn(0.4f, 1.0f)
        prefs.edit().putFloat("bubble_alpha", clamped).apply()
        _settingsFlow.value = _settingsFlow.value.copy(bubbleAlpha = clamped)
    }

    fun updateRecommendationCount(count: Int) {
        val clamped = count.coerceIn(1, 3)
        prefs.edit().putInt("recommendation_count", clamped).apply()
        _settingsFlow.value = _settingsFlow.value.copy(recommendationCount = clamped)
    }

    fun updateAutoAnalyze(enabled: Boolean) {
        prefs.edit().putBoolean("auto_analyze", enabled).apply()
        _settingsFlow.value = _settingsFlow.value.copy(autoAnalyze = enabled)
    }

    fun updateDarkTheme(enabled: Boolean) {
        prefs.edit().putBoolean("dark_theme", enabled).apply()
        _settingsFlow.value = _settingsFlow.value.copy(darkTheme = enabled)
    }
}
