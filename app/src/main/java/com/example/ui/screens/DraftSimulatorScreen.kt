package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Brawler
import com.example.data.model.BrawlMap
import com.example.data.model.DraftState
import com.example.data.model.GameMode
import com.example.data.model.PickTurn
import com.example.data.model.Recommendation
import com.example.data.repository.BrawlDatabase
import com.example.data.repository.DraftAnalysisEngine
import com.example.ui.overlay.RecommendationDetailCard
import com.example.ui.theme.BrawlCyan
import com.example.ui.theme.BrawlGold
import com.example.ui.theme.BrawlMagenta

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DraftSimulatorScreen() {
    val scrollState = rememberScrollState()

    var selectedMode by remember { mutableStateOf(GameMode.BRAWL_BALL) }
    val availableMaps = remember(selectedMode) {
        BrawlDatabase.maps.filter { it.mode == selectedMode }.ifEmpty {
            listOf(BrawlDatabase.maps.first())
        }
    }
    var selectedMap by remember(selectedMode) { mutableStateOf(availableMaps.first()) }

    var allyPicks by remember { mutableStateOf<List<String>>(listOf("piper")) }
    var enemyPicks by remember { mutableStateOf<List<String>>(listOf("frank", "mortis")) }
    var bans by remember { mutableStateOf<List<String>>(listOf("draco", "clancy")) }

    var recommendations by remember {
        mutableStateOf<List<Recommendation>>(emptyList())
    }

    // Modal dialog state for adding a brawler to a slot
    var activeSlotForPicker by remember { mutableStateOf<String?>(null) } // "ALLY", "ENEMY", "BAN"

    // Initial calculation
    remember(selectedMap, allyPicks, enemyPicks, bans) {
        val state = DraftState(
            map = selectedMap,
            mode = selectedMode,
            allyPicks = allyPicks,
            enemyPicks = enemyPicks,
            allyBans = bans.take(3),
            enemyBans = bans.drop(3),
            pickTurn = PickTurn.COUNTER_PICK
        )
        recommendations = DraftAnalysisEngine.analyzeAndRecommend(state, 3)
        Unit
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Title & Presets Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "DRAFT SİMÜLATÖRÜ",
                    color = BrawlGold,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "Harita, mod ve karakterleri seçip motoru test edin",
                    color = Color(0xFFB3A8DB),
                    fontSize = 12.sp
                )
            }

            Surface(
                color = Color(0xFF2B224E),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.clickable {
                    // Load fun competitive preset
                    selectedMode = GameMode.KNOCKOUT
                    val knockoutMap = BrawlDatabase.maps.firstOrNull { it.mode == GameMode.KNOCKOUT } ?: availableMaps.first()
                    selectedMap = knockoutMap
                    allyPicks = listOf("piper")
                    enemyPicks = listOf("angelo", "nani")
                    bans = listOf("draco", "cordelius")
                }
            ) {
                Text(
                    text = "🎲 Hazır Maç",
                    color = BrawlCyan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        // Game Mode Selector Tabs
        ScrollableTabRow(
            selectedTabIndex = GameMode.entries.indexOf(selectedMode),
            containerColor = Color(0xFF1B1536),
            contentColor = BrawlCyan,
            edgePadding = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        ) {
            GameMode.entries.forEach { mode ->
                Tab(
                    selected = selectedMode == mode,
                    onClick = {
                        selectedMode = mode
                    },
                    text = {
                        Text(
                            text = "${mode.iconEmoji} ${mode.displayNameTr}",
                            fontWeight = if (selectedMode == mode) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // Map Selector Chips
        Text(
            text = "HARİTA SEÇİMİ (${selectedMode.displayNameTr}):",
            color = Color(0xFFEDE9FE),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(6.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            availableMaps.forEach { map ->
                FilterChip(
                    selected = selectedMap == map,
                    onClick = { selectedMap = map },
                    label = { Text(map.name, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BrawlCyan,
                        selectedLabelColor = Color.Black,
                        containerColor = Color(0xFF221A3B),
                        labelColor = Color(0xFFEDE9FE)
                    )
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        // Ally Picks & Enemy Picks Section
        Row(modifier = Modifier.fillMaxWidth()) {
            // Ally team card
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF16253B))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🛡️ BİZİM TAKIM", color = BrawlCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        if (allyPicks.size < 2) {
                            IconButton(
                                onClick = { activeSlotForPicker = "ALLY" },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = BrawlCyan, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                    Spacer(Modifier.height(6.dp))

                    if (allyPicks.isEmpty()) {
                        Text("Seçim yok (İlk Pick)", color = Color(0xFF7A9BB8), fontSize = 10.sp)
                    } else {
                        allyPicks.forEach { id ->
                            val brawler = BrawlDatabase.getBrawlerById(id)
                            DraftBrawlerChip(brawler?.name ?: id, Color(0xFF1E3A5F)) {
                                allyPicks = allyPicks.filter { it != id }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.width(8.dp))

            // Enemy team card
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF381525))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⚔️ RAKİP TAKIM", color = BrawlMagenta, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        if (enemyPicks.size < 3) {
                            IconButton(
                                onClick = { activeSlotForPicker = "ENEMY" },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = BrawlMagenta, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                    Spacer(Modifier.height(6.dp))

                    if (enemyPicks.isEmpty()) {
                        Text("Rakip henüz seçmedi", color = Color(0xFFB87A8E), fontSize = 10.sp)
                    } else {
                        enemyPicks.forEach { id ->
                            val brawler = BrawlDatabase.getBrawlerById(id)
                            DraftBrawlerChip(brawler?.name ?: id, Color(0xFF5E1E3A)) {
                                enemyPicks = enemyPicks.filter { it != id }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        // Bans section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF241C33))
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("🚫 YASAKLANANLAR (BAN):", color = Color(0xFFFFB703), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Spacer(Modifier.height(4.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        bans.forEach { id ->
                            val brawler = BrawlDatabase.getBrawlerById(id)
                            DraftBrawlerChip(brawler?.name ?: id, Color(0xFF4A3414)) {
                                bans = bans.filter { it != id }
                            }
                        }
                    }
                }
                IconButton(
                    onClick = { activeSlotForPicker = "BAN" },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = BrawlGold)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Recommendations Header
        Text(
            text = "⚡ HESAPLANAN EN İYİ 3 PICK ÖNERİSİ",
            color = BrawlGold,
            fontWeight = FontWeight.Black,
            fontSize = 13.sp,
            letterSpacing = 0.5.sp
        )
        Spacer(Modifier.height(8.dp))

        if (recommendations.isNotEmpty()) {
            var selectedTab by remember { mutableIntStateOf(0) }

            ScrollableTabRow(
                selectedTabIndex = selectedTab.coerceIn(0, recommendations.size - 1),
                containerColor = Color(0xFF221A3B),
                contentColor = BrawlCyan,
                edgePadding = 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
            ) {
                recommendations.forEachIndexed { index, rec ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = "${index + 1}. Tercih (${rec.brawler.name})",
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp
                            )
                        }
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            val currentRec = recommendations.getOrNull(selectedTab) ?: recommendations[0]
            RecommendationDetailCard(currentRec)
        }

        Spacer(Modifier.height(24.dp))
    }

    // Modal dialog to search and pick a brawler
    if (activeSlotForPicker != null) {
        BrawlerPickerModal(
            title = when (activeSlotForPicker) {
                "ALLY" -> "Bizim Takıma Karakter Ekle"
                "ENEMY" -> "Rakip Takıma Karakter Ekle"
                else -> "Yasaklı (Ban) Karakter Ekle"
            },
            onDismiss = { activeSlotForPicker = null },
            onSelect = { brawler ->
                when (activeSlotForPicker) {
                    "ALLY" -> if (!allyPicks.contains(brawler.id)) allyPicks = allyPicks + brawler.id
                    "ENEMY" -> if (!enemyPicks.contains(brawler.id)) enemyPicks = enemyPicks + brawler.id
                    "BAN" -> if (!bans.contains(brawler.id)) bans = bans + brawler.id
                }
                activeSlotForPicker = null
            }
        )
    }
}

@Composable
fun DraftBrawlerChip(name: String, backgroundColor: Color, onRemove: () -> Unit) {
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(name, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(4.dp))
            Icon(
                Icons.Default.Close,
                contentDescription = "Kaldır",
                tint = Color(0xFFEDE9FE),
                modifier = Modifier
                    .size(12.dp)
                    .clickable { onRemove() }
            )
        }
    }
}

@Composable
fun BrawlerPickerModal(
    title: String,
    onDismiss: () -> Unit,
    onSelect: (Brawler) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filtered = remember(searchQuery) {
        BrawlDatabase.searchBrawlers(searchQuery)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        },
        text = {
            Column(modifier = Modifier.height(350.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Karakter ara...", fontSize = 12.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = BrawlCyan
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    filtered.forEach { brawler ->
                        Surface(
                            color = Color(0xFF221A3B),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clickable { onSelect(brawler) }
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(BrawlCyan.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(brawler.name.take(1), color = BrawlCyan, fontWeight = FontWeight.Black, fontSize = 11.sp)
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Text(brawler.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                Text(
                                    text = "${brawler.brawlerClass.titleTr} • ${brawler.tier}",
                                    color = Color(0xFFB3A8DB),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Vazgeç", color = Color(0xFFEDE9FE))
            }
        },
        containerColor = Color(0xFF1B1536)
    )
}
