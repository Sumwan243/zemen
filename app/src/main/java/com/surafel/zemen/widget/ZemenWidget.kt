package com.surafel.zemen.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.glance.*
import androidx.glance.action.clickable
import androidx.glance.appwidget.*
import androidx.glance.layout.*
import androidx.glance.text.*
import androidx.glance.action.startActivity
import com.surafel.zemen.MainActivity
import com.surafel.zemen.calendar.EthiopianCalendar
import com.surafel.zemen.calendar.GeezNumerals
import com.surafel.zemen.data.HolidayRepository
import com.surafel.zemen.data.HolidayTier
import com.surafel.zemen.data.SettingsKeys
import com.surafel.zemen.ui.theme.*

private val Context.widgetDataStore: DataStore<Preferences> by preferencesDataStore(name = "zemen_settings")

/** Widget size variants */
enum class WidgetSize(val widthDp: Dp, val heightDp: Dp) {
    STRIP_2X1(110.dp, 40.dp),    // 2x1 — date only
    STANDARD_2X2(110.dp, 110.dp), // 2x2 — full date + GC + week
    FULL_4X2(250.dp, 110.dp)     // 4x2 — all info + feast countdown
}

/**
 * Zemen Home Screen Widget — Glance-based.
 * 3 sizes: 2x1 Strip, 2x2 Standard, 4x2 Full.
 * Reads settings from DataStore (accent, numerals, language).
 */
class ZemenWidget : GlanceAppWidget() {

    @Composable
    override fun Content() {
        val prefs = currentState<Preferences>()
        val isAmharic = prefs[SettingsKeys.LANGUAGE] == "am"
        val useGeez = prefs[SettingsKeys.NUMERAL_SYSTEM] == "geez"
        val accentHex = prefs[SettingsKeys.ACCENT_COLOR] ?: "#D4AF37"

        val today = EthiopianCalendar.today()
        val gregorian = EthiopianCalendar.ethiopianToGregorian(today.year, today.month, today.day)
        val season = EthiopianCalendar.getSeason(today.month)
        val monthName = EthiopianCalendar.getMonthName(today.month, isAmharic)
        val dayText = if (useGeez) GeezNumerals.toGeez(today.day) else today.day.toString()
        val yearText = if (useGeez) GeezNumerals.toGeez(today.year) else today.year.toString()
        val accentColor = resolveAccentColor(accentHex)

        // Find next feast
        val repo = HolidayRepository()
        val nextFeast = repo.getNextFeast(today.year, today.month, today.day)
        val feastName = if (nextFeast != null) {
            if (isAmharic) nextFeast.nameAm else nextFeast.nameEn
        } else null
        val feastEmoji = nextFeast?.emoji ?: ""

        // Determine size based on widget dimensions
        val size = LocalSize.current
        val widgetSize = when {
            size.width >= 220.dp -> WidgetSize.FULL_4X2
            size.height >= 80.dp -> WidgetSize.STANDARD_2X2
            else -> WidgetSize.STRIP_2X1
        }

        // Render based on size
        when (widgetSize) {
            WidgetSize.STRIP_2X1 -> StripWidget(
                monthName = monthName,
                dayText = dayText,
                accentColor = accentColor,
                seasonEmoji = season?.emoji ?: ""
            )
            WidgetSize.STANDARD_2X2 -> StandardWidget(
                monthName = monthName,
                dayText = dayText,
                yearText = yearText,
                gregorian = "${gregorian.day}/${gregorian.month}/${gregorian.year}",
                accentColor = accentColor,
                seasonEmoji = season?.emoji ?: "",
                weekProgress = EthiopianCalendar.dayOfWeek(today.year, today.month, today.day)
            )
            WidgetSize.FULL_4X2 -> FullWidget(
                monthName = monthName,
                dayText = dayText,
                yearText = yearText,
                gregorian = "${gregorian.day}/${gregorian.month}/${gregorian.year}",
                accentColor = accentColor,
                seasonEmoji = season?.emoji ?: "",
                seasonName = if (season != null) {
                    if (isAmharic) season.nameAm else season.nameEn
                } else null,
                feastEmoji = feastEmoji,
                feastName = feastName,
                weekProgress = EthiopianCalendar.dayOfWeek(today.year, today.month, today.day),
                dayOfMonth = today.day,
                daysInMonth = EthiopianCalendar.daysInMonth(today.year, today.month)
            )
        }
    }
}

/**
 * 2x1 Strip — minimal. Month + day number. Season emoji optional.
 */
@Composable
private fun StripWidget(
    monthName: String,
    dayText: String,
    accentColor: Color,
    seasonEmoji: String
) {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
            .clickable(actionStartActivity<MainActivity>())
            .padding(8.dp)
    ) {
        Row(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (seasonEmoji.isNotEmpty()) {
                Text(
                    text = seasonEmoji,
                    style = TextStyle(fontSize = 16.sp)
                )
                Spacer(modifier = GlanceModifier.width(6.dp))
            }
            Text(
                text = monthName,
                style = TextStyle(
                    color = ColorProvider(Color(0xFFAAAAAA)),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = GlanceModifier.width(6.dp))
            Text(
                text = dayText,
                style = TextStyle(
                    color = ColorProvider(accentColor),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

/**
 * 2x2 Standard — full Ethiopian date, Gregorian below, week progress dots, season emoji.
 */
@Composable
private fun StandardWidget(
    monthName: String,
    dayText: String,
    yearText: String,
    gregorian: String,
    accentColor: Color,
    seasonEmoji: String,
    weekProgress: Int
) {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
            .clickable(actionStartActivity<MainActivity>())
            .padding(12.dp)
    ) {
        Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Season emoji
            if (seasonEmoji.isNotEmpty()) {
                Text(text = seasonEmoji, style = TextStyle(fontSize = 18.sp))
                Spacer(modifier = GlanceModifier.height(2.dp))
            }

            // Month + Day
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = dayText,
                    style = TextStyle(
                        color = ColorProvider(accentColor),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = GlanceModifier.width(8.dp))
                Column {
                    Text(
                        text = monthName,
                        style = TextStyle(
                            color = ColorProvider(Color.White),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = yearText,
                        style = TextStyle(
                            color = ColorProvider(Color(0xFFAAAAAA)),
                            fontSize = 12.sp
                        )
                    )
                }
            }

            Spacer(modifier = GlanceModifier.height(4.dp))

            // Week progress dots
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (i in 0..6) {
                    val dotColor = if (i == weekProgress) {
                        ColorProvider(accentColor)
                    } else {
                        ColorProvider(Color(0xFF333333))
                    }
                    Box(
                        modifier = GlanceModifier
                            .size(6.dp)
                            .background(dotColor)
                            .cornerRadius(3.dp)
                    )
                    if (i < 6) Spacer(modifier = GlanceModifier.width(4.dp))
                }
            }

            Spacer(modifier = GlanceModifier.height(4.dp))

            // Gregorian date
            Text(
                text = gregorian,
                style = TextStyle(
                    color = ColorProvider(Color(0xFF555555)),
                    fontSize = 11.sp
                )
            )
        }
    }
}

/**
 * 4x2 Full — everything above plus feast countdown, month progress bar, season name.
 */
@Composable
private fun FullWidget(
    monthName: String,
    dayText: String,
    yearText: String,
    gregorian: String,
    accentColor: Color,
    seasonEmoji: String,
    seasonName: String?,
    feastEmoji: String,
    feastName: String?,
    weekProgress: Int,
    dayOfMonth: Int,
    daysInMonth: Int
) {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
            .clickable(actionStartActivity<MainActivity>())
            .padding(12.dp)
    ) {
        Row(modifier = GlanceModifier.fillMaxSize()) {
            // Left side: date
            Column(
                modifier = GlanceModifier.defaultWeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (seasonEmoji.isNotEmpty()) {
                    Text(text = seasonEmoji, style = TextStyle(fontSize = 16.sp))
                    Spacer(modifier = GlanceModifier.height(2.dp))
                }

                Text(
                    text = monthName,
                    style = TextStyle(
                        color = ColorProvider(Color(0xFFAAAAAA)),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = dayText,
                    style = TextStyle(
                        color = ColorProvider(accentColor),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "$yearText  \u2022  $gregorian",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF555555)),
                        fontSize = 10.sp
                    )
                )

                Spacer(modifier = GlanceModifier.height(4.dp))

                // Week dots
                Row {
                    for (i in 0..6) {
                        val dotColor = if (i == weekProgress) {
                            ColorProvider(accentColor)
                        } else {
                            ColorProvider(Color(0xFF333333))
                        }
                        Box(
                            modifier = GlanceModifier
                                .size(5.dp)
                                .background(dotColor)
                                .cornerRadius(2.dp)
                        )
                        if (i < 6) Spacer(modifier = GlanceModifier.width(3.dp))
                    }
                }
            }

            Spacer(modifier = GlanceModifier.width(12.dp))

            // Right side: feast countdown + month progress + season
            Column(
                modifier = GlanceModifier.defaultWeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.Horizontal.End
            ) {
                // Season name
                seasonName?.let {
                    Text(
                        text = it,
                        style = TextStyle(
                            color = ColorProvider(Color(0xFFAAAAAA)),
                            fontSize = 12.sp
                        )
                    )
                }

                Spacer(modifier = GlanceModifier.height(8.dp))

                // Feast countdown
                if (feastName != null) {
                    Text(
                        text = feastEmoji,
                        style = TextStyle(fontSize = 18.sp)
                    )
                    Text(
                        text = feastName,
                        style = TextStyle(
                            color = ColorProvider(Color.White),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Spacer(modifier = GlanceModifier.height(8.dp))

                // Month progress bar
                Text(
                    text = "Day $dayOfMonth/$daysInMonth",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF555555)),
                        fontSize = 10.sp
                    )
                )
                // Simple progress bar using a box
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(Color(0xFF333333))
                        .cornerRadius(2.dp)
                ) {
                    val progress = dayOfMonth.toFloat() / daysInMonth
                    Box(
                        modifier = GlanceModifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .defaultWeight()
                            .background(accentColor)
                            .cornerRadius(2.dp)
                    )
                }
            }
        }
    }
}

class ZemenWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ZemenWidget()
}
