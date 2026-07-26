package com.surafel.zemen.calendar

/**
 * Bahra Hasab (ባሕረ ሕኃ፵) — Ethiopian Orthodox computus engine.
 * Calculates the Evangelist of the year and the date of Fasika (Ethiopian Easter),
 * then derives all mobile feast dates from it.
 *
 * Pure Kotlin — no Android dependencies for unit testability.
 */
object BahraHasab {

    /** The four Evangelists in cycle order */
    enum class Evangelist(val nameEn: String, val nameAm: String) {
        JOHN("John", "\u12B8\u1214\u120B\u122D\u121B\u1295"),
        MATTHEW("Matthew", "\u121B\u121D\u1275\u12CE\u129B"),
        MARK("Mark", "\u121B\u1228\u1265\u130B"),
        LUKE("Luke", "\u126C\u1295\u12CE")
    }

    data class FasikaResult(
        val evangelist: Evangelist,
        val fasikaDate: EthiopianCalendar.EthiopianDate,
        val fasikaGregorian: EthiopianCalendar.GregorianDate,
        val mobileFeasts: List<MobileFeast>
    )

    data class MobileFeast(
        val nameEn: String,
        val nameAm: String,
        val etDate: EthiopianCalendar.EthiopianDate,
        val gcDate: EthiopianCalendar.GregorianDate,
        val descriptionEn: String,
        val descriptionAm: String
    )

    /**
     * Get the Evangelist for a given Ethiopian year.
     * The 4-year cycle: John -> Matthew -> Mark -> Luke
     */
    fun getEvangelist(etYear: Int): Evangelist {
        val index = (etYear - 1) % 4
        return Evangelist.entries[index]
    }

    /**
     * Calculate Fasika (Ethiopian Easter) date for a given Ethiopian year.
     *
     * Algorithm:
     * 1. Find the corresponding Gregorian year (ET year + 7 for Jan-Sep, +8 for Oct-Dec)
     * 2. Use the Alexandrian computus to find Easter Sunday
     * 3. Convert back to Ethiopian date
     */
    fun calculateFasika(etYear: Int): EthiopianCalendar.EthiopianDate {
        // Ethiopian year starts on Sept 11 (or 12 in leap year)
        // Fasika falls in Ethiopian month of Megabit (month 7)
        // Fasika is always in the first half of the Ethiopian year
        val gYear = etYear + 7

        // Alexandrian Easter calculation (Meeus/Jones/Butcher algorithm)
        val a = gYear % 4
        val b = gYear % 7
        val c = gYear % 19
        val d = (19 * c + 20) % 30
        val e = (2 * a + 4 * b - d + 34) % 7
        // March days in the month before Easter
        val marchDays = (d + e + 114) / 31
        val day = ((d + e + 114) % 31) + 1
        // Easter is in March or April
        val gMonth = if (marchDays == 3) 3 else 4
        val gDay = day

        return EthiopianCalendar.gregorianToEthiopian(gYear, gMonth, gDay)
    }

    /**
     * Get full Bahra Hasab computation for a year.
     * Returns Evangelist, Fasika date, and all derived mobile feasts.
     */
    fun compute(etYear: Int): FasikaResult {
        val evangelist = getEvangelist(etYear)
        val fasikaDate = calculateFasika(etYear)
        val fasikaGregorian = EthiopianCalendar.ethiopianToGregorian(
            fasikaDate.year, fasikaDate.month, fasikaDate.day
        )

        val mobileFeasts = deriveMobileFeasts(etYear, fasikaDate)

        return FasikaResult(
            evangelist = evangelist,
            fasikaDate = fasikaDate,
            fasikaGregorian = fasikaGregorian,
            mobileFeasts = mobileFeasts
        )
    }

    /**
     * Derive mobile feast dates from Fasika.
     * All dates are relative to Fasika.
     */
    private fun deriveMobileFeasts(
        etYear: Int,
        fasika: EthiopianCalendar.EthiopianDate
    ): List<MobileFeast> {
        val feasts = mutableListOf<MobileFeast>()

        // Helper: add days to an Ethiopian date
        fun addDays(date: EthiopianCalendar.EthiopianDate, daysToAdd: Int): EthiopianCalendar.EthiopianDate {
            val jdn = EthiopianCalendar.ethiopianToJdn(date.year, date.month, date.day)
            return EthiopianCalendar.jdnToEthiopian(jdn + daysToAdd)
        }

        // Tsome Nebiyat (Fast of the Prophets) - starts 43 days before Fasika
        val tsomeNebiyatStart = addDays(fasika, -43)
        feasts.add(MobileFeast(
            nameEn = "Fast of the Prophets (Start)",
            nameAm = "\u1335\u123B\u121B \u1290\u121B\u12CB\u121D\u1270",
            etDate = tsomeNebiyatStart,
            gcDate = EthiopianCalendar.ethiopianToGregorian(tsomeNebiyatStart.year, tsomeNebiyatStart.month, tsomeNebiyatStart.day),
            descriptionEn = "The Fast of the Prophets (Tsome Nebiyat) begins 43 days before Fasika. It commemorates the fast of the prophets who awaited the coming of Christ. This is one of the major fasting periods in the Ethiopian Orthodox Church, observed with abstinence from animal products and a focus on prayer.",
            descriptionAm = "\u1335\u123B\u121B \u1290\u121B\u12CB\u121D\u1270 \u1290\u121B \u1260\u1272 \u1234 \u1260\u130A\u1272 \u1235\u1295 \u12DB\u12CD\u1235 \u1272\u1228\u12CB\u12CD \u12ED \u12AD\u121B\u120B\u121D\u1295 \u134D\u1295\u120B \u121B\u12AD\u120D \u1290\u121B\u127D \u130B\u1270\u121D\u127B \u12ED \u1235\u121D\u130C\u127B\u121D\u127D\u1295 \u130B\u1293 \u1343\u120B\u121D \u1218\u123B\u127D\u1295 \u1235\u12ED\u12EB\u121D\u1275 \u1260\u121B\u120D\u127B\u121D\u127D\u1295 \u12ED \u121C\u123B\u12EB\u127B\u121D\u127D \u1270\u121B\u123D\u12CB \u12ED \u1272\u121D\u1270\u1308\u120B\u121D\u1275\u12A0 \u1290\u121B\u1270 \u1218\u123B\u12CD\u1233 \u12ED \u134D\u122B\u1272 \u12A0\u1270\u121B\u120B\u121D \u1295\u120B\u1270\u121D \u121C\u1235\u121D\u127B \u12ED \u12A0\u1265\u129B \u1270\u121B\u123D\u120B\u1275\u12A0 \u1218\u120D\u12EB\u121D\u127D\u1295\u12A0 \u12ED \u1275\u12B3\u1295 \u120B\u121D\u1228\u1293\u1270\u121D\u127D\u1295\u12A0\u1295 \u12ED \u1218\u12CD\u12EB\u1295\u1293 \u122A\u127B\u12CD\u1233\u1295 \u12ED \u1260\u1272 \u121C\u123B\u12CD\u12B3\u1295 \u121D\u122A\u1275\u121D\u127B \u12ED \u1218\u123B\u12CD \u130B\u1293 \u1275\u12B3\u1295\u120B \u121D\u122A\u1275\u121D\u127D \u12ED \u1275\u12B3\u1295\u120B \u12A0\u1260\u122D\u127B\u121D\u127D\u1295\u12A0\u1295 \u12ED \u1218\u1228\u1270 \u12ED \u12A0\u1270\u121B\u120B\u121D \u1295\u120B\u1270\u121D\u1295 \u12ED \u1218\u1235\u121D\u130C\u127B\u121D\u127D\u1295 \u130B\u1293 \u1275\u12B3\u1295\u120B \u121D\u122A\u1275\u121D\u127D\u1295\u12A0\u1295\u12ED \u1260\u130A\u1272 \u1235\u1295 \u1343\u120B\u121D \u1218\u123B\u127D\u1295 \u1272\u1228\u12CB\u12CD \u12ED \u12AD\u121B\u120B\u121D\u1295 \u134D\u1295\u120B \u121B\u12AD\u120D \u1290\u121B\u127D \u130B\u1270\u121D\u127B \u12ED \u1235\u121D\u130C\u127B\u121D\u127D\u1295 \u130B\u1293 \u1343\u120B\u121D \u1218\u123B\u127D\u1295"
        ))

        // Debre Zeit (Mount of Olives/Palm Sunday) - 7 days before Fasika
        val debreZeit = addDays(fasika, -7)
        feasts.add(MobileFeast(
            nameEn = "Debre Zeit (Palm Sunday)",
            nameAm = "\u12B3\u121B\u1265 \u132B\u12CB\u1270",
            etDate = debreZeit,
            gcDate = EthiopianCalendar.ethiopianToGregorian(debreZeit.year, debreZeit.month, debreZeit.day),
            descriptionEn = "Debre Zeit, known as Palm Sunday in the Western tradition, commemorates Jesus Christ's triumphal entry into Jerusalem. The name means 'Mount of Olives' in Ge'ez. Orthodox faithful weave palm fronds into crosses and rings, and processions are held with singing and hymns.",
            descriptionAm = "\u12B3\u121B\u1265 \u132B\u12CB\u1270 \u12A0\u123B\u127B \u12A0\u1265\u12CB\u1270 \u1270\u122D\u1275 \u12ED \u12A0\u1275\u121B\u122A \u128D\u12D8\u1272 \u1260\u123B\u121D \u12ED \u12A0\u1265\u12CB\u1270\u127D\u1295\u12A0 \u12ED \u12A0\u1265\u12A0\u1272\u122B\u120B\u121D \u1272\u1295\u1235\u121B\u1293\u121D \u12ED \u1292\u121B\u1233\u120B\u121D\u127D \u12ED \u12A0\u1265\u12CB\u1270\u127D \u1260\u1293\u121D\u1295\u12A0\u1295\u12ED \u1272\u1228\u12CB\u12CD\u1270 \u121D\u130A\u1235 \u12A0\u1265\u12CB\u1270\u127D\u1295\u12A0\u1295\u12ED \u12A0\u1265\u12CB\u1270\u127D \u130B\u1293 \u1260\u130A\u1272 \u1272\u1228\u12CB\u12CD\u12ED \u12AD\u121B\u120B\u121D\u1295\u12ED \u134D\u1295\u120B \u121B\u12AD\u120D \u1290\u121B\u127D \u130B\u1270\u121D\u127B \u12ED \u1260\u130A\u1272 \u1235\u1295 \u12A0\u1233\u130B\u127B\u121D\u127D\u1295\u12A0 \u12ED \u12A0\u1233\u121D\u120B\u1270\u127B \u12A0\u1260\u122D\u12CB \u12ED \u12A0\u1260\u121B\u1228\u1275\u12A0\u1295\u12ED \u12A0\u1265\u12CB\u1270\u127D \u12ED \u1290\u121B\u127D \u130B\u1270\u121D\u127B \u12ED \u12A0\u1260\u121B\u1228\u1275\u12A0\u1295\u12ED \u12A0\u1265\u12CB\u1270\u127D \u12ED \u12A0\u1260\u121B\u1228\u1275\u12A0\u1295\u12ED"
        ))

        // Fasika itself
        feasts.add(MobileFeast(
            nameEn = "Fasika (Ethiopian Easter)",
            nameAm = "\u134D\u121D\u12CB\u123B",
            etDate = fasika,
            gcDate = EthiopianCalendar.ethiopianToGregorian(fasika.year, fasika.month, fasika.day),
            descriptionEn = "Fasika is the most important feast in the Ethiopian Orthodox Church, celebrating the resurrection of Jesus Christ. It follows a 55-day fast (Hudadi/Tsome Abiy Tsome). The date is calculated each year using the ancient Bahra Hasab computus. Families break the fast with Doro Wat (chicken stew) and celebrate with church services, processions, and feasting.",
            descriptionAm = "\u134D\u121D\u12CB\u123B \u12A0\u1260\u1295\u121B\u1293\u121D \u1270\u122D\u1275 \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1272\u1295\u1235\u121B\u1293\u121D \u1260\u123B\u121D \u12ED \u12A0\u1265\u12A0\u1272\u122B\u120B\u121D\u1295\u12A0\u1295 \u12ED \u12A0\u1265\u12CB\u1270 \u1260\u1272 \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1272\u1295\u1235\u121B\u1293\u121D \u1260\u123B\u121D \u12ED \u12A0\u1265\u12A0\u1272\u122B\u120B\u121D\u1295\u12A0\u1295 \u12ED \u12A0\u1265\u12CB\u1270 \u1260\u1272 \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D"
        ))

        // Yohannes Matmeq (Baptism of St. John) - traditionally associated with Fasika period
        val yohannesMatmeq = addDays(fasika, 35)
        feasts.add(MobileFeast(
            nameEn = "Yohannes Matmeq",
            nameAm = "\u12B8\u1214\u120B\u122D\u121B\u1295 \u121B\u1275\u130B\u1265\u129B",
            etDate = yohannesMatmeq,
            gcDate = EthiopianCalendar.ethiopianToGregorian(yohannesMatmeq.year, yohannesMatmeq.month, yohannesMatmeq.day),
            descriptionEn = "Yohannes Matmeq celebrates the baptism of St. John the Baptist. It is observed in the period following Fasika and is marked by special church services and religious ceremonies.",
            descriptionAm = "\u12B8\u1214\u120B\u122D\u121B\u1295 \u121B\u1275\u130B\u1265\u129B \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u12A0\u1265\u12CB\u1270 \u1260\u1272 \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D"
        ))

        // Tsome Dib'ua (Apostles' Fast) - starts ~50 days after Fasika (Pentecost + 1)
        val tsomeDibua = addDays(fasika, 50)
        feasts.add(MobileFeast(
            nameEn = "Apostles' Fast (Start)",
            nameAm = "\u1335\u123B\u121B \u12B3\u121B\u1260\u12CB\u121B",
            etDate = tsomeDibua,
            gcDate = EthiopianCalendar.ethiopianToGregorian(tsomeDibua.year, tsomeDibua.month, tsomeDibua.day),
            descriptionEn = "The Apostles' Fast (Tsome Dib'ua) begins after Pentecost and lasts until the feast of SS. Peter and Paul. It honors the apostles who fasted after receiving the Holy Spirit. The duration varies each year depending on the date of Fasika.",
            descriptionAm = "\u1335\u123B\u121B \u12B3\u121B\u1260\u12CB\u121B \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1272\u1295\u1235\u121B\u1293\u121D \u1260\u123B\u121D \u12ED \u12A0\u1265\u12A0\u1272\u122B\u120B\u121D\u1295\u12A0\u1295 \u12ED \u12A0\u1265\u12CB\u1270 \u1260\u1272 \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D"
        ))

        return feasts
    }
}
