package com.example.data.repository

import com.example.data.model.Brawler
import com.example.data.model.BrawlerClass
import com.example.data.model.BrawlMap
import com.example.data.model.GameMode
import com.example.data.model.MapLayout
import com.example.data.model.RangeType

object BrawlDatabase {

    val brawlers: List<Brawler> = listOf(
        // MARKSMEN
        Brawler(
            id = "piper",
            name = "Piper",
            brawlerClass = BrawlerClass.MARKSMAN,
            range = RangeType.VERY_LONG,
            tier = "S",
            winRate = 56.8,
            pickRate = 9.4,
            avatarColorHex = "#70CFFF",
            bestModes = listOf(GameMode.BOUNTY, GameMode.KNOCKOUT, GameMode.WIPEOUT),
            bestLayouts = listOf(MapLayout.OPEN),
            counterAgainst = listOf("brock", "colt", "shelly", "bull", "rosa", "el_primo", "bo"),
            counteredBy = listOf("mortis", "edgar", "leon", "fang", "mico", "buzz"),
            synergiesWith = listOf("gene", "byron", "tick", "gray", "belle"),
            shortStrategyTr = "Açık haritalarda uzaktan tek atışla yüksek patlama hasarı. Keskin nişancı düellolarında lider."
        ),
        Brawler(
            id = "angelo",
            name = "Angelo",
            brawlerClass = BrawlerClass.MARKSMAN,
            range = RangeType.VERY_LONG,
            tier = "S",
            winRate = 57.2,
            pickRate = 8.1,
            avatarColorHex = "#8EE000",
            bestModes = listOf(GameMode.KNOCKOUT, GameMode.BOUNTY, GameMode.WIPEOUT),
            bestLayouts = listOf(MapLayout.OPEN, MapLayout.BALANCED),
            counterAgainst = listOf("bull", "frank", "el_primo", "jacky", "shelly", "rosa"),
            counteredBy = listOf("cordelius", "edgar", "fang", "mico"),
            synergiesWith = listOf("piper", "byron", "max", "gene"),
            shortStrategyTr = "Su üstünde yürüyebilme ve şarjlı devasa tek atış hasarıyla rakip hattı dağıtır."
        ),
        Brawler(
            id = "belle",
            name = "Belle",
            brawlerClass = BrawlerClass.MARKSMAN,
            range = RangeType.VERY_LONG,
            tier = "S",
            winRate = 55.4,
            pickRate = 7.5,
            avatarColorHex = "#F4D03F",
            bestModes = listOf(GameMode.KNOCKOUT, GameMode.BOUNTY, GameMode.HOT_ZONE, GameMode.HEIST),
            bestLayouts = listOf(MapLayout.OPEN, MapLayout.BALANCED),
            counterAgainst = listOf("frank", "draco", "meg", "el_primo", "bull", "rosa"),
            counteredBy = listOf("mortis", "edgar", "buzz", "mico"),
            synergiesWith = listOf("piper", "brock", "colette", "colt"),
            shortStrategyTr = "İşaretleme ultisi ile yüksek canı olan tankları eritir ve seken mermilerle kümelenmeleri engeller."
        ),
        Brawler(
            id = "brock",
            name = "Brock",
            brawlerClass = BrawlerClass.MARKSMAN,
            range = RangeType.LONG,
            tier = "A",
            winRate = 52.8,
            pickRate = 6.9,
            avatarColorHex = "#3498DB",
            bestModes = listOf(GameMode.HEIST, GameMode.BOUNTY, GameMode.KNOCKOUT),
            bestLayouts = listOf(MapLayout.OPEN, MapLayout.BALANCED),
            counterAgainst = listOf("dynamike", "barley", "sprout", "tick"),
            counteredBy = listOf("mortis", "leon", "crow", "edgar"),
            synergiesWith = listOf("piper", "belle", "byron", "colt"),
            shortStrategyTr = "Duvar kırıcı roket ultisi ve alan hasarıyla haritayı açar, kasa soygununda etkilidir."
        ),
        Brawler(
            id = "mandy",
            name = "Mandy",
            brawlerClass = BrawlerClass.MARKSMAN,
            range = RangeType.VERY_LONG,
            tier = "A",
            winRate = 53.5,
            pickRate = 6.2,
            avatarColorHex = "#FF69B4",
            bestModes = listOf(GameMode.KNOCKOUT, GameMode.BOUNTY, GameMode.WIPEOUT),
            bestLayouts = listOf(MapLayout.OPEN),
            counterAgainst = listOf("tick", "barley", "brock", "gale", "colt"),
            counteredBy = listOf("mortis", "mico", "edgar", "fang"),
            synergiesWith = listOf("piper", "gene", "byron"),
            shortStrategyTr = "Sabit dururken ekran dışına uzanan menzili ve haritayı boydan boya delen ultisi ölümcüldür."
        ),
        Brawler(
            id = "nani",
            name = "Nani",
            brawlerClass = BrawlerClass.MARKSMAN,
            range = RangeType.VERY_LONG,
            tier = "S",
            winRate = 56.1,
            pickRate = 5.8,
            avatarColorHex = "#E67E22",
            bestModes = listOf(GameMode.BOUNTY, GameMode.KNOCKOUT, GameMode.WIPEOUT),
            bestLayouts = listOf(MapLayout.OPEN),
            counterAgainst = listOf("piper", "brock", "mandy", "belle", "bea"),
            counteredBy = listOf("edgar", "fang", "buzz", "cordelius"),
            synergiesWith = listOf("byron", "gene", "gray"),
            shortStrategyTr = "Dürbünlü nişancılara karşı en iyi karşı-seçim. Peep ve iade kalkanıyla tek atış potansiyeli."
        ),

        // DAMAGE DEALERS
        Brawler(
            id = "clancy",
            name = "Clancy",
            brawlerClass = BrawlerClass.DAMAGE_DEALER,
            range = RangeType.MEDIUM,
            tier = "S",
            winRate = 58.5,
            pickRate = 9.8,
            avatarColorHex = "#E74C3C",
            bestModes = listOf(GameMode.BRAWL_BALL, GameMode.HEIST, GameMode.HOT_ZONE, GameMode.GEM_GRAB),
            bestLayouts = listOf(MapLayout.BALANCED, MapLayout.BUSHY),
            counterAgainst = listOf("frank", "bull", "el_primo", "draco", "jacky", "rosa", "buster"),
            counteredBy = listOf("piper", "angelo", "nani", "belle"),
            synergiesWith = listOf("pavel", "max", "gene", "sandy"),
            shortStrategyTr = "Aşama 3'e ulaştığında ultisi tüm tankları ve kasayı saniyeler içinde yok eder."
        ),
        Brawler(
            id = "colette",
            name = "Colette",
            brawlerClass = BrawlerClass.DAMAGE_DEALER,
            range = RangeType.LONG,
            tier = "S",
            winRate = 55.9,
            pickRate = 8.4,
            avatarColorHex = "#FF5E7E",
            bestModes = listOf(GameMode.HEIST, GameMode.BRAWL_BALL, GameMode.HOT_ZONE),
            bestLayouts = listOf(MapLayout.BALANCED, MapLayout.OPEN),
            counterAgainst = listOf("frank", "draco", "meg", "el_primo", "bull", "rosa", "jacky"),
            counteredBy = listOf("piper", "nani", "edgar", "mortis"),
            synergiesWith = listOf("clancy", "colt", "stu", "max"),
            shortStrategyTr = "Yüzdelik hasar sistemiyle tüm tankların doğrudan panzehiri ve kasa eritme makinesi."
        ),
        Brawler(
            id = "colt",
            name = "Colt",
            brawlerClass = BrawlerClass.DAMAGE_DEALER,
            range = RangeType.LONG,
            tier = "A",
            winRate = 51.4,
            pickRate = 7.9,
            avatarColorHex = "#2980B9",
            bestModes = listOf(GameMode.HEIST, GameMode.BRAWL_BALL, GameMode.BOUNTY),
            bestLayouts = listOf(MapLayout.OPEN, MapLayout.BALANCED),
            counterAgainst = listOf("frank", "el_primo", "bull", "jacky"),
            counteredBy = listOf("piper", "nani", "edgar", "mortis"),
            synergiesWith = listOf("brock", "belle", "byron"),
            shortStrategyTr = "Yüksek atış hızı ve duvar kırabilen ultisiyle Heist kasasında devasa dps."
        ),
        Brawler(
            id = "rico",
            name = "Rico",
            brawlerClass = BrawlerClass.DAMAGE_DEALER,
            range = RangeType.LONG,
            tier = "S",
            winRate = 55.7,
            pickRate = 7.1,
            avatarColorHex = "#9B59B6",
            bestModes = listOf(GameMode.BRAWL_BALL, GameMode.HOT_ZONE, GameMode.HEIST),
            bestLayouts = listOf(MapLayout.CLOSED, MapLayout.BALANCED),
            counterAgainst = listOf("shelly", "bull", "rosa", "el_primo", "draco"),
            counteredBy = listOf("dynamike", "tick", "barley", "sprout"),
            synergiesWith = listOf("sprout", "tick", "gale"),
            shortStrategyTr = "Dar koridorlarda ve duvarlı haritalarda seken mermileriyle kaçış bırakmaz."
        ),

        // ASSASSINS
        Brawler(
            id = "mortis",
            name = "Mortis",
            brawlerClass = BrawlerClass.ASSASSIN,
            range = RangeType.SHORT,
            tier = "A",
            winRate = 52.1,
            pickRate = 9.5,
            avatarColorHex = "#4A235A",
            bestModes = listOf(GameMode.BOUNTY, GameMode.KNOCKOUT, GameMode.GEM_GRAB, GameMode.BRAWL_BALL),
            bestLayouts = listOf(MapLayout.BALANCED, MapLayout.BUSHY),
            counterAgainst = listOf("dynamike", "barley", "tick", "sprout", "piper", "brock", "mandy", "belle"),
            counteredBy = listOf("shelly", "bull", "jacky", "clancy", "gale", "surge"),
            synergiesWith = listOf("gene", "byron", "pavel", "sandy"),
            shortStrategyTr = "Atıcılara ve keskin nişancılara karşı birincil avcı. Hızlı hareket kabiliyetiyle bitirir."
        ),
        Brawler(
            id = "kenji",
            name = "Kenji",
            brawlerClass = BrawlerClass.ASSASSIN,
            range = RangeType.MEDIUM,
            tier = "S",
            winRate = 57.9,
            pickRate = 9.1,
            avatarColorHex = "#C0392B",
            bestModes = listOf(GameMode.BRAWL_BALL, GameMode.HOT_ZONE, GameMode.GEM_GRAB),
            bestLayouts = listOf(MapLayout.BUSHY, MapLayout.BALANCED),
            counterAgainst = listOf("piper", "brock", "colt", "tick", "dynamike", "belle"),
            counteredBy = listOf("clancy", "colette", "gale", "shelly"),
            synergiesWith = listOf("max", "byron", "sandy", "poco"),
            shortStrategyTr = "Çift vuruş kombosu ve iyileştirici dilimleme saldırısıyla yakın dövüşte yenilmez."
        ),
        Brawler(
            id = "cordelius",
            name = "Cordelius",
            brawlerClass = BrawlerClass.ASSASSIN,
            range = RangeType.SHORT,
            tier = "S",
            winRate = 56.3,
            pickRate = 8.7,
            avatarColorHex = "#1E8449",
            bestModes = listOf(GameMode.BRAWL_BALL, GameMode.HOT_ZONE, GameMode.GEM_GRAB),
            bestLayouts = listOf(MapLayout.BUSHY, MapLayout.CLOSED),
            counterAgainst = listOf("frank", "draco", "fang", "buzz", "mortis", "edgar", "clancy"),
            counteredBy = listOf("piper", "nani", "angelo", "bea"),
            synergiesWith = listOf("gene", "tara", "sandy"),
            shortStrategyTr = "Gölge alemine çekme ultisi ile rakibin en kritik taşıyıcısını etkisiz hale getirir."
        ),
        Brawler(
            id = "melodie",
            name = "Melodie",
            brawlerClass = BrawlerClass.ASSASSIN,
            range = RangeType.MEDIUM,
            tier = "S",
            winRate = 56.6,
            pickRate = 7.8,
            avatarColorHex = "#FF1493",
            bestModes = listOf(GameMode.BRAWL_BALL, GameMode.HEIST, GameMode.GEM_GRAB),
            bestLayouts = listOf(MapLayout.OPEN, MapLayout.BALANCED),
            counterAgainst = listOf("piper", "brock", "mandy", "dynamike", "tick"),
            counteredBy = listOf("clancy", "gale", "shelly", "colette"),
            synergiesWith = listOf("max", "byron", "sandy"),
            shortStrategyTr = "3 ardışık atılma ultisi ve etrafında dönen notaları ile anında gol pozisyonu ve dps."
        ),
        Brawler(
            id = "mico",
            name = "Mico",
            brawlerClass = BrawlerClass.ASSASSIN,
            range = RangeType.SHORT,
            tier = "A",
            winRate = 53.7,
            pickRate = 6.4,
            avatarColorHex = "#D35400",
            bestModes = listOf(GameMode.KNOCKOUT, GameMode.BOUNTY, GameMode.GEM_GRAB),
            bestLayouts = listOf(MapLayout.CLOSED, MapLayout.BUSHY),
            counterAgainst = listOf("dynamike", "barley", "sprout", "tick", "piper", "mandy"),
            counteredBy = listOf("clancy", "bull", "shelly", "jacky"),
            synergiesWith = listOf("gene", "byron", "gray"),
            shortStrategyTr = "Saldırı sırasında havada dokunulmaz olma yeteneğiyle tek vuruşluk karakterleri felç eder."
        ),
        Brawler(
            id = "crow",
            name = "Crow",
            brawlerClass = BrawlerClass.ASSASSIN,
            range = RangeType.LONG,
            tier = "A",
            winRate = 52.9,
            pickRate = 7.0,
            avatarColorHex = "#2C3E50",
            bestModes = listOf(GameMode.HEIST, GameMode.BRAWL_BALL, GameMode.GEM_GRAB),
            bestLayouts = listOf(MapLayout.OPEN, MapLayout.BALANCED),
            counterAgainst = listOf("draco", "frank", "el_primo", "bull", "rosa"),
            counteredBy = listOf("piper", "nani", "brock"),
            synergiesWith = listOf("clancy", "colette", "belle"),
            shortStrategyTr = "Zehir etkisiyle can yenilenmesini durdurur ve rakip hasarını %25 azaltır."
        ),
        Brawler(
            id = "leon",
            name = "Leon",
            brawlerClass = BrawlerClass.ASSASSIN,
            range = RangeType.LONG,
            tier = "A",
            winRate = 54.1,
            pickRate = 7.7,
            avatarColorHex = "#27AE60",
            bestModes = listOf(GameMode.GEM_GRAB, GameMode.KNOCKOUT, GameMode.BOUNTY),
            bestLayouts = listOf(MapLayout.BALANCED, MapLayout.BUSHY),
            counterAgainst = listOf("piper", "brock", "belle", "tick", "dynamike"),
            counteredBy = listOf("tara", "gene", "crow", "shelly"),
            synergiesWith = listOf("sandy", "max", "gene"),
            shortStrategyTr = "Görünmezlik pelerini ile rakip arka hattına sızıp keskin nişancıları düşürür."
        ),
        Brawler(
            id = "fang",
            name = "Fang",
            brawlerClass = BrawlerClass.ASSASSIN,
            range = RangeType.LONG,
            tier = "A",
            winRate = 53.6,
            pickRate = 8.3,
            avatarColorHex = "#E91E63",
            bestModes = listOf(GameMode.BRAWL_BALL, GameMode.KNOCKOUT, GameMode.WIPEOUT),
            bestLayouts = listOf(MapLayout.BALANCED, MapLayout.BUSHY),
            counterAgainst = listOf("piper", "tick", "dynamike", "sprout", "mandy", "brock"),
            counteredBy = listOf("clancy", "gale", "cordelius", "shelly"),
            synergiesWith = listOf("byron", "poco", "max"),
            shortStrategyTr = "Uçan tekme ultisiyle takım savaşlarında zincirleme düşürme potansiyeli."
        ),

        // TANKS
        Brawler(
            id = "draco",
            name = "Draco",
            brawlerClass = BrawlerClass.TANK,
            range = RangeType.SHORT,
            tier = "S",
            winRate = 57.5,
            pickRate = 8.9,
            avatarColorHex = "#8E44AD",
            bestModes = listOf(GameMode.BRAWL_BALL, GameMode.HOT_ZONE, GameMode.GEM_GRAB),
            bestLayouts = listOf(MapLayout.BUSHY, MapLayout.BALANCED),
            counterAgainst = listOf("mortis", "edgar", "mico", "buzz", "leon"),
            counteredBy = listOf("colette", "clancy", "belle", "cordelius", "crow"),
            synergiesWith = listOf("byron", "poco", "kit", "max"),
            shortStrategyTr = "Ejderhasına bindiğinde hasar azaltımı ve alev püskürtmesiyle durdurulamaz tank."
        ),
        Brawler(
            id = "frank",
            name = "Frank",
            brawlerClass = BrawlerClass.TANK,
            range = RangeType.MEDIUM,
            tier = "S",
            winRate = 56.4,
            pickRate = 9.2,
            avatarColorHex = "#7D3C98",
            bestModes = listOf(GameMode.BRAWL_BALL, GameMode.HOT_ZONE, GameMode.GEM_GRAB),
            bestLayouts = listOf(MapLayout.BUSHY, MapLayout.CLOSED),
            counterAgainst = listOf("mortis", "edgar", "leon", "mico"),
            counteredBy = listOf("colette", "clancy", "gale", "belle", "cordelius", "shelly"),
            synergiesWith = listOf("poco", "byron", "max", "gene"),
            shortStrategyTr = "Canı azaldıkça hızlanan saldırısı ve dev alan sersemletmesiyle bölgeyi kilitler."
        ),
        Brawler(
            id = "buster",
            name = "Buster",
            brawlerClass = BrawlerClass.TANK,
            range = RangeType.SHORT,
            tier = "S",
            winRate = 55.8,
            pickRate = 7.4,
            avatarColorHex = "#16A085",
            bestModes = listOf(GameMode.BRAWL_BALL, GameMode.HOT_ZONE, GameMode.GEM_GRAB),
            bestLayouts = listOf(MapLayout.BALANCED, MapLayout.BUSHY),
            counterAgainst = listOf("piper", "brock", "colt", "rico", "belle"),
            counteredBy = listOf("clancy", "colette", "cordelius", "frank"),
            synergiesWith = listOf("byron", "sandy", "gene", "max"),
            shortStrategyTr = "Mermi yansıtıcı kalkanı ile takımını nişancı mermilerinden korur ve karşı hasar üretir."
        ),
        Brawler(
            id = "meg",
            name = "Meg",
            brawlerClass = BrawlerClass.TANK,
            range = RangeType.LONG,
            tier = "S",
            winRate = 56.7,
            pickRate = 8.5,
            avatarColorHex = "#F39C12",
            bestModes = listOf(GameMode.HOT_ZONE, GameMode.BRAWL_BALL, GameMode.GEM_GRAB),
            bestLayouts = listOf(MapLayout.BALANCED, MapLayout.OPEN),
            counterAgainst = listOf("mortis", "edgar", "fang", "buzz"),
            counteredBy = listOf("colette", "clancy", "belle", "crow"),
            synergiesWith = listOf("byron", "poco", "max"),
            shortStrategyTr = "Mecha formuyla devasa can havuzu ve geniş koni atışıyla alan kontrolü sağlar."
        ),
        Brawler(
            id = "el_primo",
            name = "El Primo",
            brawlerClass = BrawlerClass.TANK,
            range = RangeType.SHORT,
            tier = "B",
            winRate = 51.2,
            pickRate = 6.0,
            avatarColorHex = "#2E86C1",
            bestModes = listOf(GameMode.BRAWL_BALL, GameMode.HEIST),
            bestLayouts = listOf(MapLayout.BUSHY, MapLayout.CLOSED),
            counterAgainst = listOf("mortis", "edgar", "frank"),
            counteredBy = listOf("colette", "clancy", "gale", "shelly", "cordelius"),
            synergiesWith = listOf("poco", "byron", "max"),
            shortStrategyTr = "Süper atlayışıyla duvarları yıkar, topu sürer ve kasa üzerinde baskı kurar."
        ),

        // ARTILLERY / THROWERS
        Brawler(
            id = "larry_lawrie",
            name = "Larry & Lawrie",
            brawlerClass = BrawlerClass.ARTILLERY,
            range = RangeType.LONG,
            tier = "S",
            winRate = 56.2,
            pickRate = 8.6,
            avatarColorHex = "#34495E",
            bestModes = listOf(GameMode.HOT_ZONE, GameMode.GEM_GRAB, GameMode.KNOCKOUT, GameMode.BRAWL_BALL),
            bestLayouts = listOf(MapLayout.CLOSED, MapLayout.BALANCED),
            counterAgainst = listOf("frank", "draco", "bull", "rosa", "jacky", "8_bit"),
            counteredBy = listOf("mortis", "mico", "edgar", "fang"),
            synergiesWith = listOf("gale", "cordelius", "gene", "buster"),
            shortStrategyTr = "Duvar arkasından ikili patlama ve Lawrie robotu ile 4v3 avantajı yaratır."
        ),
        Brawler(
            id = "tick",
            name = "Tick",
            brawlerClass = BrawlerClass.ARTILLERY,
            range = RangeType.VERY_LONG,
            tier = "A",
            winRate = 53.4,
            pickRate = 7.1,
            avatarColorHex = "#C0392B",
            bestModes = listOf(GameMode.KNOCKOUT, GameMode.BOUNTY, GameMode.HOT_ZONE),
            bestLayouts = listOf(MapLayout.CLOSED, MapLayout.BALANCED),
            counterAgainst = listOf("8_bit", "pam", "frank", "jacky"),
            counteredBy = listOf("mortis", "mico", "edgar", "fang", "gray"),
            synergiesWith = listOf("piper", "gene", "gale"),
            shortStrategyTr = "En uzun menzilli mayınlama ile koridorları tamamen kapatır ve baskı kurar."
        ),
        Brawler(
            id = "dynamike",
            name = "Dynamike",
            brawlerClass = BrawlerClass.ARTILLERY,
            range = RangeType.LONG,
            tier = "A",
            winRate = 52.5,
            pickRate = 8.8,
            avatarColorHex = "#E74C3C",
            bestModes = listOf(GameMode.BRAWL_BALL, GameMode.HEIST, GameMode.HOT_ZONE),
            bestLayouts = listOf(MapLayout.CLOSED),
            counterAgainst = listOf("frank", "jacky", "bull", "el_primo"),
            counteredBy = listOf("mortis", "mico", "edgar", "fang"),
            synergiesWith = listOf("gale", "shelly", "buster"),
            shortStrategyTr = "Yüksek patlama hasarı ve sersemletici gadget ile duvar arkasından anında yok eder."
        ),
        Brawler(
            id = "barley",
            name = "Barley",
            brawlerClass = BrawlerClass.ARTILLERY,
            range = RangeType.LONG,
            tier = "A",
            winRate = 54.3,
            pickRate = 6.2,
            avatarColorHex = "#D4AC0D",
            bestModes = listOf(GameMode.HEIST, GameMode.HOT_ZONE),
            bestLayouts = listOf(MapLayout.CLOSED),
            counterAgainst = listOf("frank", "el_primo", "bull", "rosa"),
            counteredBy = listOf("mortis", "mico", "edgar", "fang"),
            synergiesWith = listOf("rico", "gale", "colt"),
            shortStrategyTr = "Zehirli sıvısıyla alan kapatma ve Heist kasasında sürekli hasar."
        ),

        // CONTROLLERS
        Brawler(
            id = "gale",
            name = "Gale",
            brawlerClass = BrawlerClass.CONTROLLER,
            range = RangeType.LONG,
            tier = "S",
            winRate = 57.0,
            pickRate = 9.3,
            avatarColorHex = "#5DADE2",
            bestModes = listOf(GameMode.BRAWL_BALL, GameMode.HOT_ZONE, GameMode.GEM_GRAB),
            bestLayouts = listOf(MapLayout.BALANCED, MapLayout.BUSHY, MapLayout.OPEN),
            counterAgainst = listOf("mortis", "edgar", "fang", "buzz", "frank", "draco", "el_primo", "bull"),
            counteredBy = listOf("piper", "angelo", "nani", "mandy"),
            synergiesWith = listOf("larry_lawrie", "dynamike", "tick", "piper"),
            shortStrategyTr = "Kasırga ve itici ultisiyle tüm suikastçıları ve tankları tamamen etkisiz bırakır."
        ),
        Brawler(
            id = "moe",
            name = "Moe",
            brawlerClass = BrawlerClass.CONTROLLER,
            range = RangeType.LONG,
            tier = "S",
            winRate = 58.1,
            pickRate = 8.7,
            avatarColorHex = "#A0522D",
            bestModes = listOf(GameMode.BRAWL_BALL, GameMode.HOT_ZONE, GameMode.GEM_GRAB, GameMode.KNOCKOUT),
            bestLayouts = listOf(MapLayout.BALANCED, MapLayout.CLOSED),
            counterAgainst = listOf("frank", "el_primo", "bull", "rosa", "jacky", "colt"),
            counteredBy = listOf("piper", "angelo", "nani"),
            synergiesWith = listOf("max", "byron", "gene"),
            shortStrategyTr = "Delici matkap makinesi ve seken taş saldırısıyla dar alanlarda durdurulamaz kontrol."
        ),
        Brawler(
            id = "charlie",
            name = "Charlie",
            brawlerClass = BrawlerClass.CONTROLLER,
            range = RangeType.LONG,
            tier = "A",
            winRate = 54.8,
            pickRate = 7.1,
            avatarColorHex = "#8E44AD",
            bestModes = listOf(GameMode.GEM_GRAB, GameMode.BRAWL_BALL, GameMode.HOT_ZONE),
            bestLayouts = listOf(MapLayout.BALANCED),
            counterAgainst = listOf("draco", "frank", "fang", "buzz", "edgar"),
            counteredBy = listOf("tick", "larry_lawrie", "piper"),
            synergiesWith = listOf("colette", "clancy", "piper"),
            shortStrategyTr = "Koza ultisi ile rakibin en güçlü brawler'ını 5 saniye maçtan siler, elmasları düşürtür."
        ),
        Brawler(
            id = "gene",
            name = "Gene",
            brawlerClass = BrawlerClass.CONTROLLER,
            range = RangeType.VERY_LONG,
            tier = "S",
            winRate = 55.6,
            pickRate = 7.9,
            avatarColorHex = "#AF7AC5",
            bestModes = listOf(GameMode.KNOCKOUT, GameMode.BOUNTY, GameMode.GEM_GRAB),
            bestLayouts = listOf(MapLayout.OPEN, MapLayout.BALANCED),
            counterAgainst = listOf("piper", "brock", "mandy", "bea"),
            counteredBy = listOf("draco", "frank", "buster"),
            synergiesWith = listOf("piper", "angelo", "nani", "clancy", "tara"),
            shortStrategyTr = "Sihirli el çekişi ile rakip taşıyıcıyı yakalayıp anında takımına yedirir."
        ),

        // SUPPORTS
        Brawler(
            id = "byron",
            name = "Byron",
            brawlerClass = BrawlerClass.SUPPORT,
            range = RangeType.VERY_LONG,
            tier = "S",
            winRate = 56.5,
            pickRate = 8.2,
            avatarColorHex = "#1ABC9C",
            bestModes = listOf(GameMode.KNOCKOUT, GameMode.BOUNTY, GameMode.BRAWL_BALL, GameMode.GEM_GRAB),
            bestLayouts = listOf(MapLayout.OPEN, MapLayout.BALANCED),
            counterAgainst = listOf("draco", "frank", "el_primo", "bull", "rosa"),
            counteredBy = listOf("mortis", "edgar", "fang", "mico"),
            synergiesWith = listOf("draco", "frank", "buster", "piper", "angelo"),
            shortStrategyTr = "Ön saftaki tankları devasa iyileştirir, rakipleri ise zehirleyip iyileşmelerini keser."
        ),
        Brawler(
            id = "max",
            name = "Max",
            brawlerClass = BrawlerClass.SUPPORT,
            range = RangeType.LONG,
            tier = "S",
            winRate = 55.9,
            pickRate = 8.0,
            avatarColorHex = "#E74C3C",
            bestModes = listOf(GameMode.BRAWL_BALL, GameMode.HOT_ZONE, GameMode.GEM_GRAB),
            bestLayouts = listOf(MapLayout.OPEN, MapLayout.BALANCED),
            counterAgainst = listOf("tick", "sprout", "dynamike", "barley"),
            counteredBy = listOf("crow", "piper", "nani"),
            synergiesWith = listOf("clancy", "draco", "frank", "gene", "kenji"),
            shortStrategyTr = "Tüm takıma verdiği hiper hız ultisi ile anında pozisyon üstünlüğü ve baskı sağlar."
        ),
        Brawler(
            id = "sandy",
            name = "Sandy",
            brawlerClass = BrawlerClass.SUPPORT,
            range = RangeType.MEDIUM,
            tier = "S",
            winRate = 56.1,
            pickRate = 7.7,
            avatarColorHex = "#BB8FCE",
            bestModes = listOf(GameMode.BRAWL_BALL, GameMode.HOT_ZONE, GameMode.GEM_GRAB),
            bestLayouts = listOf(MapLayout.BALANCED, MapLayout.BUSHY),
            counterAgainst = listOf("piper", "brock", "colt", "belle"),
            counteredBy = listOf("clancy", "draco", "frank"),
            synergiesWith = listOf("clancy", "kenji", "gene", "mortis"),
            shortStrategyTr = "Kum fırtınası ultisi tüm takımı gizleyerek rakibin nişan almasını imkansızlaştırır."
        ),
        Brawler(
            id = "kit",
            name = "Kit",
            brawlerClass = BrawlerClass.SUPPORT,
            range = RangeType.SHORT,
            tier = "A",
            winRate = 54.7,
            pickRate = 7.2,
            avatarColorHex = "#FAD7A0",
            bestModes = listOf(GameMode.KNOCKOUT, GameMode.WIPEOUT, GameMode.BRAWL_BALL),
            bestLayouts = listOf(MapLayout.BALANCED, MapLayout.BUSHY),
            counterAgainst = listOf("piper", "mandy", "brock"),
            counteredBy = listOf("gale", "cordelius", "clancy"),
            synergiesWith = listOf("draco", "buster", "frank", "rosa"),
            shortStrategyTr = "Dost tankın sırtına atlayarak onu iyileştirirken iplik bombaları yağdırır."
        ),
        Brawler(
            id = "poco",
            name = "Poco",
            brawlerClass = BrawlerClass.SUPPORT,
            range = RangeType.MEDIUM,
            tier = "A",
            winRate = 53.9,
            pickRate = 6.8,
            avatarColorHex = "#F4D03F",
            bestModes = listOf(GameMode.BRAWL_BALL, GameMode.HOT_ZONE),
            bestLayouts = listOf(MapLayout.BALANCED, MapLayout.BUSHY),
            counterAgainst = listOf("mortis", "edgar", "crow"),
            counteredBy = listOf("clancy", "colette", "piper"),
            synergiesWith = listOf("frank", "draco", "jacky", "buster"),
            shortStrategyTr = "Geniş dalga iyileştirmesiyle çift tank kompozisyonlarını ölümsüz kılar."
        )
    )

    val maps: List<BrawlMap> = listOf(
        // KNOCKOUT
        BrawlMap("shooting_star", "Kayan Yıldız (Shooting Star)", GameMode.BOUNTY, MapLayout.OPEN, listOf("piper", "nani", "angelo", "mandy", "belle", "byron")),
        BrawlMap("flaring_phoenix", "Parlayan Anka (Flaring Phoenix)", GameMode.KNOCKOUT, MapLayout.BALANCED, listOf("piper", "angelo", "belle", "byron", "gene", "nani")),
        BrawlMap("out_in_the_open", "Açık Alanda (Out in the Open)", GameMode.KNOCKOUT, MapLayout.OPEN, listOf("piper", "angelo", "mandy", "belle", "byron", "nani")),
        BrawlMap("belles_rock", "Belle'in Kayası (Belle's Rock)", GameMode.KNOCKOUT, MapLayout.BALANCED, listOf("larry_lawrie", "piper", "angelo", "tick", "nani")),
        BrawlMap("goldarm_gulch", "Altın Kol Vadisi (Goldarm Gulch)", GameMode.KNOCKOUT, MapLayout.BALANCED, listOf("piper", "belle", "byron", "angelo", "gene")),

        // BRAWL BALL
        BrawlBallMap("center_stage", "Orta Saha (Center Stage)", MapLayout.BUSHY, listOf("frank", "draco", "clancy", "gale", "kenji", "buster")),
        BrawlBallMap("super_beach", "Süper Plaj (Super Beach)", MapLayout.BALANCED, listOf("max", "melodie", "clancy", "draco", "gale", "sandy")),
        BrawlBallMap("pinball_dreams", "Tilt Düşleri (Pinball Dreams)", MapLayout.CLOSED, listOf("rico", "larry_lawrie", "clancy", "gale", "dynamike")),
        BrawlBallMap("sneaky_fields", "Sinsi Sahalar (Sneaky Fields)", MapLayout.BUSHY, listOf("draco", "clancy", "buster", "gale", "kenji", "byron")),
        BrawlBallMap("backyard_bowl", "Arka Bahçe (Backyard Bowl)", MapLayout.OPEN, listOf("piper", "angelo", "belle", "max", "byron")),

        // HEIST
        BrawlMap("safe_zone", "Güvenli Bölge (Safe Zone)", GameMode.HEIST, MapLayout.OPEN, listOf("colt", "colette", "clancy", "brock", "melodie", "angelo")),
        BrawlMap("pit_stop", "Mola Yeri (Pit Stop)", GameMode.HEIST, MapLayout.CLOSED, listOf("clancy", "colette", "dynamike", "barley", "rico")),
        BrawlMap("kaboom_canyon", "Kaboom Kanyonu (Kaboom Canyon)", GameMode.HEIST, MapLayout.OPEN, listOf("colt", "brock", "colette", "clancy", "piper")),
        BrawlMap("hot_potato", "Sıcak Patates (Hot Potato)", GameMode.HEIST, MapLayout.CLOSED, listOf("barley", "dynamike", "clancy", "colette", "rico")),

        // HOT ZONE
        BrawlMap("ring_of_fire", "Ateş Halkası (Ring of Fire)", GameMode.HOT_ZONE, MapLayout.BALANCED, listOf("clancy", "gale", "meg", "moe", "larry_lawrie", "amber")),
        BrawlMap("open_business", "Açık İşletme (Open Business)", GameMode.HOT_ZONE, MapLayout.CLOSED, listOf("rico", "larry_lawrie", "gale", "clancy", "moe")),
        BrawlMap("dueling_beetles", "Böcek Düellosu (Dueling Beetles)", GameMode.HOT_ZONE, MapLayout.CLOSED, listOf("gale", "clancy", "larry_lawrie", "rico", "draco")),
        BrawlMap("parallel_plays", "Paralel Oyunlar (Parallel Plays)", GameMode.HOT_ZONE, MapLayout.CLOSED, listOf("larry_lawrie", "clancy", "gale", "moe", "draco")),

        // GEM GRAB
        BrawlMap("hard_rock_mine", "Sert Taş Madeni (Hard Rock Mine)", GameMode.GEM_GRAB, MapLayout.BALANCED, listOf("gene", "clancy", "sandy", "gale", "larry_lawrie")),
        BrawlMap("double_swoosh", "Çifte Vızıltı (Double Swoosh)", GameMode.GEM_GRAB, MapLayout.BUSHY, listOf("draco", "clancy", "buster", "gene", "sandy", "kenji")),
        BrawlMap("undermine", "Köstebek Yuvası (Undermine)", GameMode.GEM_GRAB, MapLayout.BALANCED, listOf("gene", "clancy", "larry_lawrie", "gale", "sandy")),
        BrawlMap("minecart_madness", "Çılgın Vagon (Minecart Madness)", GameMode.GEM_GRAB, MapLayout.BALANCED, listOf("gene", "piper", "angelo", "clancy", "byron"))
    )

    private fun BrawlBallMap(id: String, name: String, layout: MapLayout, brawlers: List<String>) =
        BrawlMap(id, name, GameMode.BRAWL_BALL, layout, brawlers)

    fun getBrawlerById(id: String?): Brawler? {
        if (id == null) return null
        val clean = id.lowercase().trim().replace(" ", "_").replace("-", "_")
        return brawlers.firstOrNull {
            it.id == clean ||
            it.name.equals(clean, ignoreCase = true) ||
            it.id.contains(clean) ||
            clean.contains(it.id)
        }
    }

    fun searchBrawlers(query: String): List<Brawler> {
        val clean = query.lowercase().trim()
        if (clean.isEmpty()) return brawlers
        return brawlers.filter {
            it.name.lowercase().contains(clean) ||
            it.brawlerClass.titleTr.lowercase().contains(clean) ||
            it.id.contains(clean)
        }
    }
}
