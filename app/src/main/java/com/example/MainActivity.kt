package com.example

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.repository.SettingsRepository
import com.example.service.FloatingBubbleService
import com.example.service.MediaProjectionHolder
import com.example.ui.screens.BrawlerGuideScreen
import com.example.ui.screens.DraftSimulatorScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.BrawlCyan
import com.example.ui.theme.BrawlGold
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        settingsRepository = SettingsRepository(this)

        // Capture screen metrics
        val metrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(metrics)
        MediaProjectionHolder.screenWidth = metrics.widthPixels
        MediaProjectionHolder.screenHeight = metrics.heightPixels
        MediaProjectionHolder.screenDensity = metrics.densityDpi

        setContent {
            val appSettings by settingsRepository.settingsFlow.collectAsState()

            MyApplicationTheme(darkTheme = appSettings.darkTheme) {
                MainAppScaffold(
                    settingsRepository = settingsRepository,
                    onOpenBrawlStars = { launchBrawlStars() }
                )
            }
        }
    }

    private fun launchBrawlStars() {
        val packageName = "com.supercell.brawlstars"
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            startActivity(launchIntent)
        } else {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
            } catch (e: Exception) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
            }
        }
    }
}

enum class NavTab(val title: String, val icon: ImageVector, val tag: String) {
    HOME("Asistan", Icons.Default.Home, "tab_home"),
    SIMULATOR("Simülatör", Icons.Default.AutoAwesome, "tab_simulator"),
    GUIDE("Rehber", Icons.AutoMirrored.Filled.MenuBook, "tab_guide"),
    SETTINGS("Ayarlar", Icons.Default.Settings, "tab_settings")
}

@Composable
fun MainAppScaffold(
    settingsRepository: SettingsRepository,
    onOpenBrawlStars: () -> Unit
) {
    val context = LocalContext.current
    var currentTab by remember { mutableStateOf(NavTab.HOME) }

    var hasOverlayPermission by remember { mutableStateOf(Settings.canDrawOverlays(context)) }
    var hasCapturePermission by remember { mutableStateOf(MediaProjectionHolder.isPermissionGranted()) }
    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            } else true
        )
    }

    var isServiceRunning by remember { mutableStateOf(FloatingBubbleService.isRunning) }

    // Overlay Permission Launcher
    val overlayLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        hasOverlayPermission = Settings.canDrawOverlays(context)
    }

    // Media Projection Launcher
    val mediaProjectionManager = remember {
        context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }
    val captureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            MediaProjectionHolder.resultCode = result.resultCode
            MediaProjectionHolder.resultData = result.data
            hasCapturePermission = true
            Toast.makeText(context, "Ekran yakalama izni verildi!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Ekran yakalama izni verilmedi. Asistan ekranı okuyamayabilir.", Toast.LENGTH_LONG).show()
        }
    }

    // Notification Permission Launcher
    val notificationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasNotificationPermission = granted
    }

    LaunchedEffect(Unit) {
        hasOverlayPermission = Settings.canDrawOverlays(context)
        isServiceRunning = FloatingBubbleService.isRunning
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF140F2B),
                contentColor = BrawlCyan
            ) {
                NavTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = currentTab == tab,
                        onClick = { currentTab = tab },
                        icon = { Icon(tab.icon, contentDescription = tab.title) },
                        label = { Text(tab.title, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = BrawlCyan,
                            indicatorColor = BrawlCyan,
                            unselectedIconColor = Color(0xFF8E84B8),
                            unselectedTextColor = Color(0xFF8E84B8)
                        ),
                        modifier = Modifier.testTag(tab.tag)
                    )
                }
            }
        }
    ) { innerPadding ->
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                NavTab.HOME -> {
                    HomeScreen(
                        isServiceRunning = isServiceRunning,
                        hasOverlayPermission = hasOverlayPermission,
                        hasCapturePermission = hasCapturePermission,
                        hasNotificationPermission = hasNotificationPermission,
                        onRequestOverlayPermission = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                val intent = Intent(
                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:${context.packageName}")
                                )
                                overlayLauncher.launch(intent)
                            }
                        },
                        onRequestCapturePermission = {
                            captureLauncher.launch(mediaProjectionManager.createScreenCaptureIntent())
                        },
                        onRequestNotificationPermission = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        },
                        onStartService = {
                            if (!hasOverlayPermission) {
                                Toast.makeText(context, "Lütfen önce diğer uygulamaların üzerinde gösterme iznini açın!", Toast.LENGTH_SHORT).show()
                                return@HomeScreen
                            }
                            if (!hasCapturePermission) {
                                captureLauncher.launch(mediaProjectionManager.createScreenCaptureIntent())
                                return@HomeScreen
                            }

                            val serviceIntent = Intent(context, FloatingBubbleService::class.java).apply {
                                action = FloatingBubbleService.ACTION_START
                            }
                            ContextCompat.startForegroundService(context, serviceIntent)
                            isServiceRunning = true
                            Toast.makeText(context, "Yüzen bubble başlatıldı! Ekranınızda belirecektir.", Toast.LENGTH_LONG).show()
                        },
                        onStopService = {
                            val serviceIntent = Intent(context, FloatingBubbleService::class.java).apply {
                                action = FloatingBubbleService.ACTION_STOP
                            }
                            context.startService(serviceIntent)
                            isServiceRunning = false
                        },
                        onOpenBrawlStars = onOpenBrawlStars
                    )
                }
                NavTab.SIMULATOR -> {
                    DraftSimulatorScreen()
                }
                NavTab.GUIDE -> {
                    BrawlerGuideScreen()
                }
                NavTab.SETTINGS -> {
                    SettingsScreen(settingsRepository)
                }
            }
        }
    }
}
