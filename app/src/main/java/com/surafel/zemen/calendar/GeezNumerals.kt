package com.surafel.zemen.calendar

/**
 * Geez numeral lookup table.
 * Geez numerals use the Ge'ez script to represent numbers 1-30+ and years.
 * Rendered via Abyssinica SIL font.
 */
object GeezNumerals {

    /** Single digit Geez numerals: 1-9 */
    private val UNITS = mapOf(
        1 to "\u1369",  // ፩
        2 to "\u136A",  // ፪
        3 to "\u136B",  // ፫
        4 to "\u136C",  // ፬
        5 to "\u136D",  // ፭
        6 to "\u136E",  // ፮
        7 to "\u136F",  // ፯
        8 to "\u1370",  // ፰
        9 to "\u1371"   // ፱
    )

    /** Tens Geez numerals: 10, 20, 30 */
    private val TENS = mapOf(
        10 to "\u1372", // ፲
        20 to "\u1373", // ፳
        30 to "\u1374", // ፴
        40 to "\u1375", // ፵
        50 to "\u1376", // ፶
        60 to "\u1377", // ፷
        70 to "\u1378", // ፸
        80 to "\u1379", // ፹
        90 to "\u137A"  // ፺
    )

    /** Hundreds: 100 */
    private val HUNDRED = "\u137B" // ፻

    /** Thousands: 10000 */
    private val TEN_THOUSAND = "\u137C" // ፼

    /**
     * Convert a Western number to Geez numeral string.
     * Supports values 1 through 9999 (sufficient for calendar days and years).
     */
    fun toGeez(number: Int): String {
        require(number >= 1) { "Geez numerals start from 1" }

        if (number <= 9) return UNITS[number] ?: error("Unexpected")
        if (number in 10..99) {
            val ten = (number / 10) * 10
            val unit = number % 10
            val result = StringBuilder()
            result.append(TENS[ten] ?: error("Unexpected tens: $ten"))
            if (unit > 0) result.append(UNITS[unit] ?: error("Unexpected unit: $unit"))
            return result.toString()
        }
        if (number in 100..999) {
            val hundred = number / 100
            val remainder = number % 100
            val result = StringBuilder()
            if (hundred == 1) {
                result.append(HUNDRED)
            } else {
                result.append(toGeez(hundred))
                result.append(HUNDRED)
            }
            if (remainder > 0) result.append(toGeez(remainder))
            return result.toString()
        }
        // 1000-9999
        val thousands = number / 100
        val remainder = number % 100
        val result = StringBuilder()
        result.append(toGeez(thousands))
        result.append(TEN_THOUSAND)
        if (remainder > 0) result.append(toGeez(remainder))
        return result.toString()
    }

    /** Check if a string is a Geez numeral */
    fun isGeez(text: String): Boolean {
        return text.any { it.code in 0x1369..0x137C }
    }
}
