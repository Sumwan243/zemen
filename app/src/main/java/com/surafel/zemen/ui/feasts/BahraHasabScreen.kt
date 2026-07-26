package com.surafel.zemen.ui.feasts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.surafel.zemen.calendar.BahraHasab
import com.surafel.zemen.calendar.EthiopianCalendar
import com.surafel.zemen.calendar.GeezNumerals
import com.surafel.zemen.data.Holiday

/**
 * Bahra Hasab Sub-Screen.
 * Accessible from the Feasts & Seasons tab.
 * Features: year input with arrows, Calculate button, Evangelist display,
 * Fasika date, and scrollable list of all derived mobile feasts.
 */
@Composable
fun BahraHasabScreen(
    isAmharic: Boolean,
    useGeez: Boolean,
    accentColor: Color,
    onBack: () -> Unit
) {
    val today = EthiopianCalendar.today()
    var inputYear by remember { mutableIntStateOf(today.year) }
    var result by remember { mutableStateOf<BahraHasab.FasikaResult?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Top bar with back button
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
                text = if (isAmharic) "\u12B3\u121B\u1265 \u12C8\u121B\u121D\u1275" else "Bahra Hasab",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Year input
        Text(
            text = if (isAmharic) "\u12A0\u1265\u1295\u121B \u1295\u121B\u1270" else "Ethiopian Year",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Decrement
            IconButton(onClick = { inputYear-- }) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Decrease year",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            // Year display
            val yearText = if (useGeez) GeezNumerals.toGeez(inputYear) else inputYear.toString()
            Text(
                text = yearText,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.width(160.dp)
            )

            Spacer(modifier = Modifier.width(24.dp))

            // Increment
            IconButton(onClick = { inputYear++ }) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Increase year",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Calculate button
        Button(
            onClick = {
                result = BahraHasab.compute(inputYear)
            },
            modifier = Modifier.fillMaxWidth(0.5f),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = accentColor,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = if (isAmharic) "\u121B\u1235\u123B\u121B" else "Calculate",
                style = MaterialTheme.typography.labelLarge
            )
        }

        // Results
        result?.let { r ->
            Spacer(modifier = Modifier.height(24.dp))

            // Evangelist card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isAmharic) "\u1290\u121B\u121D\u1292\u121B\u1295" else "Evangelist of the Year",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.outline
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isAmharic) r.evangelist.nameAm else r.evangelist.nameEn,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = accentColor
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Fasika date
                    Text(
                        text = if (isAmharic) "\u134D\u121D\u12CB\u123B (\u1290\u121B\u1270)" else "Fasika (Easter)",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.outline
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    val fasikaMonth = EthiopianCalendar.getMonthName(r.fasikaDate.month, isAmharic)
                    val fasikaDay = if (useGeez) GeezNumerals.toGeez(r.fasikaDate.day) else r.fasikaDate.day.toString()
                    Text(
                        text = "$fasikaMonth $fasikaDay, ${r.fasikaDate.year}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${r.fasikaGregorian.day}/${r.fasikaGregorian.month}/${r.fasikaGregorian.year} GC",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.outline
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mobile feasts header
            Text(
                text = if (isAmharic) "\u1290\u121B\u1270\u127D\u1295 \u12A0\u1233\u122B\u1295\u1275\u121B\u127D\u1295" else "Derived Mobile Feasts",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Scrollable feast list
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                r.mobileFeasts.forEachIndexed { index, feast ->
                    MobileFeastCard(
                        feast = feast,
                        isAmharic = isAmharic,
                        useGeez = useGeez,
                        accentColor = accentColor
                    )
                    if (index < r.mobileFeasts.lastIndex) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Bottom padding for scroll
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun MobileFeastCard(
    feast: BahraHasab.MobileFeast,
    isAmharic: Boolean,
    useGeez: Boolean,
    accentColor: Color
) {
    val name = if (isAmharic) feast.nameAm else feast.nameEn
    val etMonth = EthiopianCalendar.getMonthName(feast.etDate.month, isAmharic)
    val etDay = if (useGeez) GeezNumerals.toGeez(feast.etDate.day) else feast.etDate.day.toString()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Feast name
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Dates
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$etMonth $etDay, ${feast.etDate.year} ET",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = accentColor
                    )
                )
                Text(
                    text = "${feast.gcDate.day}/${feast.gcDate.month}/${feast.gcDate.year} GC",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.outline
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            val desc = if (isAmharic) feast.descriptionAm else feast.descriptionEn
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                maxLines = 4
            )
        }
    }
}
