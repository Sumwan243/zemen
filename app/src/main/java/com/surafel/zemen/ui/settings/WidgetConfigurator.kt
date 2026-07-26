package com.surafel.zemen.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
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
 * Widget Configurator — sub-screen in Settings.
 * Size selector, toggles for season emoji, feast countdown, Geez numerals, widget style.
 */
@Composable
fun WidgetConfiguratorScreen(
    isAmharic: Boolean,
    onBack: () -> Unit
) {
    var selectedSize by remember { mutableIntStateOf(0) } // 0=2x1, 1=2x2, 2=4x2
    var showSeasonEmoji by remember { mutableStateOf(true) }
    var showFeastCountdown by remember { mutableStateOf(true) }
    var widgetGeezNumerals by remember { mutableStateOf(false) }
    var widgetStyle by remember { mutableIntStateOf(0) } // 0=Dark, 1=Light, 2=Gold accent

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Top bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = if (isAmharic) "\u1293\u1235\u1295" else "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isAmharic) "\u12CB\u12B3\u1275\u1270 \u121B\u1233\u1240\u12CD\u129B" else "Widget Configurator",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section: Widget Size
        SettingsSectionHeader(
            title = if (isAmharic) "\u1235\u121B \u12A0\u12CB\u121B" else "Widget Size"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WidgetSizeOption(
                label = if (isAmharic) "2×1" else "2×1",
                description = if (isAmharic) "\u134D\u1295\u121B" else "Strip",
                isSelected = selectedSize == 0,
                onClick = { selectedSize = 0 },
                modifier = Modifier.weight(1f)
            )
            WidgetSizeOption(
                label = if (isAmharic) "2×2" else "2×2",
                description = if (isAmharic) "\u1270\u121D\u1293\u12CD\u129B" else "Standard",
                isSelected = selectedSize == 1,
                onClick = { selectedSize = 1 },
                modifier = Modifier.weight(1f)
            )
            WidgetSizeOption(
                label = if (isAmharic) "4×2" else "4×2",
                description = if (isAmharic) "\u121B\u1235\u121D" else "Full",
                isSelected = selectedSize == 2,
                onClick = { selectedSize = 2 },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section: Widget Toggles
        SettingsSectionHeader(
            title = if (isAmharic) "\u12CB\u12B3\u1275\u1270 \u121B\u1233\u1240\u12CD\u129B" else "Widget Options"
        )

        SettingsSwitchItem(
            label = if (isAmharic) "\u1290\u121B\u1270 \u12A0\u1235\u121B\u120C\u1295 (\uD83C\uDF27\uFE0F)" else "Show Season Emoji (\uD83C\uDF27\uFE0F)",
            enabled = showSeasonEmoji,
            color = EthiopianGold
        ) { showSeasonEmoji = !showSeasonEmoji }

        SettingsSwitchItem(
            label = if (isAmharic) "\u1290\u121B\u1270 \u1289\u12F3\u1270\u12CB (\uD83D\uDCC5)" else "Show Feast Countdown (\uD83D\uDCC5)",
            enabled = showFeastCountdown,
            color = EthiopianGold
        ) { showFeastCountdown = !showFeastCountdown }

        SettingsSwitchItem(
            label = if (isAmharic) "\u1290\u121B\u1270 \u1233\u1265\u1272 \u134D\u1369\u136A\u136B" else "Geez Numerals on Widget (\u134D\u1369\u136A\u136B)",
            enabled = widgetGeezNumerals,
            color = EthiopianGold
        ) { widgetGeezNumerals = !widgetGeezNumerals }

        Spacer(modifier = Modifier.height(24.dp))

        // Section: Widget Style
        SettingsSectionHeader(
            title = if (isAmharic) "\u12CB\u12B3\u1275\u1270 \u12B3\u121B\u120C\u129B" else "Widget Style"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ThemeChip(
                label = if (isAmharic) "\u12B3\u1208\u12CD" else "Dark",
                isSelected = widgetStyle == 0,
                accentColor = EthiopianGold
            ) { widgetStyle = 0 }

            ThemeChip(
                label = if (isAmharic) "\u1290\u121B\u1273" else "Light",
                isSelected = widgetStyle == 1,
                accentColor = EthiopianGold
            ) { widgetStyle = 1 }

            ThemeChip(
                label = if (isAmharic) "\u1290\u121B\u1273 \u121B\u1235\u121D" else "Gold",
                isSelected = widgetStyle == 2,
                accentColor = EthiopianGold
            ) { widgetStyle = 2 }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Preview box
        Text(
            text = if (isAmharic) "\u1290\u121B\u1270 \u1295\u120B\u121B\u12CB" else "Widget Preview",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        WidgetPreviewCard(
            size = selectedSize,
            style = widgetStyle,
            accentColor = EthiopianGold,
            isAmharic = isAmharic
        )
    }
}

@Composable
private fun WidgetSizeOption(
    label: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                             else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                           else MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                           else MaterialTheme.colorScheme.outline
                )
            )
        }
    }
}

@Composable
private fun WidgetPreviewCard(
    size: Int,
    style: Int,
    accentColor: Color,
    isAmharic: Boolean
) {
    val bgColor = when (style) {
        0 -> Color(0xFF0A0A0A)
        1 -> Color(0xFFFFFFFF)
        else -> Color(0xFF0A0A0A)
    }

    val textColor = when (style) {
        0 -> Color.White
        1 -> Color.Black
        else -> Color.White
    }

    val heightDp = when (size) {
        0 -> 60.dp  // 2x1 strip
        1 -> 120.dp // 2x2 standard
        else -> 120.dp // 4x2 full
    }

    val widthDp = when (size) {
        0 -> 120.dp
        1 -> 120.dp
        else -> 260.dp
    }

    Card(
        modifier = Modifier.size(width = widthDp, height = heightDp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "\uD83C\uDF27\uFE0F",
                fontSize = 14.sp
            )
            Text(
                text = "Meskerem",
                style = MaterialTheme.typography.bodySmall.copy(color = textColor.copy(alpha = 0.6f))
            )
            Text(
                text = "12",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            )
            if (size >= 1) {
                Text(
                    text = "11/9/2025",
                    style = MaterialTheme.typography.labelSmall.copy(color = textColor.copy(alpha = 0.4f))
                )
            }
        }
    }
}
