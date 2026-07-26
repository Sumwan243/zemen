package com.surafel.zemen.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.surafel.zemen.calendar.EthiopianCalendar
import com.surafel.zemen.calendar.GeezNumerals
import com.surafel.zemen.data.Holiday
import com.surafel.zemen.ui.theme.*

/**
 * Today Screen — the emotional core of the app.
 * Shows the current Ethiopian date as the focal point with week strip below.
 * Conditionally renders Pagume special mode when currentMonth == 13.
 */
@Composable
fun TodayScreen(
    isAmharic: Boolean,
    useGeez: Boolean,
    accentColor: androidx.compose.ui.graphics.Color,
    isDark: Boolean,
    nextFeast: Holiday?
) {
    val today = EthiopianCalendar.today()
    val isPagume = today.month == 13

    if (isPagume) {
        PagumeMode(
            today = today,
            isAmharic = isAmharic,
            useGeez = useGeez,
            accentColor = accentColor,
            isDark = isDark
        )
    } else {
        NormalMode(
            today = today,
            isAmharic = isAmharic,
            useGeez = useGeez,
            accentColor = accentColor,
            isDark = isDark,
            nextFeast = nextFeast
        )
    }
}

@Composable
private fun NormalMode(
    today: EthiopianCalendar.EthiopianDate,
    isAmharic: Boolean,
    useGeez: Boolean,
    accentColor: androidx.compose.ui.graphics.Color,
    isDark: Boolean,
    nextFeast: Holiday?
) {
    val gregorian = EthiopianCalendar.ethiopianToGregorian(today.year, today.month, today.day)
    val dayOfWeek = EthiopianCalendar.dayOfWeek(today.year, today.month, today.day)
    val monthName = EthiopianCalendar.getMonthName(today.month, isAmharic)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Day number (large, focal point)
        val dayText = if (useGeez) GeezNumerals.toGeez(today.day) else today.day.toString()
        Text(
            text = dayText,
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onBackground
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Month name (medium weight)
        Text(
            text = monthName,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Ethiopian year (small)
        val yearText = if (useGeez) GeezNumerals.toGeez(today.year) else today.year.toString()
        Text(
            text = yearText,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Gregorian date (low contrast)
        val gMonthNames = if (isAmharic) {
            arrayOf("", "\u1303\u1295\u1295\u122D", "\u1290\u1265\u1262\u122D", "\u121B\u130B\u122D", "\u12EB\u1275\u122D",
                "\u121B\u12CB", "\u1230\u1295\u1295", "\u1230\u1295\u1267", "\u1230\u1296\u1272", "\u1235\u1265\u1270\u121B\u1265",
                "\u1292\u1275\u127D\u121B\u1265", "\u129F\u121B\u1265\u121B\u1265")
        } else {
            arrayOf("", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        }
        Text(
            text = "${gregorian.day} ${gMonthNames[gregorian.month]} ${gregorian.year}",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.outline
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 7-day week strip
        WeekStrip(
            today = today,
            isAmharic = isAmharic,
            useGeez = useGeez,
            accentColor = accentColor,
            isDark = isDark
        )

        Spacer(modifier = Modifier.weight(1f))

        // Feast preview card
        nextFeast?.let { feast ->
            FeastPreviewCard(feast = feast, isAmharic = isAmharic)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun WeekStrip(
    today: EthiopianCalendar.EthiopianDate,
    isAmharic: Boolean,
    useGeez: Boolean,
    accentColor: androidx.compose.ui.graphics.Color,
    isDark: Boolean
) {
    val dayOfWeek = EthiopianCalendar.dayOfWeek(today.year, today.month, today.day)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0..6) {
            val offset = i - dayOfWeek
            val dayJdn = EthiopianCalendar.ethiopianToJdn(today.year, today.month, today.day) + offset
            val dayDate = EthiopianCalendar.jdnToEthiopian(dayJdn)
            val isToday = i == dayOfWeek

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(40.dp)
            ) {
                // Day number
                val dayStr = if (useGeez) GeezNumerals.toGeez(dayDate.day) else dayDate.day.toString()
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .then(
                            if (isToday) {
                                Modifier.background(
                                    color = if (isToday) accentColor else MaterialTheme.colorScheme.background,
                                    shape = androidx.compose.foundation.shape.CircleShape
                                )
                            } else {
                                Modifier
                            }
                        )
                ) {
                    Text(
                        text = dayStr,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                            color = if (isToday) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            }
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Day of week label
                Text(
                    text = EthiopianCalendar.getDayOfWeekName(i, isAmharic),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.outline
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun FeastPreviewCard(feast: Holiday, isAmharic: Boolean) {
    val name = if (isAmharic) feast.nameAm else feast.nameEn
    val desc = if (isAmharic) feast.shortDescAm else feast.shortDescEn

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = feast.emoji,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
