package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BrawlCyan,
    onPrimary = Color(0xFF00382E),
    primaryContainer = Color(0xFF005143),
    onPrimaryContainer = BrawlCyan,

    secondary = BrawlGold,
    onSecondary = Color(0xFF3F2E00),
    secondaryContainer = Color(0xFF5B4300),
    onSecondaryContainer = BrawlGold,

    tertiary = BrawlMagenta,
    onTertiary = Color.White,

    background = BrawlDarkBg,
    onBackground = Color(0xFFF5F3FF),

    surface = BrawlSurface,
    onSurface = Color(0xFFF5F3FF),
    surfaceVariant = BrawlSurfaceVariant,
    onSurfaceVariant = BrawlTextMuted,

    outline = BrawlCardBorder
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00897B),
    onPrimary = Color.White,
    secondary = Color(0xFFE65100),
    onSecondary = Color.White,
    tertiary = BrawlMagenta,
    background = BrawlLightBg,
    onBackground = Color(0xFF1E1838),
    surface = BrawlLightSurface,
    onSurface = Color(0xFF1E1838),
    surfaceVariant = BrawlLightSurfaceVariant,
    onSurfaceVariant = Color(0xFF5D537E)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to gaming dark theme
    dynamicColor: Boolean = false, // Keep gaming brand identity
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
