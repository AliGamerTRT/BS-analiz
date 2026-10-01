package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.DraftState
import com.example.data.model.GameMode
import com.example.data.repository.BrawlDatabase
import com.example.data.repository.DraftAnalysisEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun readStringFromContext() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Brawl Pick", appName)
    }

    @Test
    fun draftEngine_recommendsAntiTankPicksAgainstFrank() {
        val brawlBallMap = BrawlDatabase.maps.first { it.mode == GameMode.BRAWL_BALL }
        val draftState = DraftState(
            map = brawlBallMap,
            mode = GameMode.BRAWL_BALL,
            enemyPicks = listOf("frank"),
            allyBans = listOf("draco"),
            enemyBans = listOf("mortis")
        )

        val recommendations = DraftAnalysisEngine.analyzeAndRecommend(draftState, 3)

        // Should return up to 3 recommendations
        assertTrue(recommendations.isNotEmpty())
        assertTrue(recommendations.size <= 3)

        // Banned brawlers should NOT appear in recommendations
        val recIds = recommendations.map { it.brawler.id }
        assertFalse(recIds.contains("draco"))
        assertFalse(recIds.contains("mortis"))
        assertFalse(recIds.contains("frank"))

        // Top recommendation should have high counter or map score
        val topRec = recommendations.first()
        assertTrue(topRec.totalScore > 50)
        assertTrue(topRec.reasons.isNotEmpty())
    }
}
