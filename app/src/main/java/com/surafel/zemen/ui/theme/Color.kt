package com.surafel.zemen.ui.theme

import androidx.compose.ui.graphics.Color

// ─── Design Spec Colors ─────────────────────────────────────────────────────

/** Ethiopian Gold — primary accent */
val EthiopianGold = Color(0xFFD4AF37)

/** National holiday dot */
val HolidayBlue = Color(0xFF378ADD)

/** Islamic holiday dot */
val HolidayGreen = Color(0xFF2D9B5A)

// ─── Dark Theme ─────────────────────────────────────────────────────────────

val DarkBackground = Color(0xFF0A0A0A)
val DarkSurface = Color(0xFF0D0D0D)
val DarkSurfaceVariant = Color(0xFF1A1A1A)
val DarkTextPrimary = Color(0xFFFFFFFF)
val DarkTextSecondary = Color(0xFFAAAAAA)
val DarkTextTertiary = Color(0xFF555555)

// ─── Light Theme ────────────────────────────────────────────────────────────

val LightBackground = Color(0xFFFFFFFF)
val LightSurface = Color(0xFFF5F5F5)
val LightSurfaceVariant = Color(0xFFEEEEEE)
val LightTextPrimary = Color(0xFF000000)
val LightTextSecondary = Color(0xFF666666)
val LightTextTertiary = Color(0xFFAAAAAA)

// ─── Pagume Special ────────────────────────────────────────────────────────

val PagumeBackgroundStart = Color(0xFF0D0A12)
val PagumeBackgroundEnd = Color(0xFF1A1228)
val PagumeAccent = Color(0xFFC4B8E8)

// ─── Accent Color Palette ──────────────────────────────────────────────────

val AccentTerracotta = Color(0xFFC86B4D)
val AccentSkyBlue = Color(0xFF4F9CF9)
val AccentSoftPurple = Color(0xFF7C6FCD)
val AccentTeal = Color(0xFF2DD4BF)
val AccentForestGreen = Color(0xFF3D8B4E)

/** All available accent colors for Settings picker */
val ACCENT_COLORS = listOf(
    EthiopianGold to "Gold",
    AccentTerracotta to "Terracotta",
    AccentSkyBlue to "Sky Blue",
    AccentSoftPurple to "Soft Purple",
    AccentTeal to "Teal",
    AccentForestGreen to "Forest Green"
)

/**
 * Resolve an accent color by its hex string key.
 * Returns EthiopianGold if the key is not recognized.
 */
fun resolveAccentColor(hex: String): Color {
    return when (hex) {
        "#C86B4D" -> AccentTerracotta
        "#4F9CF9" -> AccentSkyBlue
        "#7C6FCD" -> AccentSoftPurple
        "#2DD4BF" -> AccentTeal
        "#3D8B4E" -> AccentForestGreen
        else -> EthiopianGold
    }
}

/** Get hex string for an accent color */
fun accentColorToHex(color: Color): String {
    return when (color) {
        AccentTerracotta -> "#C86B4D"
        AccentSkyBlue -> "#4F9CF9"
        AccentSoftPurple -> "#7C6FCD"
        AccentTeal -> "#2DD4BF"
        AccentForestGreen -> "#3D8B4E"
        else -> "#D4AF37"
    }
}
