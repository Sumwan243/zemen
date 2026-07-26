package com.surafel.zemen.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.surafel.zemen.ui.calendar.CalendarScreen
import com.surafel.zemen.ui.converter.ConverterScreen
import com.surafel.zemen.ui.feasts.FeastsScreen
import com.surafel.zemen.ui.home.TodayScreen
import com.surafel.zemen.ui.settings.SettingsScreen

/**
 * Bottom tab navigation with four tabs:
 * Today, Calendar, Feasts & Seasons, Settings
 */
enum class ZemenTab(
    val labelEn: String,
    val labelAm: String,
    val icon: ImageVector
) {
    TODAY("Today", "\u12A0\u1295\u121B", Icons.Default.Home),
    CALENDAR("Calendar", "\u1295\u121B\u12B3\u12CB\u1270", Icons.Default.CalendarMonth),
    FEASTS("Feasts", "\u1290\u121B\u1270\u127D\u1295", Icons.Default.Spa),
    SETTINGS("Settings", "\u1235\u12B3\u1274\u12CD\u129B", Icons.Default.Settings)
}

@Composable
fun ZemanNavigation(
    selectedTab: ZemenTab,
    onTabSelected: (ZemenTab) -> Unit,
    isAmharic: Boolean,
    useGeez: Boolean,
    accentColor: Color,
    themeMode: String,
    isDark: Boolean,
    orthodoxEnabled: Boolean,
    nationalEnabled: Boolean,
    islamicEnabled: Boolean,
    onLanguageChanged: (Boolean) -> Unit,
    onNumeralChanged: (Boolean) -> Unit,
    onThemeChanged: (String) -> Unit,
    onAccentChanged: (Color) -> Unit,
    onOrthodoxToggled: (Boolean) -> Unit,
    onNationalToggled: (Boolean) -> Unit,
    onIslamicToggled: (Boolean) -> Unit,
    nextFeast: com.surafel.zemen.data.Holiday?
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        Column(modifier = Modifier.fillMaxSize()) {
            when (selectedTab) {
                ZemenTab.TODAY -> {
                    TodayScreen(
                        isAmharic = isAmharic,
                        useGeez = useGeez,
                        accentColor = accentColor,
                        isDark = isDark,
                        nextFeast = nextFeast
                    )
                }
                ZemenTab.CALENDAR -> {
                    CalendarScreen(
                        isAmharic = isAmharic,
                        useGeez = useGeez,
                        accentColor = accentColor,
                        isDark = isDark
                    )
                }
                ZemenTab.FEASTS -> {
                    FeastsScreen(
                        isAmharic = isAmharic,
                        useGeez = useGeez,
                        accentColor = accentColor,
                        orthodoxEnabled = orthodoxEnabled,
                        nationalEnabled = nationalEnabled,
                        islamicEnabled = islamicEnabled
                    )
                }
                ZemenTab.SETTINGS -> {
                    SettingsScreen(
                        isAmharic = isAmharic,
                        useGeez = useGeez,
                        themeMode = themeMode,
                        accentColor = accentColor,
                        orthodoxEnabled = orthodoxEnabled,
                        nationalEnabled = nationalEnabled,
                        islamicEnabled = islamicEnabled,
                        onLanguageChanged = onLanguageChanged,
                        onNumeralChanged = onNumeralChanged,
                        onThemeChanged = onThemeChanged,
                        onAccentChanged = onAccentChanged,
                        onOrthodoxToggled = onOrthodoxToggled,
                        onNationalToggled = onNationalToggled,
                        onIslamicToggled = onIslamicToggled
                    )
                }
            }
        }

        // Bottom tab bar
        BottomTabBar(
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            isAmharic = isAmharic,
            accentColor = accentColor
        )
    }
}

@Composable
private fun BottomTabBar(
    selectedTab: ZemenTab,
    onTabSelected: (ZemenTab) -> Unit,
    isAmharic: Boolean,
    accentColor: Color
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        ZemenTab.entries.forEach { tab ->
            val isSelected = selectedTab == tab
            val label = if (isAmharic) tab.labelAm else tab.labelEn

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = label,
                        tint = if (isSelected) accentColor
                               else MaterialTheme.colorScheme.outline
                    )
                },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) accentColor
                               else MaterialTheme.colorScheme.outline
                    )
                },
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = accentColor.copy(alpha = 0.12f)
                )
            )
        }
    }
}
