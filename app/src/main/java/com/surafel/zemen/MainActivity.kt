package com.surafel.zemen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.surafel.zemen.calendar.EthiopianCalendar
import com.surafel.zemen.data.HolidayRepository
import com.surafel.zemen.data.SettingsDataStore
import com.surafel.zemen.ui.navigation.ZemanNavigation
import com.surafel.zemen.ui.navigation.ZemenTab
import com.surafel.zemen.ui.theme.EthiopianGold
import com.surafel.zemen.ui.theme.ZemenTheme
import com.surafel.zemen.ui.theme.ThemeMode

class MainActivity : ComponentActivity() {

    private lateinit var settingsDataStore: SettingsDataStore
    private val holidayRepository = HolidayRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        settingsDataStore = SettingsDataStore(applicationContext)

        setContent {
            val isAmharic by settingsDataStore.isAmharic.collectAsState(initial = false)
            val useGeez by settingsDataStore.useGeezNumerals.collectAsState(initial = false)
            val accentColor by settingsDataStore.accentColor.collectAsState(initial = EthiopianGold)
            val themeMode by settingsDataStore.themeMode.collectAsState(initial = "dark")
            val orthodoxEnabled by settingsDataStore.orthodoxEnabled.collectAsState(initial = true)
            val nationalEnabled by settingsDataStore.nationalEnabled.collectAsState(initial = true)
            val islamicEnabled by settingsDataStore.islamicEnabled.collectAsState(initial = true)

            val selectedTab = remember { mutableStateOf(ZemenTab.TODAY) }

            val resolvedThemeMode = when (themeMode) {
                "light" -> ThemeMode.LIGHT
                "system" -> ThemeMode.SYSTEM
                else -> ThemeMode.DARK
            }

            val isDark = when (resolvedThemeMode) {
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            val today = EthiopianCalendar.today()
            val nextFeast = holidayRepository.getNextFeast(
                today.year, today.month, today.day
            )

            ZemenTheme(
                themeMode = resolvedThemeMode,
                accentColor = accentColor
            ) {
                ZemanNavigation(
                    selectedTab = selectedTab.value,
                    onTabSelected = { selectedTab.value = it },
                    isAmharic = isAmharic,
                    useGeez = useGeez,
                    accentColor = accentColor,
                    themeMode = themeMode,
                    isDark = isDark,
                    orthodoxEnabled = orthodoxEnabled,
                    nationalEnabled = nationalEnabled,
                    islamicEnabled = islamicEnabled,
                    onLanguageChanged = { settingsDataStore.setLanguage(it) },
                    onNumeralChanged = { settingsDataStore.setNumeralSystem(it) },
                    onThemeChanged = { settingsDataStore.setThemeMode(it) },
                    onAccentChanged = { settingsDataStore.setAccentColor(it) },
                    onOrthodoxToggled = { settingsDataStore.setOrthodoxEnabled(it) },
                    onNationalToggled = { settingsDataStore.setNationalEnabled(it) },
                    onIslamicToggled = { settingsDataStore.setIslamicEnabled(it) },
                    nextFeast = nextFeast
                )
            }
        }
    }
}
