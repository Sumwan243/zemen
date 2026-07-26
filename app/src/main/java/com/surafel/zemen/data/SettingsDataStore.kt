package com.surafel.zemen.data

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.surafel.zemen.ui.theme.accentColorToHex
import com.surafel.zemen.ui.theme.resolveAccentColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "zemen_settings")

object SettingsKeys {
    val LANGUAGE = stringPreferencesKey("language")               // "en" or "am"
    val NUMERAL_SYSTEM = stringPreferencesKey("numeral_system")  // "western" or "geez"
    val ACCENT_COLOR = stringPreferencesKey("accent_color")      // hex string
    val THEME_MODE = stringPreferencesKey("theme_mode")          // "dark", "light", "system"
    val ORTHODOX_ENABLED = booleanPreferencesKey("orthodox_enabled")
    val NATIONAL_ENABLED = booleanPreferencesKey("national_enabled")
    val ISLAMIC_ENABLED = booleanPreferencesKey("islamic_enabled")
}

class SettingsDataStore(private val context: Context) {

    val isAmharic: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[SettingsKeys.LANGUAGE] == "am"
    }

    val useGeezNumerals: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[SettingsKeys.NUMERAL_SYSTEM] == "geez"
    }

    val accentColor: Flow<Color> = context.dataStore.data.map { prefs ->
        val hex = prefs[SettingsKeys.ACCENT_COLOR] ?: "#D4AF37"
        resolveAccentColor(hex)
    }

    val themeMode: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[SettingsKeys.THEME_MODE] ?: "dark"
    }

    val orthodoxEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[SettingsKeys.ORTHODOX_ENABLED] ?: true
    }

    val nationalEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[SettingsKeys.NATIONAL_ENABLED] ?: true
    }

    val islamicEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[SettingsKeys.ISLAMIC_ENABLED] ?: true
    }

    suspend fun setLanguage(isAmharic: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[SettingsKeys.LANGUAGE] = if (isAmharic) "am" else "en"
        }
    }

    suspend fun setNumeralSystem(useGeez: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[SettingsKeys.NUMERAL_SYSTEM] = if (useGeez) "geez" else "western"
        }
    }

    suspend fun setAccentColor(color: Color) {
        context.dataStore.edit { prefs ->
            prefs[SettingsKeys.ACCENT_COLOR] = accentColorToHex(color)
        }
    }

    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { prefs ->
            prefs[SettingsKeys.THEME_MODE] = mode
        }
    }

    suspend fun setOrthodoxEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[SettingsKeys.ORTHODOX_ENABLED] = enabled
        }
    }

    suspend fun setNationalEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[SettingsKeys.NATIONAL_ENABLED] = enabled
        }
    }

    suspend fun setIslamicEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[SettingsKeys.ISLAMIC_ENABLED] = enabled
        }
    }
}
