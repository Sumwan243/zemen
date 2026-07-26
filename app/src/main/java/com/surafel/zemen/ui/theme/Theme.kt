package com.surafel.zemen.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Zemen theme configuration.
 * Supports dark, light, and system-default modes.
 * Accent color is applied at root level so all composables inherit it.
 */

private fun darkScheme(accent: androidx.compose.ui.graphics.Color) = darkColorScheme(
    primary = accent,
    onPrimary = DarkTextPrimary,
    secondary = DarkSurfaceVariant,
    onSecondary = DarkTextPrimary,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkTextTertiary,
    outlineVariant = DarkSurfaceVariant
)

private fun lightScheme(accent: androidx.compose.ui.graphics.Color) = lightColorScheme(
    primary = accent,
    onPrimary = LightTextPrimary,
    secondary = LightSurfaceVariant,
    onSecondary = LightTextPrimary,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,
    outline = LightTextTertiary,
    outlineVariant = LightSurfaceVariant
)

/**
 * Theme enum matching the Settings toggle
 */
enum class ThemeMode { DARK, LIGHT, SYSTEM }

@Composable
fun ZemenTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    accentColor: androidx.compose.ui.graphics.Color = EthiopianGold,
    content: @Composable () -> Unit
) {
    val useDark = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = if (useDark) darkScheme(accentColor) else lightScheme(accentColor)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !useDark
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ZemenTypography,
        content = content
    )
}
