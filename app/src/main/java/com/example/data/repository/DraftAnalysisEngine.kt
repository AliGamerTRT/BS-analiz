package com.example.data.repository

import com.example.data.model.Brawler
import com.example.data.model.DraftState
import com.example.data.model.GameMode
import com.example.data.model.MapLayout
import com.example.data.model.Recommendation
import kotlin.math.min
import kotlin.math.roundToInt

object DraftAnalysisEngine {

    fun analyzeAndRecommend(draftState: DraftState, maxCount: Int = 3): List<Recommendation> {
        val unavailableIds = (draftState.allyPicks + draftState.enemyPicks + draftState.allyBans + draftState.enemyBans)
            .map { it.lowercase().trim().replace(" ", "_") }
            .toSet()

        val enemyBrawlerObjects = draftState.enemyPicks.mapNotNull { BrawlDatabase.getBrawlerById(it) }
        val allyBrawlerObjects = draftState.allyPicks.mapNotNull { BrawlDatabase.getBrawlerById(it) }

        val candidateList = BrawlDatabase.brawlers.filter { brawler ->
            !unavailableIds.contains(brawler.id.lowercase()) &&
            !unavailableIds.contains(brawler.name.lowercase())
        }

        val evaluated = candidateList.map { brawler ->
            evaluateBrawler(
                brawler = brawler,
                draftState = draftState,
                enemies = enemyBrawlerObjects,
                allies = allyBrawlerObjects
            )
        }

        val sorted = evaluated.sortedByDescending { it.totalScore }
        return sorted.take(min(maxCount, sorted.size)).mapIndexed { index, rec ->
            rec.copy(rank = index + 1)
        }
    }

    private fun evaluateBrawler(
        brawler: Brawler,
        draftState: DraftState,
        enemies: List<Brawler>,
        allies: List<Brawler>
    ): Recommendation {
        var baseScore = 50.0

        // 1. Tier & Win Rate Factor (0 - 20)
        baseScore += when (brawler.tier) {
            "S" -> 16.0
            "A" -> 11.0
            "B" -> 6.0
            else -> 2.0
        }
        baseScore += ((brawler.winRate - 50.0) * 1.5).coerceIn(-5.0, 10.0)

        // 2. Game Mode Fit (0 - 100 sub-score)
        var modeScore = 50.0
        if (brawler.bestModes.contains(draftState.mode)) {
            modeScore += 40.0
        }
        if (draftState.mode.preferredClasses.contains(brawler.brawlerClass)) {
            modeScore += 10.0
        }
        modeScore = modeScore.coerceIn(20.0, 99.0)

        // 3. Map & Layout Fit (0 - 100 sub-score)
        var mapScore = 50.0
        val currentLayout = draftState.map?.layout ?: when (draftState.mode) {
            GameMode.BOUNTY, GameMode.KNOCKOUT -> MapLayout.OPEN
            GameMode.BRAWL_BALL -> MapLayout.BUSHY
            GameMode.HEIST, GameMode.HOT_ZONE -> MapLayout.CLOSED
            else -> MapLayout.BALANCED
        }

        if (brawler.bestLayouts.contains(currentLayout)) {
            mapScore += 35.0
        }
        if (draftState.map?.topTierBrawlers?.contains(brawler.id) == true) {
            mapScore += 15.0
        }
        mapScore = mapScore.coerceIn(25.0, 100.0)

        // 4. Counter Advantage Against Enemy Team (0 - 100 sub-score)
        var counterScore = 50.0
        val counterAdvantages = mutableListOf<String>()
        val counterReasons = mutableListOf<String>()

        if (enemies.isNotEmpty()) {
            var counterPoints = 0
            var counteredPenalty = 0

            enemies.forEach { enemy ->
                if (brawler.counterAgainst.contains(enemy.id)) {
                    counterPoints += 25
                    counterAdvantages.add(enemy.name)
                    counterReasons.add("Rakip ${enemy.name}'a karşı net üstünlük ve counter avantajı sağlar.")
                }
                if (brawler.counteredBy.contains(enemy.id)) {
                    counteredPenalty += 20
                }
            }

            counterScore += (counterPoints - counteredPenalty)
        } else {
            // No enemies picked yet: blind first pick strength
            counterScore = if (brawler.tier == "S") 85.0 else 70.0
        }
        counterScore = counterScore.coerceIn(15.0, 99.0)

        // 5. Ally Team Synergy & Comp Balance (0 - 100 sub-score)
        var synergyScore = 50.0
        val synergyAdvantages = mutableListOf<String>()
        val synergyReasons = mutableListOf<String>()

        if (allies.isNotEmpty()) {
            allies.forEach { ally ->
                if (brawler.synergiesWith.contains(ally.id)) {
                    synergyScore += 25.0
                    synergyAdvantages.add(ally.name)
                    synergyReasons.add("Takım arkadaşı ${ally.name} ile harika kombo ve sinerji oluşturur.")
                }
            }

            // Comp balance check: don't pick 3 marksmen on bushy, or 3 tanks on open
            val allyClasses = allies.map { it.brawlerClass }
            if (allyClasses.contains(brawler.brawlerClass)) {
                synergyScore -= 10.0 // slight penalty for duplicate class
            } else {
                synergyScore += 10.0 // bonus for class diversity
            }
        } else {
            synergyScore = 75.0
        }
        synergyScore = synergyScore.coerceIn(20.0, 98.0)

        // Weighted Total Score (0 - 100)
        // Counter: 30%, Map: 25%, Mode: 20%, Synergy: 15%, Base/Tier: 10%
        val total = (counterScore * 0.30) +
                    (mapScore * 0.25) +
                    (modeScore * 0.20) +
                    (synergyScore * 0.15) +
                    (baseScore * 0.10)

        val reasons = mutableListOf<String>()
        val mapName = draftState.map?.name ?: draftState.mode.displayNameTr

        if (draftState.map?.topTierBrawlers?.contains(brawler.id) == true) {
            reasons.add("$mapName haritasının en yüksek kazanma oranlı meta seçimi.")
        } else if (brawler.bestModes.contains(draftState.mode)) {
            reasons.add("${draftState.mode.displayNameTr} modunda %${brawler.winRate} kazanma oranıyla çok güçlü.")
        }

        reasons.addAll(counterReasons.take(2))
        reasons.addAll(synergyReasons.take(1))

        if (reasons.isEmpty()) {
            reasons.add(brawler.shortStrategyTr)
        }

        return Recommendation(
            brawler = brawler,
            rank = 1,
            totalScore = total.roundToInt().coerceIn(1, 100),
            counterScore = counterScore.roundToInt(),
            mapScore = mapScore.roundToInt(),
            synergyScore = synergyScore.roundToInt(),
            reasons = reasons,
            counterPoints = counterAdvantages,
            synergyPoints = synergyAdvantages
        )
    }
}
