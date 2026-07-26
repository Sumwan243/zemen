package com.surafel.zemen.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.surafel.zemen.ui.theme.*

/**
 * Settings Screen — all app configuration.
 * Language, numerals, accent color, holiday tiers, theme mode.
 */
@Composable
fun SettingsScreen(
    isAmharic: Boolean,
    useGeez: Boolean,
    themeMode: String,
    accentColor: Color,
    orthodoxEnabled: Boolean,
    nationalEnabled: Boolean,
    islamicEnabled: Boolean,
    onLanguageChanged: (Boolean) -> Unit,
    onNumeralChanged: (Boolean) -> Unit,
    onThemeChanged: (String) -> Unit,
    onAccentChanged: (Color) -> Unit,
    onOrthodoxToggled: (Boolean) -> Unit,
    onNationalToggled: (Boolean) -> Unit,
    onIslamicToggled: (Boolean) -> Unit
) {
    val showWidgetConfig = remember { mutableStateOf(false) }

    if (showWidgetConfig.value) {
        WidgetConfiguratorScreen(
            isAmharic = isAmharic,
            onBack = { showWidgetConfig.value = false }
        )
        return@Column
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        // Section: Language
        SettingsSectionHeader(
            title = if (isAmharic) "\u1295\u121B\u1270 / Language" else "Language"
        )
        SettingsToggleRow(
            labelLeft = "English",
            labelRight = "\u12A0\u121B\u121B\u1295\u129F",
            isRightSelected = isAmharic,
            accentColor = accentColor
        ) { onLanguageChanged(!isAmharic) }

        Spacer(modifier = Modifier.height(24.dp))

        // Section: Numerals
        SettingsSectionHeader(
            title = if (isAmharic) "\u1233\u1260\u1275\u1272 / Numbers" else "Numerals"
        )
        SettingsToggleRow(
            labelLeft = "1 2 3",
            labelRight = "\u1369 \u136A \u136B",
            isRightSelected = useGeez,
            accentColor = accentColor
        ) { onNumeralChanged(!useGeez) }

        Spacer(modifier = Modifier.height(24.dp))

        // Section: Theme
        SettingsSectionHeader(
            title = if (isAmharic) "\u12B3\u121B\u120C\u129B / Theme" else "Theme"
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ThemeChip(
                label = if (isAmharic) "\u12B3\u1208\u12CD" else "Dark",
                isSelected = themeMode == "dark",
                accentColor = accentColor
            ) { onThemeChanged("dark") }

            ThemeChip(
                label = if (isAmharic) "\u1290\u121B\u1273" else "Light",
                isSelected = themeMode == "light",
                accentColor = accentColor
            ) { onThemeChanged("light") }

            ThemeChip(
                label = if (isAmharic) "\u1235\u12CB\u1273\u1275\u121B" else "System",
                isSelected = themeMode == "system",
                accentColor = accentColor
            ) { onThemeChanged("system") }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section: Accent Color
        SettingsSectionHeader(
            title = if (isAmharic) "\u12A0\u1235\u121B\u1270 \u1295\u121B\u1270" else "Accent Color"
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ACCENT_COLORS.forEach { (color, name) ->
                val isSelected = accentColor == color
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(color)
                        .then(
                            if (isSelected) Modifier.border(
                                width = 3.dp,
                                color = MaterialTheme.colorScheme.onBackground,
                                shape = CircleShape
                            ) else Modifier
                        )
                        .clickable { onAccentChanged(color) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section: Holiday Tiers
        SettingsSectionHeader(
            title = if (isAmharic) "\u1290\u121B\u1270\u127D\u1295 \u1323\u121B\u1272" else "Holiday Tiers"
        )
        SettingsSwitchItem(
            label = if (isAmharic) "\u12A0\u1265\u1295\u121B \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 (\u26AB)" else "Orthodox (\u26AB)",
            enabled = orthodoxEnabled,
            color = EthiopianGold
        ) { onOrthodoxToggled(!orthodoxEnabled) }

        SettingsSwitchItem(
            label = if (isAmharic) "\u1290\u121B\u1270\u127D\u1295 (\uD83D\uDD35)" else "National (\uD83D\uDD35)",
            enabled = nationalEnabled,
            color = HolidayBlue
        ) { onNationalToggled(!nationalEnabled) }

        SettingsSwitchItem(
            label = if (isAmharic) "\u12AB\u123B\u121B\u121B\u1270 (\uD83D\uDFE2)" else "Islamic (\uD83D\uDFE2)",
            enabled = islamicEnabled,
            color = HolidayGreen
        ) { onIslamicToggled(!islamicEnabled) }

        Spacer(modifier = Modifier.height(24.dp))

        // Widget Configurator button
        SettingsSectionHeader(
            title = if (isAmharic) "\u12CB\u12B3\u1275\u1270" else "Widget"
        )
        Card(
            onClick = { showWidgetConfig.value = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isAmharic) "\u12CB\u12B3\u1275\u1270 \u121B\u1233\u1240\u12CD\u129B" else "Widget Configurator",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                androidx.compose.material3.Icon(
                    imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.outline
        ),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun SettingsToggleRow(
    labelLeft: String,
    labelRight: String,
    isRightSelected: Boolean,
    accentColor: Color,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = labelLeft,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (!isRightSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (!isRightSelected) accentColor else MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Text(
                text = labelRight,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isRightSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isRightSelected) accentColor else MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
private fun ThemeChip(
    label: String,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) accentColor else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                       else MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
private fun SettingsSwitchItem(
    label: String,
    enabled: Boolean,
    color: Color,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
        Switch(
            checked = enabled,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedTrackColor = color
            )
        )
    }
}
