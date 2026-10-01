package com.example.ui.overlay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AnalysisResult
import com.example.data.model.BubbleSize
import com.example.data.model.Recommendation

@Composable
fun FloatingOverlayRoot(
    isScanning: Boolean,
    isExpanded: Boolean,
    analysisResult: AnalysisResult?,
    bubbleSize: BubbleSize,
    bubbleAlpha: Float,
    onBubbleClick: () -> Unit,
    onBubbleLongClick: () -> Unit,
    onCloseExpanded: () -> Unit,
    onRescanClick: () -> Unit,
    onOpenSettingsClick: () -> Unit,
    onCloseServiceClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .alpha(bubbleAlpha)
            .padding(4.dp),
        contentAlignment = Alignment.TopStart
    ) {
        if (!isExpanded) {
            // Collapsed Circular Bubble
            FloatingBubbleComponent(
                isScanning = isScanning,
                bubbleSize = bubbleSize,
                hasResult = analysisResult != null,
                onClick = onBubbleClick,
                onLongClick = onBubbleLongClick
            )
        } else {
            // Expanded Result Panel
            FloatingResultPanel(
                result = analysisResult,
                isScanning = isScanning,
                onClose = onCloseExpanded,
                onRescan = onRescanClick,
                onOpenApp = onOpenSettingsClick,
                onStopService = onCloseServiceClick
            )
        }
    }
}

@Composable
fun FloatingBubbleComponent(
    isScanning: Boolean,
    bubbleSize: BubbleSize,
    hasResult: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (isScanning) 1.15f else 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val rotateAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotate"
    )

    val sizeDp = bubbleSize.dpSize.dp

    Box(
        modifier = Modifier
            .size(sizeDp)
            .shadow(12.dp, CircleShape)
            .clip(CircleShape)
            .border(
                width = if (isScanning) 3.dp else 2.5.dp,
                brush = Brush.sweepGradient(
                    if (isScanning) listOf(Color(0xFF00F5D4), Color(0xFFFFD166), Color(0xFFFF0055), Color(0xFF00F5D4))
                    else listOf(Color(0xFFFFD166), Color(0xFF00F5D4), Color(0xFFFFD166))
                ),
                shape = CircleShape
            )
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF2C2250), Color(0xFF130E24))
                )
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = { onLongClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (isScanning) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(sizeDp - 8.dp)
                    .rotate(rotateAngle),
                color = Color(0xFF00F5D4),
                strokeWidth = 3.dp
            )
            Text(
                text = "...",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "⭐",
                    fontSize = (bubbleSize.dpSize / 3).sp
                )
                Text(
                    text = "PICK",
                    color = Color(0xFFFFD166),
                    fontWeight = FontWeight.Black,
                    fontSize = (bubbleSize.dpSize / 5).coerceAtLeast(8).sp
                )
            }
        }

        if (hasResult && !isScanning) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(2.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00F5D4))
            )
        }
    }
}

@Composable
fun FloatingResultPanel(
    result: AnalysisResult?,
    isScanning: Boolean,
    onClose: () -> Unit,
    onRescan: () -> Unit,
    onOpenApp: () -> Unit,
    onStopService: () -> Unit
) {
    Card(
        modifier = Modifier
            .widthIn(max = 340.dp)
            .shadow(16.dp, RoundedCornerShape(20.dp))
            .border(1.5.dp, Brush.horizontalGradient(listOf(Color(0xFF00F5D4), Color(0xFFFFD166))), RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF161226)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFD166)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🏆", fontSize = 14.sp)
                    }
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "ÖNERİLEN PICK",
                            color = Color(0xFFFFD166),
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                        val mapTitle = result?.draftState?.map?.name ?: "Brawl Stars Draft"
                        Text(
                            text = mapTitle,
                            color = Color(0xFFB0A8D9),
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row {
                    IconButton(
                        onClick = onRescan,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Yeniden Tara",
                            tint = Color(0xFF00F5D4),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Kapat",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            if (isScanning) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color(0xFF00F5D4))
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Ekran Analiz Ediliyor...",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else if (result == null || result.recommendations.isEmpty()) {
                Surface(
                    color = Color(0xFF241C3D),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "⚠️ Draft Bilgisi Okunamadı",
                            color = Color(0xFFFF6B6B),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Brawl Stars karakter seçim ekranındayken tekrar tarayın.",
                            color = Color(0xFFCCC5E8),
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = onRescan,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00F5D4)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Yeniden Tara", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            } else {
                var selectedTabIndex by remember { mutableIntStateOf(0) }
                val recs = result.recommendations

                // 1. Tercih, 2. Tercih, 3. Tercih Tabs
                TabRow(
                    selectedTabIndex = selectedTabIndex.coerceIn(0, recs.size - 1),
                    containerColor = Color(0xFF221A3B),
                    contentColor = Color(0xFF00F5D4),
                    indicator = { tabPositions ->
                        if (selectedTabIndex < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                color = Color(0xFF00F5D4),
                                height = 3.dp
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    recs.forEachIndexed { index, rec ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    text = "${index + 1}. ${rec.brawler.name}",
                                    fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                            }
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                val currentRec = recs.getOrNull(selectedTabIndex) ?: recs[0]

                // Main Recommendation Card
                RecommendationDetailCard(currentRec)
            }

            Spacer(Modifier.height(10.dp))

            // Footer Quick Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onOpenApp,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color(0xFFB0A8D9))
                    Spacer(Modifier.width(4.dp))
                    Text("Ana Ekran", color = Color(0xFFB0A8D9), fontSize = 11.sp)
                }

                Text(
                    text = "Durdur",
                    color = Color(0xFFFF5E5E),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onStopService() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun RecommendationDetailCard(rec: Recommendation) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF221A3B)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Brawler Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Avatar badge with character color
                    val avatarColor = try {
                        Color(android.graphics.Color.parseColor(rec.brawler.avatarColorHex))
                    } catch (e: Exception) {
                        Color(0xFF70CFFF)
                    }

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(avatarColor)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = rec.brawler.name.take(2).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = rec.brawler.name,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(Modifier.width(6.dp))
                            Surface(
                                color = Color(0xFFFFD166),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "${rec.brawler.tier} Tier",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 9.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }

                        Text(
                            text = "${rec.brawler.brawlerClass.titleTr} • ${rec.brawler.range.titleTr} Menzil",
                            color = Color(0xFFB0A8D9),
                            fontSize = 11.sp
                        )
                    }
                }

                // Match Score Circle
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "%${rec.totalScore}",
                        color = Color(0xFF00F5D4),
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Skor",
                        color = Color(0xFF8E84B8),
                        fontSize = 9.sp
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // "Bu pick neden öneriliyor?" Reasons
            Text(
                text = "💡 Bu pick neden öneriliyor?",
                color = Color(0xFFFFD166),
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
            Spacer(Modifier.height(4.dp))

            rec.reasons.forEach { reason ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text("•", color = Color(0xFF00F5D4), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = reason,
                        color = Color(0xFFEDE9FE),
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Score indicators (Counter, Harita, Sinerji)
            ScoreBarItem("⚔️ Counter Üstünlüğü", rec.counterScore, Color(0xFFFF0055))
            ScoreBarItem("🗺️ Harita Uyumu", rec.mapScore, Color(0xFF00F5D4))
            ScoreBarItem("🤝 Takım Sinerjisi", rec.synergyScore, Color(0xFFFFD166))
        }
    }
}

@Composable
fun ScoreBarItem(label: String, score: Int, color: Color) {
    Column(modifier = Modifier.padding(vertical = 3.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = Color(0xFFB0A8D9), fontSize = 10.sp)
            Text("%$score", color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(2.dp))
        LinearProgressIndicator(
            progress = { (score / 100f).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = color,
            trackColor = Color(0xFF332754)
        )
    }
}
