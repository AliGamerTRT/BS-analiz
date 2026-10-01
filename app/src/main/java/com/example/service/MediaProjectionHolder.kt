package com.example.service

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

object MediaProjectionHolder {
    private const val TAG = "MediaProjectionHolder"

    var resultCode: Int = 0
    var resultData: Intent? = null
    var screenWidth: Int = 1080
    var screenHeight: Int = 2400
    var screenDensity: Int = 420

    var mediaProjection: MediaProjection? = null

    fun isPermissionGranted(): Boolean {
        return resultCode != 0 && resultData != null
    }

    fun initProjection(manager: MediaProjectionManager): MediaProjection? {
        if (mediaProjection != null) return mediaProjection
        val data = resultData ?: return null
        return try {
            val projection = manager.getMediaProjection(resultCode, data)
            mediaProjection = projection
            projection
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing MediaProjection", e)
            null
        }
    }

    suspend fun captureCurrentScreen(): Bitmap? = suspendCancellableCoroutine { continuation ->
        val projection = mediaProjection
        if (projection == null) {
            continuation.resume(null)
            return@suspendCancellableCoroutine
        }

        val width = screenWidth.coerceAtLeast(480)
        val height = screenHeight.coerceAtLeast(800)
        val density = screenDensity.coerceAtLeast(160)

        val imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2)
        val handler = Handler(Looper.getMainLooper())
        var virtualDisplay: VirtualDisplay? = null

        var hasResumed = false

        imageReader.setOnImageAvailableListener({ reader ->
            val image = reader.acquireLatestImage()
            if (image != null && !hasResumed) {
                hasResumed = true
                try {
                    val planes = image.planes
                    val buffer = planes[0].buffer
                    val pixelStride = planes[0].pixelStride
                    val rowStride = planes[0].rowStride
                    val rowPadding = rowStride - pixelStride * width

                    val bitmap = Bitmap.createBitmap(
                        width + rowPadding / pixelStride,
                        height,
                        Bitmap.Config.ARGB_8888
                    )
                    bitmap.copyPixelsFromBuffer(buffer)
                    image.close()

                    // Crop to exact width and height
                    val cropped = if (rowPadding > 0) {
                        Bitmap.createBitmap(bitmap, 0, 0, width, height)
                    } else {
                        bitmap
                    }

                    virtualDisplay?.release()
                    imageReader.close()

                    continuation.resume(cropped)
                } catch (e: Exception) {
                    Log.e(TAG, "Error processing captured frame", e)
                    virtualDisplay?.release()
                    imageReader.close()
                    continuation.resume(null)
                }
            } else {
                image?.close()
            }
        }, handler)

        try {
            virtualDisplay = projection.createVirtualDisplay(
                "BrawlPickCapture",
                width,
                height,
                density,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                imageReader.surface,
                null,
                handler
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create VirtualDisplay", e)
            imageReader.close()
            if (!hasResumed) {
                hasResumed = true
                continuation.resume(null)
            }
        }

        continuation.invokeOnCancellation {
            virtualDisplay?.release()
            imageReader.close()
        }
    }

    fun release() {
        try {
            mediaProjection?.stop()
        } catch (e: Exception) {
            Log.w(TAG, "Error stopping mediaProjection", e)
        }
        mediaProjection = null
        resultCode = 0
        resultData = null
    }
}
