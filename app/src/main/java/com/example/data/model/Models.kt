package com.example.data.model

enum class GameMode(
    val id: String,
    val displayNameTr: String,
    val displayNameEn: String,
    val iconEmoji: String,
    val preferredClasses: List<BrawlerClass>
) {
    GEM_GRAB("gem_grab", "Elmas Kapmaca", "Gem Grab", "💎", listOf(BrawlerClass.CONTROLLER, BrawlerClass.SUPPORT, BrawlerClass.DAMAGE_DEALER)),
    BRAWL_BALL("brawl_ball", "Savaş Topu", "Brawl Ball", "⚽", listOf(BrawlerClass.TANK, BrawlerClass.ASSASSIN, BrawlerClass.DAMAGE_DEALER)),
    HEIST("heist", "Kasa Soygunu", "Heist", "💰", listOf(BrawlerClass.DAMAGE_DEALER, BrawlerClass.ARTILLERY, BrawlerClass.MARKSMAN)),
    BOUNTY("bounty", "Ödül Avı", "Bounty", "⭐", listOf(BrawlerClass.MARKSMAN, BrawlerClass.ASSASSIN, BrawlerClass.CONTROLLER)),
    HOT_ZONE("hot_zone", "Sıcak Bölge", "Hot Zone", "🚩", listOf(BrawlerClass.CONTROLLER, BrawlerClass.TANK, BrawlerClass.ARTILLERY)),
    KNOCKOUT("knockout", "Nakavt", "Knockout", "🥊", listOf(BrawlerClass.MARKSMAN, BrawlerClass.ASSASSIN, BrawlerClass.SUPPORT)),
    WIPEOUT("wipeout", "Hesaplaşma / Yok Oluş", "Wipeout", "💀", listOf(BrawlerClass.MARKSMAN, BrawlerClass.DAMAGE_DEALER, BrawlerClass.ASSASSIN));

    companion object {
        fun fromString(name: String?): GameMode {
            if (name == null) return GEM_GRAB
            val clean = name.lowercase().trim()
            return entries.firstOrNull {
                it.name.equals(clean, ignoreCase = true) ||
                it.displayNameTr.lowercase().contains(clean) ||
                it.displayNameEn.lowercase().contains(clean) ||
                clean.contains(it.id)
            } ?: GEM_GRAB
        }
    }
}

enum class MapLayout {
    OPEN,       // Açık harita - Marksman / Long range
    CLOSED,     // Duvar ağırlıklı - Thrower / Artillery
    BUSHY,      // Çalı ağırlıklı - Tank / Assassin
    BALANCED    // Dengeli / Hibrit
}

data class BrawlMap(
    val id: String,
    val name: String,
    val mode: GameMode,
    val layout: MapLayout,
    val topTierBrawlers: List<String>
)

enum class BrawlerClass(val titleTr: String) {
    DAMAGE_DEALER("Hasar Verici"),
    ASSASSIN("Suikastçı"),
    MARKSMAN("Keskin Nişancı"),
    ARTILLERY("Topçu / Atıcı"),
    TANK("Tank"),
    SUPPORT("Destek"),
    CONTROLLER("Alan Kontrolü")
}

enum class RangeType(val titleTr: String) {
    VERY_SHORT("Çok Kısa"),
    SHORT("Kısa"),
    MEDIUM("Orta"),
    LONG("Uzun"),
    VERY_LONG("Çok Uzun")
}

data class Brawler(
    val id: String,
    val name: String,
    val brawlerClass: BrawlerClass,
    val range: RangeType,
    val tier: String, // S, A, B, C
    val winRate: Double, // e.g. 56.4
    val pickRate: Double, // e.g. 8.2
    val avatarColorHex: String,
    val bestModes: List<GameMode>,
    val bestLayouts: List<MapLayout>,
    val counterAgainst: List<String>,
    val counteredBy: List<String>,
    val synergiesWith: List<String>,
    val shortStrategyTr: String
)

enum class TeamRole {
    ALLY,
    ENEMY
}

enum class PickTurn(val titleTr: String) {
    FIRST_PICK("İlk Seçim (Takımımız)"),
    SECOND_PICK("İkinci Seçim (Rakip)"),
    COUNTER_PICK("Karşı Seçim (Counter)"),
    LAST_PICK("Son Seçim (Kapanış)")
}

data class DraftState(
    val map: BrawlMap? = null,
    val mode: GameMode = GameMode.BRAWL_BALL,
    val allyPicks: List<String> = emptyList(),
    val enemyPicks: List<String> = emptyList(),
    val allyBans: List<String> = emptyList(),
    val enemyBans: List<String> = emptyList(),
    val pickTurn: PickTurn = PickTurn.COUNTER_PICK
)

data class Recommendation(
    val brawler: Brawler,
    val rank: Int,
    val totalScore: Int, // 0 - 100
    val counterScore: Int, // 0 - 100
    val mapScore: Int, // 0 - 100
    val synergyScore: Int, // 0 - 100
    val reasons: List<String>,
    val counterPoints: List<String>,
    val synergyPoints: List<String>
)

data class AnalysisResult(
    val draftState: DraftState,
    val recommendations: List<Recommendation>,
    val detectionConfidence: Float, // 0.0 to 1.0
    val detectedSummary: String,
    val isSuccess: Boolean,
    val errorMessage: String? = null,
    val requiresApiKey: Boolean = false
)

data class AppSettings(
    val bubbleSize: BubbleSize = BubbleSize.NORMAL,
    val bubbleAlpha: Float = 0.95f,
    val recommendationCount: Int = 3,
    val autoAnalyze: Boolean = false,
    val language: String = "tr",
    val darkTheme: Boolean = true,
    val geminiApiKey: String = ""
)

enum class BubbleSize(val dpSize: Int, val titleTr: String) {
    SMALL(46, "Küçük (46dp)"),
    NORMAL(56, "Normal (56dp)"),
    LARGE(68, "Büyük (68dp)")
}
