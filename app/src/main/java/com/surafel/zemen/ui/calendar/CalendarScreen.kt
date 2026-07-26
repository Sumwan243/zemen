package com.surafel.zemen.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.surafel.zemen.calendar.*

/**
 * Calendar Grid Screen — full month view.
 * 7-column grid, today highlighted, selected state, feast dots.
 */
@Composable
fun CalendarScreen(
    isAmharic: Boolean,
    useGeez: Boolean,
    accentColor: Color,
    isDark: Boolean
) {
    val today = EthiopianCalendar.today()
    var selectedMonth by remember { mutableIntStateOf(today.month) }
    var selectedYear by remember { mutableIntStateOf(today.year) }
    var selectedDay by remember { mutableIntStateOf(today.day) }
    var localGeez by remember { mutableStateOf(useGeez) }

    val monthName = EthiopianCalendar.getMonthName(selectedMonth, isAmharic)

    // Calculate Gregorian month range for the selected Ethiopian month
    val firstOfEtMonth = EthiopianCalendar.ethiopianToGregorian(selectedYear, selectedMonth, 1)
    val lastDay = EthiopianCalendar.daysInMonth(selectedYear, selectedMonth)
    val lastOfEtMonth = EthiopianCalendar.ethiopianToGregorian(selectedYear, selectedMonth, lastDay)

    val gMonthNames = if (isAmharic) {
        arrayOf("", "\u1303\u1295\u1295\u122D", "\u1290\u1265\u1262\u122D", "\u121B\u130B\u122D", "\u12EB\u1275\u122D",
            "\u121B\u12CB", "\u1230\u1295\u1295", "\u1230\u1295\u1267", "\u1230\u1296\u1272", "\u1235\u1265\u1270\u121B\u1265",
            "\u1292\u1275\u127D\u121B\u1265", "\u129F\u121B\u1265\u121B\u1265")
    } else {
        arrayOf("", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Month header with navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Month + Year title
            Column(modifier = Modifier.weight(1f)) {
                val yearText = if (useGeez) GeezNumerals.toGeez(selectedYear) else selectedYear.toString()
                Text(
                    text = "$monthName $yearText",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                // Gregorian range sub-line
                val gcRange = if (firstOfEtMonth.month == lastOfEtMonth.month) {
                    "${gMonthNames[firstOfEtMonth.month]} ${firstOfEtMonth.year}"
                } else {
                    "${gMonthNames[firstOfEtMonth.month]} - ${gMonthNames[lastOfEtMonth.month]} ${firstOfEtMonth.year}"
                }
                Text(
                    text = gcRange,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.outline
                    )
                )
            }

            // Navigation arrows
            Row {
                IconButton(onClick = {
                    if (selectedMonth > 1) {
                        selectedMonth--
                    } else {
                        selectedMonth = 13
                        selectedYear--
                    }
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                IconButton(onClick = {
                    if (selectedMonth < 13) {
                        selectedMonth++
                    } else {
                        selectedMonth = 1
                        selectedYear++
                    }
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        // Day-of-week headers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 0..6) {
                Text(
                    text = EthiopianCalendar.getDayOfWeekName(i, isAmharic),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.outline
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Calendar grid
        MonthGrid(
            etYear = selectedYear,
            etMonth = selectedMonth,
            today = today,
            selectedDay = selectedDay,
            isAmharic = isAmharic,
            useGeez = useGeez,
            accentColor = accentColor,
            isDark = isDark,
            useGeez = localGeez,
            onDaySelected = { day -> selectedDay = day }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Geez/Western toggle pill (bottom bar of calendar screen)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "1 2 3",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (!localGeez) FontWeight.Bold else FontWeight.Normal,
                    color = if (!localGeez) accentColor else MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier.clickable { localGeez = false }
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Pill indicator
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .align(if (localGeez) Alignment.CenterEnd else Alignment.CenterStart)
                        .clip(CircleShape)
                        .background(accentColor)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "\u134D \u136A \u136B",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (localGeez) FontWeight.Bold else FontWeight.Normal,
                    color = if (localGeez) accentColor else MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier.clickable { localGeez = true }
            )
        }
    }
}

@Composable
private fun MonthGrid(
    etYear: Int,
    etMonth: Int,
    today: EthiopianCalendar.EthiopianDate,
    selectedDay: Int,
    isAmharic: Boolean,
    useGeez: Boolean,
    accentColor: Color,
    isDark: Boolean,
    onDaySelected: (Int) -> Unit
) {
    val daysInMonth = EthiopianCalendar.daysInMonth(etYear, etMonth)

    // Calculate the day of week for the 1st of the month
    val firstDayOfWeek = EthiopianCalendar.dayOfWeek(etYear, etMonth, 1)

    // Build grid data: leading empty cells, then day cells
    val totalCells = firstDayOfWeek + daysInMonth
    val rows = ((totalCells + 6) / 7)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        for (row in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (col in 0..6) {
                    val cellIndex = row * 7 + col
                    Box(
                        modifier = Modifier
                            .size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (cellIndex < firstDayOfWeek) {
                            // Empty / leading day from previous month
                            // Show trailing days from previous month at reduced opacity
                            val prevMonth = if (etMonth > 1) etMonth - 1 else 12
                            val prevYear = if (etMonth > 1) etYear else etYear - 1
                            val prevDays = EthiopianCalendar.daysInMonth(prevYear, prevMonth)
                            val prevDay = prevDays - (firstDayOfWeek - cellIndex) + 1
                            val dayStr = if (useGeez) GeezNumerals.toGeez(prevDay) else prevDay.toString()
                            Text(
                                text = dayStr,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                                ),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            val day = cellIndex - firstDayOfWeek + 1
                            if (day <= daysInMonth) {
                                DayCell(
                                    day = day,
                                    isToday = etYear == today.year && etMonth == today.month && day == today.day,
                                    isSelected = day == selectedDay,
                                    accentColor = accentColor,
                                    isDark = isDark,
                                    useGeez = useGeez,
                                    onClick = { onDaySelected(day) }
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun DayCell(
    day: Int,
    isToday: Boolean,
    isSelected: Boolean,
    accentColor: Color,
    isDark: Boolean,
    useGeez: Boolean,
    onClick: () -> Unit
) {
    val dayStr = if (useGeez) GeezNumerals.toGeez(day) else day.toString()

    // Determine background color
    val bgColor = when {
        isSelected && isToday -> accentColor
        isSelected -> accentColor
        isToday -> MaterialTheme.colorScheme.onBackground
        else -> Color.Transparent
    }

    // Determine text color
    val textColor = when {
        isSelected -> MaterialTheme.colorScheme.onPrimary
        isToday -> MaterialTheme.colorScheme.onPrimary
        else -> MaterialTheme.colorScheme.onBackground
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(CircleShape)
            .background(
                color = bgColor,
                shape = CircleShape
            )
            .clickable { onClick() }
    ) {
        Text(
            text = dayStr,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
                color = textColor
            ),
            textAlign = TextAlign.Center
        )
    }
}
