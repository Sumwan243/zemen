package com.surafel.zemen.data

import com.surafel.zemen.calendar.EthiopianCalendar

/**
 * Holiday data model and repository.
 * Holidays are organized into three tiers: Orthodox, National, Islamic.
 */
enum class HolidayTier(val dotColor: String) {
    ORTHODOX("gold"),    // #D4AF37
    NATIONAL("blue"),    // #378ADD
    ISLAMIC("green")     // #2D9B5A
}

data class Holiday(
    val id: String,
    val tier: HolidayTier,
    val etMonth: Int,
    val etDay: Int,
    val nameEn: String,
    val nameAm: String,
    val shortDescEn: String,
    val shortDescAm: String,
    val longDescEn: String,
    val longDescAm: String,
    val emoji: String = "",
    val isRecurring: Boolean = false,  // e.g. Kidus Mikael on 12th of every month
    /** If true, etMonth/etDay are computed at runtime (mobile feasts) */
    val isMobile: Boolean = false
)

/**
 * Fixed holiday data — holidays with fixed Ethiopian dates.
 */
object FixedHolidays {

    val orthodox = listOf(
        Holiday(
            id = "gena",
            tier = HolidayTier.ORTHODOX,
            etMonth = 4, etDay = 29,
            nameEn = "Gena",
            nameAm = "\u130E\u1290",
            shortDescEn = "Ethiopian Christmas",
            shortDescAm = "\u1290\u121D\u1295 \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D",
            longDescEn = "Gena is the Ethiopian Orthodox celebration of Christmas, observed on Tahsas 29 (January 7 in the Gregorian calendar). Unlike Western Christmas, Ethiopian Christmas focuses on religious observance rather than gift-giving. The day is marked by early morning church services, traditional games including Gena (a form of hockey played with a wooden stick and ball), and feasting on Doro Wat and injera.",
            longDescAm = "\u130E\u1290 \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1272\u1295\u1235\u121B\u1293\u121D \u1260\u123B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D",
            emoji = "\uD83C\uDF84"
        ),
        Holiday(
            id = "timkat",
            tier = HolidayTier.ORTHODOX,
            etMonth = 5, etDay = 11,
            nameEn = "Timkat",
            nameAm = "\u1275\u121B\u130B\u1275",
            shortDescEn = "Epiphany",
            shortDescAm = "\u1275\u121B\u130B\u1275 \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D",
            longDescEn = "Timkat is the Ethiopian Orthodox celebration of Epiphany, commemorating the baptism of Jesus Christ in the Jordan River. It is celebrated on Terr 11 (January 19). The festival involves elaborate processions with the Tabot (replica of the Ark of the Covenant) being carried to nearby bodies of water, where priests bless the water and faithful are sprinkled. It is one of the most colorful and widely celebrated festivals in Ethiopia.",
            longDescAm = "\u1275\u121B\u130B\u1275 \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1272\u1295\u1235\u121B\u1293\u121D \u1260\u123B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D",
            emoji = "\u2668\uFE0F"
        ),
        Holiday(
            id = "meskel",
            tier = HolidayTier.ORTHODOX,
            etMonth = 1, etDay = 17,
            nameEn = "Meskel",
            nameAm = "\u121B\u122D\u1293\u121B",
            shortDescEn = "Finding of the True Cross",
            shortDescAm = "\u12A0\u1265\u121B \u1233\u121B\u1273\u121B \u1343\u121B\u1295\u1275\u129B",
            longDescEn = "Meskel celebrates the discovery of the True Cross upon which Jesus was crucified. According to tradition, Queen Helena (Empress Eleni) found the cross by lighting a massive bonfire and following the smoke. The celebration on Meskerem 17 (September 27) features the Demera bonfire, beautifully decorated with Meskel daisies, singing, and dancing. It is one of the most spectacular festivals in Ethiopia.",
            longDescAm = "\u121B\u122D\u1293\u121B \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1272\u1295\u1235\u121B\u1293\u121D \u1260\u123B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D",
            emoji = "\u271D\uFE0F"
        ),
        Holiday(
            id = "enkutatash",
            tier = HolidayTier.ORTHODOX,
            etMonth = 1, etDay = 1,
            nameEn = "Enkutatash",
            nameAm = "\u12A5\u1295\u1275\u127B\u1265",
            shortDescEn = "Ethiopian New Year",
            shortDescAm = "\u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1290\u121B\u1275 \u1295\u121B\u12EB\u1270",
            longDescEn = "Enkutatash, the Ethiopian New Year, falls on Meskerem 1 (September 11, or 12 in a leap year). It marks the end of the rainy season and the beginning of spring. The holiday is celebrated with singing, dancing, exchanging of flowers (especially the Adey Abeba — yellow Meskel daisy), and special meals. Children go house-to-house singing New Year songs. Enkutatash also commemorates the return of the Queen of Sheba from her visit to King Solomon.",
            longDescAm = "\u12A5\u1295\u1275\u127B\u1265 \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1272\u1295\u1235\u121B\u1293\u121D \u1260\u123B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D",
            emoji = "\uD83C\uDF38"
        ),
        Holiday(
            id = "kidus_yohannes",
            tier = HolidayTier.ORTHODOX,
            etMonth = 11, etDay = 26,
            nameEn = "Kidus Yohannes",
            nameAm = "\u1296\u121B\u1295 \u12B8\u1214\u120B\u122D\u121B\u1295",
            shortDescEn = "St. John the Baptist",
            shortDescAm = "\u1296\u121B\u1295 \u12B8\u1214\u120B\u122D\u121B\u1295",
            longDescEn = "Kidus Yohannes commemorates St. John the Baptist, a central figure in Ethiopian Orthodox Christianity. Celebrated on Sene 26, it honors his role as the forerunner of Christ. Special church services are held, and the faithful remember his teachings and sacrifice.",
            longDescAm = "\u1296\u121B\u1295 \u12B8\u1214\u120B\u122D\u121B\u1295 \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1272\u1295\u1235\u121B\u1293\u121D \u1260\u123B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D",
            emoji = "\u26BD\uFE0F"
        ),
        Holiday(
            id = "kidus_mikael",
            tier = HolidayTier.ORTHODOX,
            etMonth = 1, etDay = 12,
            nameEn = "Kidus Mikael",
            nameAm = "\u1296\u121B\u1295 \u121B\u12CB\u123B\u12CB\u121B",
            shortDescEn = "St. Michael (monthly, 12th)",
            shortDescAm = "\u1296\u121B\u1295 \u121B\u12CB\u123B\u12CB\u121B (\u121B\u121A\u1265\u127D\u1293 \u1234\u121D\u1295)",
            longDescEn = "Kidus Mikael (St. Michael) is honored on the 12th of every Ethiopian month. St. Michael is the archangel and commander of the heavenly host in Ethiopian Orthodox tradition. Churches dedicated to him hold special services, and many Ethiopians attend mass on these days. St. Michael is one of the most venerated saints in the Ethiopian church.",
            longDescAm = "\u1296\u121B\u1295 \u121B\u12CB\u123B\u12CB\u121B \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1272\u1295\u1235\u121B\u1293\u121D \u1260\u123B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D",
            emoji = "\u2694\uFE0F",
            isRecurring = true
        ),
        Holiday(
            id = "kidus_giorgis",
            tier = HolidayTier.ORTHODOX,
            etMonth = 1, etDay = 23,
            nameEn = "Kidus Giorgis",
            nameAm = "\u1296\u121B\u1295 \u130E\u12CB\u1265\u130B\u12CB\u1295",
            shortDescEn = "St. George (monthly, 23rd)",
            shortDescAm = "\u1296\u121B\u1295 \u130E\u12CB\u1265\u130B\u12CB\u1295 (\u121B\u121A\u1265\u127D\u1293 \u1234\u121D\u1295)",
            longDescEn = "Kidus Giorgis (St. George) is celebrated on the 23rd of every Ethiopian month. St. George is the patron saint of Ethiopia and is deeply woven into the national identity. His icon — a warrior on horseback slaying a dragon — is ubiquitous in Ethiopian Orthodox churches. The day is marked by special church services and prayers.",
            longDescAm = "\u1296\u121B\u1295 \u130E\u12CB\u1265\u130B\u12CB\u1295 \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1272\u1295\u1235\u121B\u1293\u121D \u1260\u123B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D",
            emoji = "\uD83D\uDDE1\uFE0F",
            isRecurring = true
        )
    )

    val national = listOf(
        Holiday(
            id = "adwa_victory",
            tier = HolidayTier.NATIONAL,
            etMonth = 6, etDay = 23,
            nameEn = "Adwa Victory Day",
            nameAm = "\u12A0\u12E3\u12EB\u1270 \u1290\u121B\u1270 \u1295\u121B\u12EB\u1270",
            shortDescEn = "Victory of Adwa",
            shortDescAm = "\u12A0\u12E3\u12EB\u1270 \u1290\u121B\u1270 \u1295\u121B\u12EB\u1270",
            longDescEn = "Adwa Victory Day commemorates the Ethiopian victory over Italy at the Battle of Adwa on March 1, 1896. Under the leadership of Emperor Menelik II, Ethiopian forces defeated a well-armed European colonial army, making Ethiopia one of only two African nations to avoid colonization. It is a powerful symbol of African resistance and sovereignty, celebrated with patriotic events and ceremonies across the country.",
            longDescAm = "\u12A0\u12E3\u12EB\u1270 \u1290\u121B\u1270 \u1295\u121B\u12EB\u1270 \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1272\u1295\u1235\u121B\u1293\u121D \u1260\u123B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D",
            emoji = "\uD83C\uDFF4\uD83C\uDDE6\uD83C\uDDF2"
        ),
        Holiday(
            id = "workers_day",
            tier = HolidayTier.NATIONAL,
            etMonth = 8, etDay = 23,
            nameEn = "Workers' Day",
            nameAm = "\u123B\u1260\u121B\u12CB\u127D \u1295\u121B\u12EB\u1270",
            shortDescEn = "International Workers' Day",
            shortDescAm = "\u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u123B\u1260\u121B\u12CB\u127D \u1295\u121B\u12EB\u1270",
            longDescEn = "International Workers' Day, also known as May Day, is celebrated on Miazia 23 (May 1). It honors the labor movement and workers' rights. In Ethiopia, it is marked by public events, speeches, and gatherings organized by trade unions and government bodies.",
            longDescAm = "\u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u123B\u1260\u121B\u12CB\u127D \u1295\u121B\u12EB\u1270 \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1272\u1295\u1235\u121B\u1293\u121D \u1260\u123B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D",
            emoji = "\u2702\uFE0F"
        ),
        Holiday(
            id = "patriots_victory",
            tier = HolidayTier.NATIONAL,
            etMonth = 8, etDay = 27,
            nameEn = "Patriots' Victory Day",
            nameAm = "\u12A0\u121B\u1290\u121B\u128E\u127D\u1295\u129B \u1295\u121B\u12EB\u1270",
            shortDescEn = "Victory over Italian occupation",
            shortDescAm = "\u12A0\u121B\u1290\u121B\u128E\u127D\u1295\u129B \u1295\u121B\u12EB\u1270",
            longDescEn = "Patriots' Victory Day on Miazia 27 (May 5) commemorates the liberation of Ethiopia from Italian fascist occupation in 1941. Ethiopian patriots, known as Arbegnoch, fought a five-year guerrilla war alongside Allied forces to restore Ethiopian sovereignty under Emperor Haile Selassie. The day honors their bravery and sacrifice.",
            longDescAm = "\u12A0\u121B\u1290\u121B\u128E\u127D\u1295\u129B \u1295\u121B\u12EB\u1270 \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1272\u1295\u1235\u121B\u1293\u121D \u1260\u123B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D",
            emoji = "\uD83C\uDDFA\uD83C\uDDF2"
        ),
        Holiday(
            id = "derg_downfall",
            tier = HolidayTier.NATIONAL,
            etMonth = 9, etDay = 20,
            nameEn = "Derg Downfall Day",
            nameAm = "\u12B3\u1265\u130B\u1270 \u1290\u121B\u1270 \u1295\u121B\u12EB\u1270",
            shortDescEn = "Fall of the Derg regime",
            shortDescAm = "\u12B3\u1265\u130B\u1270 \u1290\u121B\u1270 \u1295\u121B\u12EB\u1270",
            longDescEn = "Derg Downfall Day on Ginbot 20 (May 28) marks the fall of the Derg military junta in 1991 and the establishment of the Federal Democratic Republic of Ethiopia. The day is celebrated as a national holiday with public ceremonies and remembrances.",
            longDescAm = "\u12B3\u1265\u130B\u1270 \u1290\u121B\u1270 \u1295\u121B\u12EB\u1270 \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1272\u1295\u1235\u121B\u1293\u121D \u1260\u123B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D",
            emoji = "\uD83D\uDCF0"
        ),
        Holiday(
            id = "nations_day",
            tier = HolidayTier.NATIONAL,
            etMonth = 3, etDay = 29,
            nameEn = "Nations, Nationalities & Peoples' Day",
            nameAm = "\u1292\u121B\u127D\u1295\u129B \u1290\u121B\u1270 \u1295\u121B\u12EB\u1270",
            shortDescEn = "Ethnic diversity celebration",
            shortDescAm = "\u1292\u121B\u127D\u1295\u129B \u1290\u121B\u1270 \u1295\u121B\u12EB\u1270",
            longDescEn = "Celebrated on Hidar 29 (December 9), this day honors the nations, nationalities, and peoples of Ethiopia. It recognizes the country's remarkable ethnic and cultural diversity and the constitutional principle of self-determination. The day features cultural performances, exhibitions, and speeches celebrating unity in diversity.",
            longDescAm = "\u1292\u121B\u127D\u1295\u129B \u1290\u121B\u1270 \u1295\u121B\u12EB\u1270 \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1272\u1295\u1235\u121B\u1293\u121D \u1260\u123B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D",
            emoji = "\uD83C\uDF0D"
        )
    )

    /** Get all fixed holidays */
    fun getAll(): List<Holiday> = orthodox + national
}

/**
 * HolidayRepository — combines fixed + mobile holidays,
 * respects tier toggles from Settings.
 */
class HolidayRepository {

    /** Get holidays for a specific Ethiopian month, filtered by enabled tiers */
    fun getHolidaysForMonth(
        etYear: Int,
        etMonth: Int,
        orthodoxEnabled: Boolean = true,
        nationalEnabled: Boolean = true,
        islamicEnabled: Boolean = true
    ): List<Holiday> {
        val holidays = mutableListOf<Holiday>()

        if (orthodoxEnabled) {
            holidays.addAll(
                FixedHolidays.orthodox.filter { h ->
                    h.etMonth == etMonth || h.isRecurring
                }
            )
        }

        if (nationalEnabled) {
            holidays.addAll(
                FixedHolidays.national.filter { h -> h.etMonth == etMonth }
            )
        }

        // Islamic holidays computed via ummalqura-calendar
        if (islamicEnabled) {
            val today = EthiopianCalendar.today()
            val gcToday = EthiopianCalendar.ethiopianToGregorian(today.year, today.month, today.day)
            val islamic = com.surafel.zemen.calendar.HijriConverter.getIslamicHolidaysForYear(gcToday.year)
            // Also include next year's holidays that may fall in the current ET year
            val islamicNext = com.surafel.zemen.calendar.HijriConverter.getIslamicHolidaysForYear(gcToday.year + 1)
            holidays.addAll(islamic + islamicNext)
        }

        return holidays
    }

    /** Check if a specific Ethiopian date is a holiday, return the holiday */
    fun getHolidayForDate(
        etYear: Int,
        etMonth: Int,
        etDay: Int,
        orthodoxEnabled: Boolean = true,
        nationalEnabled: Boolean = true,
        islamicEnabled: Boolean = true
    ): Holiday? {
        val candidates = mutableListOf<Holiday>()

        if (orthodoxEnabled) {
            candidates.addAll(FixedHolidays.orthodox)
        }
        if (nationalEnabled) {
            candidates.addAll(FixedHolidays.national)
        }

        return candidates.find { h ->
            if (h.isRecurring) {
                h.etDay == etDay
            } else {
                h.etMonth == etMonth && h.etDay == etDay
            }
        }
    }

    /** Get the next upcoming feast from a given date */
    fun getNextFeast(
        etYear: Int,
        etMonth: Int,
        etDay: Int
    ): Holiday? {
        val allHolidays = FixedHolidays.getAll().filter { !it.isRecurring }
        val currentJdn = EthiopianCalendar.ethiopianToJdn(etYear, etMonth, etDay)

        return allHolidays
            .map { h ->
                Triple(h, EthiopianCalendar.ethiopianToJdn(etYear, h.etMonth, h.etDay), EthiopianCalendar.ethiopianToJdn(etYear, h.etMonth, h.etDay) - currentJdn)
            }
            .filter { (_, _, daysAhead) -> daysAhead > 0 }
            .minByOrNull { (_, _, daysAhead) -> daysAhead }
            ?.first
    }
}
