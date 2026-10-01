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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Brawler
import com.example.data.model.BrawlerClass
import com.example.data.repository.BrawlDatabase
import com.example.ui.theme.BrawlCyan
import com.example.ui.theme.BrawlGold
import com.example.ui.theme.BrawlMagenta

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrawlerGuideScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedClass by remember { mutableStateOf<BrawlerClass?>(null) }
    var selectedBrawlerForDetail by remember { mutableStateOf<Brawler?>(null) }

    val filteredList = remember(searchQuery, selectedClass) {
        BrawlDatabase.brawlers.filter { brawler ->
            val matchesQuery = searchQuery.isBlank() ||
                brawler.name.contains(searchQuery, ignoreCase = true) ||
                brawler.shortStrategyTr.contains(searchQuery, ignoreCase = true)

            val matchesClass = selectedClass == null || brawler.brawlerClass == selectedClass
            matchesQuery && matchesClass
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "BRAWLER META REHBERİ",
            color = BrawlGold,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp
        )
        Text(
            text = "Karakterlerin counter listesi, sinerjileri ve en iyi modları",
            color = Color(0xFFB3A8DB),
            fontSize = 12.sp
        )

        Spacer(Modifier.height(12.dp))

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Karakter veya strateji ara...", fontSize = 12.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = BrawlCyan) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = BrawlCyan
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // Class Filter Chips
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FilterChip(
                selected = selectedClass == null,
                onClick = { selectedClass = null },
                label = { Text("Tümü", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = BrawlCyan,
                    selectedLabelColor = Color.Black,
                    containerColor = Color(0xFF221A3B),
                    labelColor = Color(0xFFEDE9FE)
                )
            )

            BrawlerClass.entries.forEach { bClass ->
                FilterChip(
                    selected = selectedClass == bClass,
                    onClick = {
                        selectedClass = if (selectedClass == bClass) null else bClass
                    },
                    label = { Text(bClass.titleTr, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BrawlCyan,
                        selectedLabelColor = Color.Black,
                        containerColor = Color(0xFF221A3B),
                        labelColor = Color(0xFFEDE9FE)
                    )
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // Brawlers List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredList, key = { it.id }) { brawler ->
                BrawlerListItemCard(
                    brawler = brawler,
                    isExpanded = selectedBrawlerForDetail?.id == brawler.id,
                    onClick = {
                        selectedBrawlerForDetail = if (selectedBrawlerForDetail?.id == brawler.id) null else brawler
                    }
                )
            }
        }
    }
}

@Composable
fun BrawlerListItemCard(
    brawler: Brawler,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1536)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val avatarColor = try {
                        Color(android.graphics.Color.parseColor(brawler.avatarColorHex))
                    } catch (e: Exception) {
                        BrawlCyan
                    }

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(avatarColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = brawler.name.take(2).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = brawler.name,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(Modifier.width(6.dp))
                            Surface(
                                color = if (brawler.tier == "S") BrawlGold else Color(0xFF4C3E7E),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "${brawler.tier} Tier",
                                    color = if (brawler.tier == "S") Color.Black else Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = "${brawler.brawlerClass.titleTr} • ${brawler.range.titleTr} Menzil",
                            color = Color(0xFFB3A8DB),
                            fontSize = 11.sp
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "%${brawler.winRate}",
                        color = BrawlCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text("Win Rate", color = Color(0xFF8E84B8), fontSize = 9.sp)
                }
            }

            if (isExpanded) {
                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFF2C2250))
                )
                Spacer(Modifier.height(8.dp))

                Text(
                    text = brawler.shortStrategyTr,
                    color = Color(0xFFEDE9FE),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(Modifier.height(8.dp))

                // Best modes
                Text(
                    text = "🏆 En İyi Modlar: ${brawler.bestModes.joinToString { it.displayNameTr }}",
                    color = BrawlGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(4.dp))

                // Counter against
                Text(
                    text = "⚔️ Kimleri Counterlar: ${brawler.counterAgainst.joinToString { it.replace("_", " ").replaceFirstChar(Char::titlecase) }}",
                    color = BrawlCyan,
                    fontSize = 11.sp
                )

                Spacer(Modifier.height(4.dp))

                // Countered by
                Text(
                    text = "🛡️ Kimlerden Kaçmalı: ${brawler.counteredBy.joinToString { it.replace("_", " ").replaceFirstChar(Char::titlecase) }}",
                    color = BrawlMagenta,
                    fontSize = 11.sp
                )
            }
        }
    }
}
