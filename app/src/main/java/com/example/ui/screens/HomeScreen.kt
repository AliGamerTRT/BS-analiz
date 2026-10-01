package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ScreenShare
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrawlCyan
import com.example.ui.theme.BrawlGold
import com.example.ui.theme.BrawlMagenta

@Composable
fun HomeScreen(
    isServiceRunning: Boolean,
    hasOverlayPermission: Boolean,
    hasCapturePermission: Boolean,
    hasNotificationPermission: Boolean,
    onRequestOverlayPermission: () -> Unit,
    onRequestCapturePermission: () -> Unit,
    onRequestNotificationPermission: () -> Unit,
    onStartService: () -> Unit,
    onStopService: () -> Unit,
    onOpenBrawlStars: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hero Card
        HeroStatusCard(
            isServiceRunning = isServiceRunning,
            hasAllPermissions = hasOverlayPermission && hasCapturePermission,
            onStartService = onStartService,
            onStopService = onStopService,
            onOpenBrawlStars = onOpenBrawlStars
        )

        Spacer(Modifier.height(16.dp))

        // Required Permissions Checklist
        Text(
            text = "GEREKLİ İZİNLER & DURUM",
            color = BrawlGold,
            fontWeight = FontWeight.Black,
            fontSize = 13.sp,
            letterSpacing = 1.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // 1. Overlay Permission
        PermissionCard(
            title = "Diğer Uygulamaların Üzerinde Gösterme",
            description = "Brawl Stars oynarken ekranın üzerinde küçük yüzen bubble'ın görünebilmesi için zorunludur.",
            isGranted = hasOverlayPermission,
            icon = Icons.Default.Layers,
            actionLabel = "İzni Aç",
            onClick = onRequestOverlayPermission,
            testTag = "btn_grant_overlay"
        )

        Spacer(Modifier.height(10.dp))

        // 2. Screen Capture Permission
        PermissionCard(
            title = "Ekran Yakalama (MediaProjection)",
            description = "Bubble'a dokunduğunuzda mevcut Brawl Stars draft ekranını bir kerelik tarayıp harita ve karakterleri okur.",
            isGranted = hasCapturePermission,
            icon = Icons.AutoMirrored.Filled.ScreenShare,
            actionLabel = "Ekran İzni Ver",
            onClick = onRequestCapturePermission,
            testTag = "btn_grant_capture"
        )

        Spacer(Modifier.height(10.dp))

        // 3. Notification Permission
        PermissionCard(
            title = "Arka Plan Servis Bildirimi",
            description = "Asistanın oyun oynarken arka planda kapanmaması için servis bildirimini etkinleştirin.",
            isGranted = hasNotificationPermission,
            icon = Icons.Default.Notifications,
            actionLabel = "Bildirim İzni Ver",
            onClick = onRequestNotificationPermission,
            testTag = "btn_grant_notification"
        )

        Spacer(Modifier.height(20.dp))

        // Quick Usage Steps Card
        QuickUsageGuideCard()

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun HeroStatusCard(
    isServiceRunning: Boolean,
    hasAllPermissions: Boolean,
    onStartService: () -> Unit,
    onStopService: () -> Unit,
    onOpenBrawlStars: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.5.dp,
                Brush.linearGradient(
                    if (isServiceRunning) listOf(BrawlCyan, BrawlGold)
                    else listOf(Color(0xFF382F63), Color(0xFF251E45))
                ),
                RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1536))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(
                        if (isServiceRunning) BrawlCyan.copy(alpha = 0.15f)
                        else Color(0xFF2C2250)
                    )
                    .border(
                        2.dp,
                        if (isServiceRunning) BrawlCyan else Color(0xFF4C3E7E),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isServiceRunning) "⚡" else "⭐",
                    fontSize = 32.sp
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = if (isServiceRunning) "YÜZEN ASİSTAN AKTİF" else "BRAWL PICK ASİSTANI",
                color = if (isServiceRunning) BrawlCyan else Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                letterSpacing = 0.5.sp
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = if (isServiceRunning)
                    "Bubble şu anda ekranınızın üzerinde yüzer durumdadır. Brawl Stars'ı açıp karakter seçiminde bubble'a dokunun."
                else
                    "Brawl Stars draft maçlarında haritayı, rakibi ve banları analiz ederek size en iyi 3 pick önerisini sunar.",
                color = Color(0xFFB3A8DB),
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            Spacer(Modifier.height(16.dp))

            if (!isServiceRunning) {
                Button(
                    onClick = onStartService,
                    enabled = hasAllPermissions,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrawlCyan,
                        disabledContainerColor = Color(0xFF332958)
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_start_bubble")
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (hasAllPermissions) "YÜZEN BUBBLE'I BAŞLAT" else "Önce İzinleri Verin",
                        color = if (hasAllPermissions) Color.Black else Color(0xFF8E84B8),
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp
                    )
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = onStopService,
                        colors = ButtonDefaults.buttonColors(containerColor = BrawlMagenta),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("btn_stop_bubble")
                    ) {
                        Icon(Icons.Default.Stop, contentDescription = null, tint = Color.White)
                        Spacer(Modifier.width(6.dp))
                        Text("Durdur", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Spacer(Modifier.width(10.dp))

                    Button(
                        onClick = onOpenBrawlStars,
                        colors = ButtonDefaults.buttonColors(containerColor = BrawlGold),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("btn_open_game")
                    ) {
                        Text("🎮 Brawl Stars", color = Color.Black, fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }
}

@Composable
fun PermissionCard(
    title: String,
    description: String,
    isGranted: Boolean,
    icon: ImageVector,
    actionLabel: String,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isGranted) Color(0xFF1E173C) else Color(0xFF271A37)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isGranted) Color(0xFF00F5D4).copy(alpha = 0.3f) else Color(0xFFFF5E7E).copy(alpha = 0.4f),
                RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        if (isGranted) Color(0xFF00F5D4).copy(alpha = 0.15f)
                        else Color(0xFFFF0055).copy(alpha = 0.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (isGranted) BrawlCyan else BrawlMagenta,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(Modifier.width(6.dp))
                    if (isGranted) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "İzin verildi",
                            tint = BrawlCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(Modifier.height(2.dp))

                Text(
                    text = description,
                    color = Color(0xFFB3A8DB),
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }

            if (!isGranted) {
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(containerColor = BrawlCyan),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag(testTag)
                ) {
                    Text(
                        text = actionLabel,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
fun QuickUsageGuideCard() {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF16122C)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "⚡ NASIL KULLANILIR?",
                color = BrawlGold,
                fontWeight = FontWeight.Black,
                fontSize = 13.sp
            )
            Spacer(Modifier.height(10.dp))

            GuideStepRow("1", "İzinleri tamamlayıp 'Yüzen Bubble'ı Başlat' butonuna basın.")
            GuideStepRow("2", "Brawl Stars Ranked / Güç Ligi maçına girin.")
            GuideStepRow("3", "Karakter seçim (draft) ekranında ekranın üstündeki bubble'a bir kez dokunun.")
            GuideStepRow("4", "Asistan ekranı tarayacak ve en iyi 3 pick önerisini nedenleriyle açacaktır.")
            GuideStepRow("5", "Paneldeki 'X' simgesine dokunarak paneli tekrar küçük bubble'a küçültebilirsiniz.")
        }
    }
}

@Composable
fun GuideStepRow(step: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(Color(0xFF2C2250)),
            contentAlignment = Alignment.Center
        ) {
            Text(step, color = BrawlCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(10.dp))
        Text(
            text = description,
            color = Color(0xFFEDE9FE),
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }
}
