package com.surafel.zemen.calendar

/**
 * Pure Kotlin Ethiopian Calendar conversion engine.
 * Zero Android framework dependencies for testability.
 *
 * Ethiopian Calendar:
 * - 13 months: 12 months of 30 days + Pagume (5 or 6 days)
 * - Leap year every 4 years (year before Gregorian leap year)
 * - Week starts on Sunday
 */
object EthiopianCalendar {

    /** Ethiopian month names in Amharic */
    val MONTHS_AMHARIC = arrayOf(
        "\u12E1\u122D\u1225\u120B\u121D", // Meskerem
        "\u1262\u1218\u134D\u1208\u120B", // Tikimt
        "\u134D\u1265\u1228\u1270",         // Hidar
        "\u1273\u12CE\u121B",               // Tahsas
        "\u12F3\u122B",                     // Tir
        "\u133E\u122B\u128D\u12D8\u122A",   // Yekatit
        "\u121B\u1308\u1262\u1272",         // Megabit
        "\u121B\u12EB\u121B",               // Miazia
        "\u130D\u1295\u1262",               // Ginbot
        "\u1230\u1295",                     // Sene
        "\u128C\u121B",                     // Hamle
        "\u1290\u1265\u121B",               // Nehase
        "\u1343\u1308\u1335\u121B"          // Pagume
    )

    /** Ethiopian month names in English */
    val MONTHS_ENGLISH = arrayOf(
        "Meskerem", "Tikimt", "Hidar", "Tahsas",
        "Tir", "Yekatit", "Megabit", "Miazia",
        "Ginbot", "Sene", "Hamle", "Nehase", "Pagume"
    )

    /** Day of week names in Amharic (short) - Sunday first */
    val DAYS_AMHARIC = arrayOf(
        "\u12A5\u1215\u1233",   // Ehud (Sun)
        "\u1233\u120B",          // Segno (Mon)
        "\u121B\u1228\u1272",    // Maksegno (Tue)
        "\u1228\u121B",          // Rob (Wed)
        "\u12C8\u121B",          // Hamus (Thu)
        "\u12A0\u1265\u1261",    // Arb (Fri)
        "\u1275\u12B3"           // Kidame (Sat)
    )

    /** Day of week names in English (short) - Sunday first */
    val DAYS_ENGLISH = arrayOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")

    /** Season data: name, months covered (1-indexed) */
    data class Season(
        val nameEn: String,
        val nameAm: String,
        val emoji: String,
        val startMonth: Int,
        val endMonth: Int
    )

    val SEASONS = listOf(
        Season("Kiremt", "\u12AD\u1228\u121D\u1275", "\uD83C\uDF27\uFE0F", 10, 1),  // Oct-Jan (Hidar-Tir)
        Season("Tsedey", "\u1339\u1265\u12CB", "\uD83C\uDF3E\uFE0F", 2, 5),        // Feb-May (Yekatit-Ginbot)
        Season("Bega", "\u121A\u130B", "\u2600\uFE0F", 6, 9),                       // Jun-Sep (Sene-Nehase)
        Season("Belg", "\u121A\u120D\u130B", "\uD83C\uDF3C\uFE0F", 10, 12)           // (Overlap for crop cycle)
    )

    /** Re-order seasons per design doc:
     *  Kiremt (June-Sep = Hidar? No -- let me re-read the doc:
     *  Kiremt: June-September (rainy)  -> Ethiopian months ~Sene 10- Nehase 13? No.
     *  The doc says Gregorian months. Kiremt = Jun-Sep.
     *  Let's map: Ethiopian month of Kiremt overlaps Sene through Meskerem roughly.
     *  Per the doc the seasons are listed with Gregorian months.
     *  For season detection from Ethiopian month, we use approximate mapping:
     *    Kiremt: Sene(10) - Nehase(13) + Meskerem(1) early
     *    Tsedey: Meskerem(1) - Tahsas(4)
     *    Bega: Tahsas(4) - Megabit(7)
     *    Belg: Miazia(8) - Sene(10)
     *
     *  Actually, the Ethiopian seasons align roughly:
     *    Kiremt (rainy): Ethiopian Sene(10) - Meskerem(1)
     *    Metsew (autumn): Meskerem(1) - Hidar(3)
     *    Bega (dry): Tahsas(4) - Megabit(7)
     *    Belg (spring): Miazia(8) - Sene(10)
     *
     *  But the doc uses:
     *    Kiremt: June-Sept
     *    Tsedey: Sept-Dec
     *    Bega: Dec-March
     *    Belg: March-June
     *
     *  We'll use Ethiopian month ranges for in-app detection:
     */
    val SEASON_MAP = listOf(
        Season("Kiremt", "\u12AD\u1228\u121D\u1275", "\uD83C\uDF27\uFE0F", 10, 1),
        Season("Tsedey", "\u1339\u1265\u12CB", "\uD83C\uDF3E\uFE0F", 2, 4),
        Season("Bega", "\u121A\u130B", "\u2600\uFE0F", 5, 7),
        Season("Belg", "\u121A\u120D\u130B", "\uD83C\uDF3C\uFE0F", 8, 9)
    )

    data class EthiopianDate(
        val year: Int,
        val month: Int,  // 1-13
        val day: Int     // 1-30 (or 1-6 for Pagume)
    )

    data class GregorianDate(
        val year: Int,
        val month: Int,  // 1-12
        val day: Int
    )

    /** Check if Ethiopian year is a leap year (Pagume has 6 days) */
    fun isEthiopianLeapYear(etYear: Int): Boolean {
        return etYear % 4 == 3
    }

    /** Get number of days in an Ethiopian month */
    fun daysInMonth(etYear: Int, etMonth: Int): Int {
        return if (etMonth == 13) {
            if (isEthiopianLeapYear(etYear)) 6 else 5
        } else {
            30
        }
    }

    /**
     * Ethiopian date -> Julian Day Number
     * JDN = 1723856 + 365*(etYear-1) + floor(etYear/4) + 30*(etMonth-1) + etDay - 1
     */
    fun ethiopianToJdn(etYear: Int, etMonth: Int, etDay: Int): Long {
        val n = etYear - 1
        return 1723856L + 365L * n + (n / 4) + 30L * (etMonth - 1) + etDay - 1
    }

    /**
     * Julian Day Number -> Gregorian date
     * Standard JDN to Gregorian conversion algorithm
     */
    fun jdnToGregorian(jdn: Long): GregorianDate {
        var j = jdn
        val f = j + 1401 + (((4 * j + 274277) / 146097) * 3) / 4 - 38
        val e = 4 * f + 3
        val g = (e % 1461) / 4
        val h = 5 * g + 2
        val day = ((h % 153) / 5) + 1
        val month = ((h / 153 + 2) % 12) + 1
        val year = e / 1461 - 4716 + ((12 + 2 - month) / 12)
        return GregorianDate(year.toInt(), month, day.toInt())
    }

    /** Ethiopian date -> Gregorian date */
    fun ethiopianToGregorian(etYear: Int, etMonth: Int, etDay: Int): GregorianDate {
        val jdn = ethiopianToJdn(etYear, etMonth, etDay)
        return jdnToGregorian(jdn)
    }

    /**
     * Gregorian date -> Ethiopian date
     * Uses reverse JDN lookup
     */
    fun gregorianToJdn(gYear: Int, gMonth: Int, gDay: Int): Long {
        val a = (14 - gMonth) / 12
        val y = gYear + 4800 - a
        val m = gMonth + 12 * a - 3
        return gDay.toLong() + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400 - 32045
    }

    fun gregorianToEthiopian(gYear: Int, gMonth: Int, gDay: Int): EthiopianDate {
        val jdn = gregorianToJdn(gYear, gMonth, gDay)
        return jdnToEthiopian(jdn)
    }

    /** Julian Day Number -> Ethiopian date */
    fun jdnToEthiopian(jdn: Long): EthiopianDate {
        var j = jdn
        val r = (j - 1723856) % 1461
        val n = (j - 1723856) / 1461
        val year = 4 * n + r / 365
        val dayOfYear = (r % 365).toInt() + 1
        val month = if (dayOfYear <= 360) {
            ((dayOfYear - 1) / 30) + 1
        } else {
            13 // Pagume
        }
        val day = if (month == 13) {
            dayOfYear - 360
        } else {
            ((dayOfYear - 1) % 30) + 1
        }
        return EthiopianDate(year.toInt(), month, day)
    }

    /**
     * Day of week for Ethiopian date.
     * Returns 0 = Sunday, 1 = Monday, ..., 6 = Saturday
     */
    fun dayOfWeek(etYear: Int, etMonth: Int, etDay: Int): Int {
        val jdn = ethiopianToJdn(etYear, etMonth, etDay)
        return ((jdn + 1) % 7).toInt()  // JDN mod 7: 0=Monday, so +1 -> 0=Sunday
    }

    /** Get the current Ethiopian date from system time */
    fun today(): EthiopianDate {
        val calendar = java.util.Calendar.getInstance()
        val gYear = calendar.get(java.util.Calendar.YEAR)
        val gMonth = calendar.get(java.util.Calendar.MONTH) + 1
        val gDay = calendar.get(java.util.Calendar.DAY_OF_MONTH)
        return gregorianToEthiopian(gYear, gMonth, gDay)
    }

    /** Get season for an Ethiopian month (1-12; Pagume=13 has no season) */
    fun getSeason(etMonth: Int): Season? {
        if (etMonth == 13) return null
        return SEASON_MAP.find {
            if (it.startMonth <= it.endMonth) {
                etMonth in it.startMonth..it.endMonth
            } else {
                etMonth >= it.startMonth || etMonth <= it.endMonth
            }
        }
    }

    /** Get month name in the given language */
    fun getMonthName(month: Int, isAmharic: Boolean): String {
        return if (isAmharic) MONTHS_AMHARIC[month - 1] else MONTHS_ENGLISH[month - 1]
    }

    /** Get day of week short name */
    fun getDayOfWeekName(dayOfWeek: Int, isAmharic: Boolean): String {
        return if (isAmharic) DAYS_AMHARIC[dayOfWeek] else DAYS_ENGLISH[dayOfWeek]
    }
}
