package com.surafel.zemen.ui.feasts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.surafel.zemen.calendar.EthiopianCalendar
import com.surafel.zemen.data.*

/**
 * Feasts & Seasons Screen — holiday timeline and season cards.
 */
@Composable
fun FeastsScreen(
    isAmharic: Boolean,
    useGeez: Boolean,
    accentColor: Color,
    orthodoxEnabled: Boolean,
    nationalEnabled: Boolean,
    islamicEnabled: Boolean
) {
    val selectedFeast = remember { mutableStateOf<Holiday?>(null) }
    val selectedSeason = remember { mutableStateOf<EthiopianCalendar.Season?>(null) }
    val showBahraHasab = remember { mutableStateOf(false) }
    val today = EthiopianCalendar.today()

    // Season data for display
    val seasonData = listOf(
        Triple(
            EthiopianCalendar.SEASON_MAP[0],
            "\uD83C\uDF27\uFE0F",
            if (isAmharic) "ክረምት — የዝናብ ወቅት. ከሰኔ እስከ መስከረም ድረስ. አካባቢው አስተማማኝ እርጥበት ያገኛል."
                    else "Kiremt — the rainy season. Covers approximately June through September. The Ethiopian highlands turn lush green with daily rains, creating dramatic landscapes of mist-covered valleys and cascading waterfalls."
        ),
        Triple(
            EthiopianCalendar.SEASON_MAP[1],
            "\uD83C\uDF3E\uFE0F",
            if (isAmharic) "ጅደይ — የማርቆስ ወቅት. የአብይ ውስጥ የበጋ ዝናብ ያቁማል. ሞራሎች ወይራ ለብሰው ያበራሉ."
                    else "Tsedey — the harvest season, running roughly from September through December. The landscape shifts to golden tones as crops ripen and the famous Meskel daisies bloom across the highlands."
        ),
        Triple(
            EthiopianCalendar.SEASON_MAP[2],
            "\u2600\uFE0F",
            if (isAmharic) "በጋ — የደረቅ እርሻ ወቅት. ሰማያዊ ሰማይ እና ጸጥታ. ቲምቃት አካል በዚህ ጊዜ ይካሄዳል."
                    else "Bega — the dry cool season, from December through March. Clear blue skies dominate the highlands, and this is when Timkat celebrations take place in open-air ceremonies."
        ),
        Triple(
            EthiopianCalendar.SEASON_MAP[3],
            "\uD83C\uDF3C\uFE0F",
            if (isAmharic) "በልግ — የአቋራጭ ጥልቅስ ወቅት. አበባ ዘርፎች ሲያበቁሩ. የሜስቀል ዱቄት ዘገባ ላይ."
                    else "Belg — the short rains season, from March through June. The countryside blossoms with yellow Meskel daisies and the brief spring rains bring new life before the heavy Kiremt season."
        )
    )

    val allHolidays = FixedHolidays.getAll().filter { h ->
        when (h.tier) {
            HolidayTier.ORTHODOX -> orthodoxEnabled
            HolidayTier.NATIONAL -> nationalEnabled
            HolidayTier.ISLAMIC -> islamicEnabled
        }
    }.sortedBy { it.etMonth * 100 + it.etDay }

    Box(modifier = Modifier.fillMaxSize()) {
        if (showBahraHasab.value) {
            BahraHasabScreen(
                isAmharic = isAmharic,
                useGeez = useGeez,
                accentColor = accentColor,
                onBack = { showBahraHasab.value = false }
            )
        } else {
            LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Bahra Hasab button
            item {
                OutlinedButton(
                    onClick = { showBahraHasab.value = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp, accentColor.copy(alpha = 0.4f)
                    )
                ) {
                    Text(
                        text = if (isAmharic) "\u12B3\u121B\u1265 \u12C8\u121B\u121D\u1275 \u1280\u121B\u12CB" else "Open Bahra Hasab Engine",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = accentColor
                        )
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Season cards header
            item {
                Text(
                    text = if (isAmharic) "\u1235\u1275\u12CD\u127D" else "Seasons",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Season cards
            items(seasonData) { (season, emoji, description) ->
                SeasonCard(
                    season = season,
                    emoji = emoji,
                    description = description,
                    accentColor = accentColor,
                    isAmharic = isAmharic,
                    onClick = { selectedSeason.value = season }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Pagume card
            item {
                SeasonCard(
                    season = EthiopianCalendar.Season(
                        nameEn = "Pagume",
                        nameAm = "\u1343\u1308\u1335\u121B",
                        emoji = "\u2728",
                        startMonth = 13,
                        endMonth = 13
                    ),
                    emoji = "\u2728",
                    description = if (isAmharic)
                        "ኳጉሜን — የ13ኛው ወር. በአብይ አመት መጨረሻ ላይ ያለ የተለየ ጊዜ. ዓመት ከዓመት ወደ ዓመት የሚለውጥ ወቅት."
                    else
                        "Pagume — the 13th month of the Ethiopian calendar. A sacred 5-day period of transition between years. The app enters a special visual state during this time.",
                    accentColor = Color(0xFFC4B8E8),
                    isAmharic = isAmharic,
                    isSpecial = true,
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Holiday timeline header
            item {
                Text(
                    text = if (isAmharic) "\u1290\u121B\u1270\u127D\u1295" else "Holidays",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Holiday list
            items(allHolidays) { holiday ->
                HolidayTimelineItem(
                    holiday = holiday,
                    isAmharic = isAmharic,
                    useGeez = useGeez,
                    onClick = { selectedFeast.value = holiday }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Bottom spacing
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
        } // end else (main content)

        // Feast detail bottom sheet
        selectedFeast.value?.let { feast ->
            FeastDetailSheet(
                feast = feast,
                isAmharic = isAmharic,
                onDismiss = { selectedFeast.value = null }
            )
        }

        // Season detail fullscreen sheet
        selectedSeason.value?.let { season ->
            SeasonDetailSheet(
                season = selectedSeason.value!!,
                seasonData = seasonData,
                isAmharic = isAmharic,
                accentColor = accentColor,
                onDismiss = { selectedSeason.value = null }
            )
        }
    }
}

@Composable
private fun SeasonCard(
    season: EthiopianCalendar.Season,
    emoji: String,
    description: String,
    accentColor: Color,
    isAmharic: Boolean,
    isSpecial: Boolean = false,
    onClick: () -> Unit
) {
    val name = if (isAmharic) season.nameAm else season.nameEn

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSpecial) {
                androidx.compose.ui.graphics.Color(0xFF1A1228)
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 36.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = if (isSpecial) Color(0xFFC4B8E8) else MaterialTheme.colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isSpecial) Color(0xFFC4B8E8).copy(alpha = 0.7f)
                               else MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                )
            }
        }
    }
}

@Composable
private fun HolidayTimelineItem(
    holiday: Holiday,
    isAmharic: Boolean,
    useGeez: Boolean,
    onClick: () -> Unit
) {
    val dotColor = when (holiday.tier) {
        HolidayTier.ORTHODOX -> EthiopianGold  // Color(0xFFD4AF37)
        HolidayTier.NATIONAL -> Color(0xFF378ADD)
        HolidayTier.ISLAMIC -> Color(0xFF2D9B5A)
    }

    val name = if (isAmharic) holiday.nameAm else holiday.nameEn

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Colored dot
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(dotColor, androidx.compose.foundation.shape.CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${holiday.emoji}  $name",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
            Text(
                text = "${holiday.etMonth}/${holiday.etDay}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.outline
                )
            )
        }
    }
}

@Composable
private fun FeastDetailSheet(
    feast: Holiday,
    isAmharic: Boolean,
    onDismiss: () -> Unit
) {
    val name = if (isAmharic) feast.nameAm else feast.nameEn
    val desc = if (isAmharic) feast.longDescAm else feast.longDescEn
    val shortDesc = if (isAmharic) feast.shortDescAm else feast.shortDescEn

    // Simple overlay bottom sheet
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onDismiss() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .clickable(enabled = false) {},
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Handle bar
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .background(
                            MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(2.dp)
                        )
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Emoji + Name
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = feast.emoji, fontSize = 32.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = name,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Short desc
                Text(
                    text = shortDesc,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Long desc
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

/**
 * Season Detail Fullscreen Sheet.
 * Expands to show full description, months covered, notable holidays,
 * traditional foods and practices — all in the selected language.
 */
@Composable
private fun SeasonDetailSheet(
    season: EthiopianCalendar.Season,
    seasonData: List<Triple<EthiopianCalendar.Season, String, String>>,
    isAmharic: Boolean,
    accentColor: Color,
    onDismiss: () -> Unit
) {
    val data = seasonData.find { it.first.nameEn == season.nameEn }
    val emoji = data?.second ?: ""
    val baseDescription = data?.third ?: ""

    // Full extended descriptions for each season
    val extendedDesc = when (season.nameEn) {
        "Kiremt" -> if (isAmharic)
            "ክረምት የኢትዮጵያ ከፍል-ምድሮች ላይ ከሰኔ እስከ መስከረም ድረስ ያለውን የዝናብ ወቅት ይወክላል። ይህ ወቅት አገር አቀፍ የሰብል ስራ ዋስትና የሚገኝበትን ወቅት ያመላክታል። በተለይሙ ሰና፣ ጎጃም፣ ወርሃገ፣ ወርክም ዘርፎች ይተከሉ እንዲሁም የአትክልት ስራ ተግባርነት ይጨመረታል። የክረምት ወቅት የብረት ብርሃን በሚያምርበት በእርብ የሚያንቀሳቀስ ትንሽ ትርፊያ ነው — ሰማያዊ ቀለም ከሰማይ ይረፈሰፍ ሲሆን። በዚህ ወቅት ቤቶች ውስጥ በሚቀመጥሩ ቡና እና ቡና ማቅለብ ላይ የተመሰረተ የሰላም ባህርይ ሲወስድ። ባህሉ ደስታ እና ምግብ ይይዛል። ሰዎች በራስ-ራስነት የሚመገቡ በተለያዊ ዘይቤቶች ይሳማማራሉ።"
        else
            "Kiremt is the rainy season across the Ethiopian highlands, running roughly from June through September. It is the most agriculturally significant period of the year, when the main crops — teff, maize, sorghum, and barley — are planted and nurtured by daily afternoon rains. The landscape transforms into a lush tapestry of deep greens, mist-covered valleys, and cascading waterfalls. Rivers swell and the countryside comes alive with birdsong and wildflowers. Families gather around warm meals of shiro wot, doro wot, and genfo during the cooler rainy evenings. Kiremt is also celebrated culturally through the holiday of Enkutatash, which marks the transition from the rainy season to the harvest."

        "Tsedey" -> if (isAmharic)
            "ጅደይ የማርቆስ ወቅት ነው። ከመስከረም እስከ ታኅሣሥ ድረስ ያለውን ወቅት ይይዛል። አካባቢው ወይራማ ቀለም ያለው እና የመስቀል ድሮ አበቦች በሰራዊት ላይ ይበቁላሉ። በዚህ ወቅት ከፍል-ምድሮች ላይ አበቦች እና ሰብሎች ይረከባሉ። ሰብሎቹ ይበሩ እና ሰብሎች ሲያጠኑ በማህበር የተገናኙ ይሆናል። የአትክልት ስራ ይጨመረታል። በተለይሙ የመስቀል በዓል በመስከረም 17 በየተለይ ሰብሎች እና በዓላት ሲከበረቡ ይደረጃል።"
        else
            "Tsedey, the harvest season, runs approximately from September through December. It is the most joyful period in the Ethiopian agricultural calendar, when golden fields of teff, barley, and wheat are ready for reaping. The landscape shifts from the deep greens of Kiremt to warm amber and gold tones. The famous Meskel daisies (Adey Abeba) bloom across the highlands, creating spectacular yellow carpets. Families come together for communal harvesting, sharing labor and celebrating with food, drink, and song. The Meskel festival in September is the centerpiece of Tsedey — a vibrant celebration featuring the Demera bonfire, religious processions, and feasting on doro wot and kitfo."

        "Bega" -> if (isAmharic)
            "በጋ የደረቅ ወቅት ነው። ከታኅሣሥ እስከ ሚያዝያ ድረስ ያለውን ወቅት ይይዛል። ሰማያዊ ሰማይ እና ጸጥታ የከፍል-ምድሮቹን ያጋጥማል። ይህ ወቅት ቲምቃት በእርብ እና በከተማ ማዕከላዎች የሚካሄደውን የቅዱስ ውሃ በዓል ያመላክታል። በተለይሙ ቲምቃት በእርብ ተገምቶ በተለይ የአትክልት ውስጥ በእርብ ሲተገምት ይደረጃል። የብረት እና የሰማይ የሚትገቡ በዓላት ይታያሉ። በተለይሙ ባህሉ እና ሰላም ይደርሳል።"
        else
            "Bega is the dry cool season, from December through March. Clear blue skies dominate the Ethiopian highlands, creating some of the most spectacular stargazing conditions in Africa. This is when Timkat (Ethiopian Epiphany) is celebrated — one of the most colorful and important festivals in Ethiopia, featuring massive processions with priests carrying the Tabot (replica of the Ark of the Covenant) to bodies of water for blessing ceremonies. The air is crisp and cool, especially at altitude, and the dry season is ideal for travel, construction, and outdoor activities. Traditional foods associated with Bega include roasted barley (kolo) and the warming stew shiro."

        else -> if (isAmharic)
            "በልግ የአቋራጭ ጥልቅስ ወቅት ነው። ከሚያዝያ እስከ ሰኔ ድረስ ያለውን ወቅት ይይዛል። አካባቢው በየተለይ አበቦች እና የመስቀል ድሮ አበቦች በተራሮች ላይ ይበቁላሉ። በዚህ ወቅት አካባቢው በተስፋፊ ማህበረ ሰብ ይተሰማል። የአትክልት ስራ በመጋቢት ይገባል። የማይ ትንሽ ጥልቅስ በተለይ ሰማይ ይዘነባል። የአትክልት ስራ በተለይ የሚገባ ወቅት ነው። ሰብሎች ሲያጠኑ ይተገባል። ባህሉ እና ሰላም ይደርሳል።"
        else
            "Belg is the short rains season, from March through June. The countryside blossoms with yellow Meskel daisies and wildflowers, creating a brief but beautiful spring period before the heavy Kiremt rains return. The short rains are crucial for secondary crop planting and pasture regeneration. This is when the agricultural cycle begins its next phase — farmers prepare their fields and plant early crops. Belg is a season of renewal and hope, when the Ethiopian highlands briefly shimmer in gold and green. It is also associated with the celebration of Fasika (Ethiopian Easter), the most important feast in the Ethiopian Orthodox calendar."
    }

    // Notable holidays in this season
    val notableHolidays = when (season.nameEn) {
        "Kiremt" -> if (isAmharic) listOf("\u12A5\u1295\u1275\u127B\u1265 (አዳሲስ ዓመት)") else listOf("Enkutatash (Ethiopian New Year)")
        "Tsedey" -> if (isAmharic) listOf("\u121B\u122D\u1293\u121B (\uየእምነት መስቀል)", "\u130E\u1290 (ገና)", "\u1275\u121B\u130B\u1275 (ጥምቀት)") else listOf("Meskel (Finding of the True Cross)", "Gena (Ethiopian Christmas)", "Timkat (Epiphany)")
        "Bega" -> if (isAmharic) listOf("\u1275\u121B\u130B\u1275 (ጥምቀት)", "\u134D\u121D\u12CB\u123B (ፋሲካ)") else listOf("Timkat (Epiphany)", "Fasika (Easter)")
        else -> if (isAmharic) listOf("\u134D\u121D\u12CB\u123B (ፋሲካ)", "\u1290\u121B\u1270\u127Dች ዓሳን") else listOf("Fasika (Easter)", "Various feast days")
    }

    // Traditional foods
    val foods = when (season.nameEn) {
        "Kiremt" -> if (isAmharic) "ሾሮ ወጥ፣ ዶሮ ወጥ፣ ገንፎ — በእርብ ያምርቡ እና በሚቀመጥሩ ምግቦች ይተሰማሉ።" else "Shiro wot, Doro wot, Genfo — hearty warm stews and porridge enjoyed during the cool rainy evenings."
        "Tsedey" -> if (isAmharic) "ዶሮ ወጥ፣ ኪቶ፣ ተፈላ — በመስቀል በዓል ይመገባሉ።" else "Doro wot, Kitfo, Teff bread — special feast dishes prepared for Meskel celebrations."
        "Bega" -> if (isAmharic) "ኮሎ (የብርት አበባ)፣ ሾሮ ወጥ — የብረት ምግቦች ይወስዳሉ።" else "Kolo (roasted barley), Shiro wot — warming foods for the cool dry season."
        else -> if (isAmharic) "ዶሮ ወጥ፣ ገንፎ — በፋሲካ በዓል ይመገባሉ።" else "Doro wot, Genfo — celebratory dishes for Fasika and the spring season."
    }

    // Fullscreen overlay
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clickable { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Back handle + emoji + name
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = emoji, fontSize = 40.sp)
                Spacer(modifier = Modifier.width(16.dp))
                val seasonTitle = if (isAmharic) season.nameAm else season.nameEn
                Text(
                    text = seasonTitle,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Medium,
                        color = accentColor
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Months covered
            val monthsLabel = if (isAmharic) "\u1233\u1275\u1272\u127D" else "Months"
            Text(
                text = "$monthsLabel: ${if (isAmharic) season.nameAm else season.nameEn}",
                style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.outline)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Full description
            Text(
                text = extendedDesc,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Notable holidays
            val holidaysLabel = if (isAmharic) "\u1290\u121B\u1270\u127D\u1295" else "Notable Holidays"
            Text(
                text = holidaysLabel,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = accentColor
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            notableHolidays.forEach { holiday ->
                Text(
                    text = "\u2022  $holiday",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Traditional foods
            val foodsLabel = if (isAmharic) "\u1290\u121B\u1272\u1293" else "Traditional Foods"
            Text(
                text = foodsLabel,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = accentColor
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = foods,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
