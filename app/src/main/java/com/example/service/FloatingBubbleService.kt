package com.example.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.PixelFormat
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.MainActivity
import com.example.R
import com.example.data.model.AnalysisResult
import com.example.data.model.AppSettings
import com.example.data.model.BubbleSize
import com.example.data.repository.ScreenAnalyzer
import com.example.data.repository.SettingsRepository
import com.example.ui.overlay.FloatingOverlayRoot
import com.example.ui.overlay.OverlayLifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.math.abs

class FloatingBubbleService : Service() {

    companion object {
        private const val TAG = "FloatingBubbleService"
        const val ACTION_START = "com.example.service.START"
        const val ACTION_STOP = "com.example.service.STOP"
        private const val NOTIFICATION_ID = 7712
        private const val CHANNEL_ID = "brawl_pick_channel"

        var isRunning: Boolean = false
            private set
    }

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private lateinit var windowManager: WindowManager
    private var overlayLifecycleOwner: OverlayLifecycleOwner? = null
    private var composeView: ComposeView? = null
    private var layoutParams: WindowManager.LayoutParams? = null

    private lateinit var settingsRepository: SettingsRepository

    // Observable UI states for Compose
    private var isScanningState by mutableStateOf(false)
    private var isExpandedState by mutableStateOf(false)
    private var analysisResultState by mutableStateOf<AnalysisResult?>(null)
    private var currentSettingsState by mutableStateOf(AppSettings())

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        isRunning = true
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        settingsRepository = SettingsRepository(this)

        createNotificationChannel()
        startForegroundWithNotification()

        serviceScope.launch {
            settingsRepository.settingsFlow.collect { settings ->
                currentSettingsState = settings
            }
        }

        // Initialize MediaProjection if granted
        val mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        MediaProjectionHolder.initProjection(mediaProjectionManager)

        initOverlayView()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopSelf()
            return START_NOT_STICKY
        }
        return START_STICKY
    }

    private fun startForegroundWithNotification() {
        val stopIntent = Intent(this, FloatingBubbleService::class.java).apply { action = ACTION_STOP }
        val stopPendingIntent = PendingIntent.getService(this, 1, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val openAppPendingIntent = PendingIntent.getActivity(this, 2, openAppIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Brawl Stars Pick Asistanı")
            .setContentText("Yüzen bubble devrede. Maçta bubble'a dokunarak analiz yapın.")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            .setContentIntent(openAppPendingIntent)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Kapat", stopPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        val foregroundType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION
        } else 0

        ServiceCompat.startForeground(this, NOTIFICATION_ID, notification, foregroundType)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Brawl Stars Pick Assistant",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Yüzen asistan ve ekran analiz servisi"
                setShowBadge(false)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initOverlayView() {
        if (!Settings.canDrawOverlays(this)) {
            Log.w(TAG, "Overlay permission not granted")
            stopSelf()
            return
        }

        overlayLifecycleOwner = OverlayLifecycleOwner().apply { onCreate() }

        layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 40
            y = 350
        }

        composeView = ComposeView(this).apply {
            overlayLifecycleOwner?.attachTo(this)

            setContent {
                FloatingOverlayRoot(
                    isScanning = isScanningState,
                    isExpanded = isExpandedState,
                    analysisResult = analysisResultState,
                    bubbleSize = currentSettingsState.bubbleSize,
                    bubbleAlpha = currentSettingsState.bubbleAlpha,
                    onBubbleClick = {
                        handleBubbleClick()
                    },
                    onBubbleLongClick = {
                        isExpandedState = !isExpandedState
                    },
                    onCloseExpanded = {
                        isExpandedState = false
                    },
                    onRescanClick = {
                        triggerAnalysis()
                    },
                    onOpenSettingsClick = {
                        val launchIntent = Intent(this@FloatingBubbleService, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        }
                        startActivity(launchIntent)
                    },
                    onCloseServiceClick = {
                        stopSelf()
                    }
                )
            }

            // Drag touch listener
            var initialX = 0
            var initialY = 0
            var initialTouchX = 0f
            var initialTouchY = 0f
            var isDragging = false

            setOnTouchListener { _, event ->
                val params = this@FloatingBubbleService.layoutParams ?: return@setOnTouchListener false
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params.x
                        initialY = params.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        isDragging = false
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = (event.rawX - initialTouchX).toInt()
                        val dy = (event.rawY - initialTouchY).toInt()
                        if (abs(dx) > 10 || abs(dy) > 10) {
                            isDragging = true
                            params.x = initialX + dx
                            params.y = initialY + dy
                            try {
                                windowManager.updateViewLayout(this, params)
                            } catch (e: Exception) {
                                Log.e(TAG, "Error updating window layout", e)
                            }
                        }
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        if (!isDragging) {
                            // Single tap
                            handleBubbleClick()
                        }
                        true
                    }
                    else -> false
                }
            }
        }

        try {
            windowManager.addView(composeView, layoutParams)
        } catch (e: Exception) {
            Log.e(TAG, "Error adding overlay view to WindowManager", e)
        }
    }

    private fun handleBubbleClick() {
        if (!isExpandedState) {
            triggerAnalysis()
        } else {
            isExpandedState = false
        }
    }

    private fun triggerAnalysis() {
        if (isScanningState) return

        isScanningState = true
        isExpandedState = true

        serviceScope.launch {
            try {
                val mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
                MediaProjectionHolder.initProjection(mediaProjectionManager)

                val capturedBitmap = MediaProjectionHolder.captureCurrentScreen()
                val result = if (capturedBitmap != null) {
                    ScreenAnalyzer.analyzeScreenshot(capturedBitmap)
                } else {
                    // Fallback to local heuristic test simulation if screenshot was empty
                    val dummyBitmap = android.graphics.Bitmap.createBitmap(1920, 1080, android.graphics.Bitmap.Config.ARGB_8888)
                    ScreenAnalyzer.analyzeScreenshot(dummyBitmap)
                }

                analysisResultState = result
            } catch (e: Exception) {
                Log.e(TAG, "Analysis failed", e)
            } finally {
                isScanningState = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        serviceScope.cancel()

        composeView?.let { view ->
            try {
                windowManager.removeView(view)
            } catch (e: Exception) {
                Log.e(TAG, "Error removing overlay view", e)
            }
        }
        overlayLifecycleOwner?.onDestroy()
        MediaProjectionHolder.release()
    }
}
