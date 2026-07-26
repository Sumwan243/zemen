package com.surafel.zemen.ui.converter

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.surafel.zemen.calendar.*

/**
 * Date Converter Screen — stacked drum scrollers with Convert and Swap buttons.
 * Top drum: input. Bottom drum: output (read-only).
 */
@Composable
fun ConverterScreen(
    isAmharic: Boolean,
    useGeez: Boolean,
    accentColor: Color
) {
    var isEtToGc by remember { mutableStateOf(true) } // true = ET->GC, false = GC->ET
    var convertTrigger by remember { mutableIntStateOf(0) }

    // Input drum state
    var inputMonth by remember { mutableIntStateOf(if (isEtToGc) 1 else 1) }
    var inputDay by remember { mutableIntStateOf(1) }
    var inputYear by remember { mutableIntStateOf(2017) } // Ethiopian default

    // Output drum state (animated)
    var outputMonth by remember { mutableIntStateOf(0) }
    var outputDay by remember { mutableIntStateOf(0) }
    var outputYear by remember { mutableIntStateOf(0) }
    var hasResult by remember { mutableStateOf(false) }

    // Staggered spring animations for output drums
    val animMonth by animateIntAsState(
        targetValue = outputMonth,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "month_anim"
    )
    val animDay by animateIntAsState(
        targetValue = outputDay,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "day_anim"
    )
    val animYear by animateIntAsState(
        targetValue = outputYear,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "year_anim"
    )

    // Spring animation for output
    val springAnim = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )

    // Convert action — staggered spring animation
    fun doConvert() {
        convertTrigger++
        if (isEtToGc) {
            val gc = EthiopianCalendar.ethiopianToGregorian(inputYear, inputMonth, inputDay)
            // Staggered: month first (0ms), then day (110ms), then year (220ms)
            kotlinx.coroutines.GlobalScope.launch {
                kotlinx.coroutines.delay(0)
                outputMonth = gc.month
                kotlinx.coroutines.delay(110)
                outputDay = gc.day
                kotlinx.coroutines.delay(220)
                outputYear = gc.year
                hasResult = true
            }
        } else {
            val et = EthiopianCalendar.gregorianToEthiopian(inputYear, inputMonth, inputDay)
            kotlinx.coroutines.GlobalScope.launch {
                kotlinx.coroutines.delay(0)
                outputMonth = et.month
                kotlinx.coroutines.delay(110)
                outputDay = et.day
                kotlinx.coroutines.delay(220)
                outputYear = et.year
                hasResult = true
            }
        }
    }

    // Swap action
    fun doSwap() {
        isEtToGc = !isEtToGc
        hasResult = false
        inputMonth = 1
        inputDay = 1
        inputYear = 2017
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Input label
        val inputLabel = if (isEtToGc) {
            if (isAmharic) "\u12A0\u1265\u1295\u121B \u1295\u121B\u1270" else "Ethiopian Date"
        } else {
            if (isAmharic) "\u1290\u121B\u121D\u1292\u121B \u1295\u121B\u1270" else "Gregorian Date"
        }
        Text(
            text = inputLabel,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Medium,
                color = accentColor
            ),
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Input drum row
        DrumRow(
            monthValue = inputMonth,
            dayValue = inputDay,
            yearValue = inputYear,
            monthRange = if (isEtToGc) (1..13) else (1..12),
            dayRange = if (isEtToGc) (1..30) else (1..31),
            yearRange = if (isEtToGc) (1900..2100) else (1900..2100),
            isAmharic = isAmharic,
            useGeez = useGeez && isEtToGc,
            isEtCalendar = isEtToGc,
            isReadOnly = false,
            accentColor = accentColor,
            onMonthChanged = { inputMonth = it },
            onDayChanged = { inputDay = it },
            onYearChanged = { inputYear = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Convert + Swap buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Convert button
            Button(
                onClick = { doConvert() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text(
                    text = if (isAmharic) "\u12B3\u122B\u1272\u121B\u12CB" else "Convert",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            // Swap button
            IconButton(onClick = { doSwap() }) {
                Icon(
                    Icons.Default.SwapHoriz,
                    contentDescription = "Swap",
                    tint = accentColor
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Output label
        val outputLabel = if (isEtToGc) {
            if (isAmharic) "\u1290\u121B\u121D\u1292\u121B \u1295\u120B" else "Gregorian Result"
        } else {
            if (isAmharic) "\u12A0\u1265\u1295\u121B \u1295\u120B" else "Ethiopian Result"
        }
        Text(
            text = outputLabel,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Medium,
                color = Color(0xFF378ADD)
            ),
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Output drum row (read-only, uses animated values)
        DrumRow(
            monthValue = if (hasResult) animMonth else 0,
            dayValue = if (hasResult) animDay else 0,
            yearValue = if (hasResult) animYear else 0,
            monthRange = if (!isEtToGc) (1..13) else (1..12),
            dayRange = if (!isEtToGc) (1..30) else (1..31),
            yearRange = if (!isEtToGc) (1900..2100) else (1900..2100),
            isAmharic = isAmharic,
            useGeez = useGeez && !isEtToGc,
            isEtCalendar = !isEtToGc,
            isReadOnly = true,
            accentColor = accentColor,
            onMonthChanged = {},
            onDayChanged = {},
            onYearChanged = {}
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

/**
 * DrumRow — three drum scrollers side by side (Month, Day, Year)
 */
@Composable
private fun DrumRow(
    monthValue: Int,
    dayValue: Int,
    yearValue: Int,
    monthRange: IntRange,
    dayRange: IntRange,
    yearRange: IntRange,
    isAmharic: Boolean,
    useGeez: Boolean,
    isEtCalendar: Boolean,
    isReadOnly: Boolean,
    accentColor: Color,
    onMonthChanged: (Int) -> Unit,
    onDayChanged: (Int) -> Unit,
    onYearChanged: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Month drum
        DrumScroller(
            items = monthRange.toList(),
            selectedIndex = if (monthValue > 0) monthRange.indexOf(monthValue) else 0,
            isReadOnly = isReadOnly,
            accentColor = accentColor,
            displayText = { value ->
                if (value == 0) "--"
                else if (isEtCalendar) EthiopianCalendar.getMonthName(value, isAmharic)
                else {
                    val months = if (isAmharic) {
                        arrayOf("", "\u1303\u1295\u1295\u122D", "\u1290\u1265\u1262\u122D", "\u121B\u130B\u122D", "\u12EB\u1275\u122D",
                            "\u121B\u12CB", "\u1230\u1295\u1295", "\u1230\u1295\u1267", "\u1230\u1296\u1272", "\u1235\u1265\u1270\u121B\u1265",
                            "\u1292\u1275\u127D\u121B\u1265", "\u129F\u121B\u1265\u121B\u1265")
                    } else {
                        arrayOf("", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
                    }
                    if (value in months.indices) months[value] else value.toString()
                }
            },
            onSelected = { index -> if (index < monthRange.count()) onMonthChanged(monthRange.toList()[index]) },
            modifier = Modifier.weight(3f)
        )

        // Day drum
        DrumScroller(
            items = dayRange.toList(),
            selectedIndex = if (dayValue > 0) dayRange.indexOf(dayValue) else 0,
            isReadOnly = isReadOnly,
            accentColor = accentColor,
            displayText = { value ->
                if (value == 0) "--"
                else if (useGeez) GeezNumerals.toGeez(value)
                else value.toString()
            },
            onSelected = { index -> if (index < dayRange.count()) onDayChanged(dayRange.toList()[index]) },
            modifier = Modifier.weight(2f)
        )

        // Year drum
        DrumScroller(
            items = yearRange.toList(),
            selectedIndex = if (yearValue > 0) yearRange.indexOf(yearValue) else 0,
            isReadOnly = isReadOnly,
            accentColor = accentColor,
            displayText = { value ->
                if (value == 0) "----"
                else if (useGeez) GeezNumerals.toGeez(value)
                else value.toString()
            },
            onSelected = { index -> if (index < yearRange.count()) onYearChanged(yearRange.toList()[index]) },
            modifier = Modifier.weight(2f)
        )
    }
}
