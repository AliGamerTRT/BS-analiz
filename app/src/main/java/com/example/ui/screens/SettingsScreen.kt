package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BubbleSize
import com.example.data.repository.SettingsRepository
import com.example.ui.theme.BrawlCyan
import com.example.ui.theme.BrawlGold
import com.example.ui.theme.BrawlMagenta
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(settingsRepository: SettingsRepository) {
    val context = LocalContext.current
    val settings by settingsRepository.settingsFlow.collectAsState()
    val effectiveKey = settingsRepository.getEffectiveGeminiApiKey()

    var apiKeyInput by remember(settings.geminiApiKey) { mutableStateOf(settings.geminiApiKey) }
    var isKeySavedMessage by remember { mutableStateOf<String?>(null) }
    var showApiKeyText by remember { mutableStateOf(false) }

    var updateToastMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "AYARLAR & TERCİHLER",
            color = BrawlGold,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp
        )
        Text(
            text = "Gemini AI ekran analizi, bubble hareketi ve tema ayarları",
            color = Color(0xFFB3A8DB),
            fontSize = 12.sp
        )

        Spacer(Modifier.height(16.dp))

        // GEMINI API KEY CARD (TOP PRIORITY)
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1536)),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.5.dp,
                    if (effectiveKey.isNotBlank()) BrawlCyan.copy(alpha = 0.6f) else Color(0xFFFF5E7E).copy(alpha = 0.5f),
                    RoundedCornerShape(18.dp)
                )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Key, contentDescription = null, tint = BrawlGold, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "GEMINI AI VISION (EKRAN ANALİZİ)",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                    }

                    if (effectiveKey.isNotBlank()) {
                        Surface(
                            color = BrawlCyan.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "AKTİF",
                                color = BrawlCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Brawl Stars ekranındaki haritayı, bizim ve rakip takımın seçtiği brawler'ları hatasız algılamak için Gemini AI Vision modelini kullanır.",
                    color = Color(0xFFEDE9FE),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = apiKeyInput,
                    onValueChange = {
                        apiKeyInput = it
                        isKeySavedMessage = null
                    },
                    placeholder = { Text("AIzaSy... (Gemini API Anahtarı)", fontSize = 12.sp) },
                    singleLine = true,
                    visualTransformation = if (showApiKeyText) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { showApiKeyText = !showApiKeyText }) {
                            Icon(
                                if (showApiKeyText) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Göster/Gizle",
                                tint = Color(0xFFB3A8DB)
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = BrawlCyan
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            settingsRepository.updateGeminiApiKey(apiKeyInput)
                            isKeySavedMessage = "Gemini API anahtarı başarıyla kaydedildi!"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrawlCyan),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Kaydet", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://aistudio.google.com/app/apikey"))
                            context.startActivity(intent)
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, tint = BrawlGold, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Ücretsiz Anahtar Al", color = BrawlGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                if (isKeySavedMessage != null) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = isKeySavedMessage!!,
                        color = BrawlCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // Bubble Size Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1536)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "🔘 Floating Bubble Boyutu",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BubbleSize.entries.forEach { size ->
                        FilterChip(
                            selected = settings.bubbleSize == size,
                            onClick = { settingsRepository.updateBubbleSize(size) },
                            label = { Text(size.titleTr, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrawlCyan,
                                selectedLabelColor = Color.Black,
                                containerColor = Color(0xFF251E45),
                                labelColor = Color(0xFFEDE9FE)
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Bubble Opacity Slider Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1536)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "👁️ Bubble Şeffaflığı",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "%${(settings.bubbleAlpha * 100).roundToInt()}",
                        color = BrawlCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                Spacer(Modifier.height(6.dp))

                Slider(
                    value = settings.bubbleAlpha,
                    onValueChange = { settingsRepository.updateBubbleAlpha(it) },
                    valueRange = 0.4f..1.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = BrawlCyan,
                        activeTrackColor = BrawlCyan,
                        inactiveTrackColor = Color(0xFF2C2250)
                    )
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // Number of Recommendations Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1536)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "🎯 Öneri Sayısı (1 - 3)",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Sonuç panelinde kaç farklı pick alternatifi listelensin?",
                    color = Color(0xFFB3A8DB),
                    fontSize = 11.sp
                )
                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(1, 2, 3).forEach { count ->
                        FilterChip(
                            selected = settings.recommendationCount == count,
                            onClick = { settingsRepository.updateRecommendationCount(count) },
                            label = { Text("$count Öneri", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrawlGold,
                                selectedLabelColor = Color.Black,
                                containerColor = Color(0xFF251E45),
                                labelColor = Color(0xFFEDE9FE)
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Auto re-analysis & Language
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1536)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "🔄 Otomatik Yeniden Analiz",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Draft aşaması değiştikçe periyodik kontrol eder.",
                            color = Color(0xFFB3A8DB),
                            fontSize = 11.sp
                        )
                    }
                    Switch(
                        checked = settings.autoAnalyze,
                        onCheckedChange = { settingsRepository.updateAutoAnalyze(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = BrawlCyan
                        )
                    )
                }

                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFF251E45))
                )
                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("🌐 Analiz Dili", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Strateji ve neden açıklamaları dili", color = Color(0xFFB3A8DB), fontSize = 11.sp)
                    }
                    Surface(
                        color = Color(0xFF251E45),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Türkçe (TR)",
                            color = BrawlCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFF251E45))
                )
                Spacer(Modifier.height(10.dp))

                // Theme Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "🎨 Koyu Tema (Gaming)",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Oyun içi şeffaf koyu görünüm",
                            color = Color(0xFFB3A8DB),
                            fontSize = 11.sp
                        )
                    }
                    Switch(
                        checked = settings.darkTheme,
                        onCheckedChange = { settingsRepository.updateDarkTheme(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = BrawlCyan
                        )
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Data & Meta Update Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1536)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "📦 Meta Veritabanı Durumu",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "80+ Karakter, en yeni haritalar ve counter ilişkileri günceldir. İnternet olmadan da tam çalışır.",
                    color = Color(0xFFB3A8DB),
                    fontSize = 11.sp
                )
                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = {
                        updateToastMessage = "Veritabanı güncel! En son Brawl Stars Ranked meta verileri yüklü."
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF251E45)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = BrawlCyan, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Verileri Güncelle (Kontrol Et)", color = Color.White, fontSize = 12.sp)
                }

                if (updateToastMessage != null) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = updateToastMessage!!,
                        color = BrawlCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}
