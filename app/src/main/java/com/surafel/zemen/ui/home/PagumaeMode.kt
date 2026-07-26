package com.surafel.zemen.ui.home

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import com.surafel.zemen.ui.theme.*

/**
 * Pagume Special Screen — conditional visual mode of the Today screen.
 * Triggered when currentMonth == 13.
 * Features deep purple-black gradient, violet-white accent, ambient glow animation.
 */
@Composable
fun PagumeMode(
    today: EthiopianCalendar.EthiopianDate,
    isAmharic: Boolean,
    useGeez: Boolean,
    accentColor: androidx.compose.ui.graphics.Color,
    isDark: Boolean
) {
    val gregorian = EthiopianCalendar.ethiopianToGregorian(today.year, today.month, today.day)
    val dayOfWeek = EthiopianCalendar.dayOfWeek(today.year, today.month, today.day)
    val monthName = EthiopianCalendar.getMonthName(today.month, isAmharic)

    // Ambient glow pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "pagume_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = androidx.compose.animation.core.FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(PagumeBackgroundStart, PagumeBackgroundEnd)
                )
            )
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Pagume emoji
            Text(
                text = "\u2728",
                fontSize = 48.sp,
                modifier = Modifier.offset(y = (-8).dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Ambient glow behind date
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(
                        color = PagumeAccent.copy(alpha = glowAlpha),
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Day number
                val dayText = if (useGeez) GeezNumerals.toGeez(today.day) else today.day.toString()
                Text(
                    text = dayText,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Light,
                        color = PagumeAccent
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Month name
            Text(
                text = monthName,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = PagumeAccent
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Year
            val yearText = if (useGeez) GeezNumerals.toGeez(today.year) else today.year.toString()
            Text(
                text = yearText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = PagumeAccent.copy(alpha = 0.7f)
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Gregorian reference
            val gMonthNames = arrayOf("", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
            Text(
                text = "${gregorian.day} ${gMonthNames[gregorian.month]} ${gregorian.year}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = PagumeAccent.copy(alpha = 0.5f)
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Pagume contextual description
            val pagumeDesc = if (isAmharic) {
                "\u12A0\u1233\u121D\u1295\u1293 \u121B\u121A\u1265\u127D\u1295\u1295\u12CD\u1295 \u1295\u1270\u121B\u1295 \u1234 \u1235\u1295\u121B\u12B3\u1295\u129B\u1295 \u1275\u121B\u127B\u120B\u121D"
            } else {
                "The 13th month. 5 days of transition between years."
            }
            Text(
                text = pagumeDesc,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = PagumeAccent.copy(alpha = 0.6f)
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}
