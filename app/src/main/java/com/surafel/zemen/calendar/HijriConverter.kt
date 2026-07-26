package com.surafel.zemen.calendar

import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import com.github.msarhan.ummalqura.calendar.UmmalquraDateFormat
import java.util.*

/**
 * Hijri calendar converter using the Umm al-Qura calendar library.
 * Computes Islamic holiday dates for a given Gregorian year and converts
 * them to Ethiopian dates.
 *
 * The conversion chain: Hijri -> Gregorian -> Ethiopian
 */
object HijriConverter {

    data class IslamicHoliday(
        val id: String,
        val nameEn: String,
        val nameAm: String,
        val shortDescEn: String,
        val shortDescAm: String,
        val longDescEn: String,
        val longDescAm: String,
        val emoji: String,
        /** Hijri month (1-12) */
        val hijriMonth: Int,
        /** Hijri day */
        val hijriDay: Int,
        /** If true, this marks a period start, not a single day */
        val isPeriodStart: Boolean = false
    )

    /** All Islamic holidays observed in Ethiopia */
    val ISLAMIC_HOLIDAYS = listOf(
        IslamicHoliday(
            id = "eid_al_fitr",
            nameEn = "Eid al-Fitr",
            nameAm = "\u12A2\u12F3 \u12A0\u120D\u134D\u1275\u121B",
            shortDescEn = "End of Ramadan",
            shortDescAm = "\u1288\u121B\u121D\u1240\u1295\u129B \u12A0\u1295\u12CD\u1270",
            longDescEn = "Eid al-Fitr marks the end of the holy month of Ramadan. It is one of the two most important festivals in Islam. Ethiopian Muslims celebrate with special morning prayers at mosques, family gatherings, feasting, charity (Zakat al-Fitr), and exchanging greetings. Traditional dishes such as injera with various wots, doro tibs, and sweet beverages are shared among families and neighbors. The day is a public holiday in Ethiopia.",
            longDescAm = "\u12A2\u12F3 \u12A0\u120D\u134D\u1275\u121B \u1288\u121B\u121D\u1240\u1295\u129B\u121D \u12A0\u1295\u12CD\u1270 \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1290\u121B\u1270\u127D\u1295 \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1272\u1295\u1235\u121B\u1293\u121D \u1260\u123B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D",
            emoji = "\uD83C\uDF8E",
            hijriMonth = 10,
            hijriDay = 1
        ),
        IslamicHoliday(
            id = "eid_al_adha",
            nameEn = "Eid al-Adha",
            nameAm = "\u12A2\u12F3 \u12A0\u120D\u12A0\u12F3",
            shortDescEn = "Feast of Sacrifice",
            shortDescAm = "\u1290\u121B\u1270 \u12A0\u1235\u12F2\u1272\u1275\u129B",
            longDescEn = "Eid al-Adha, the Festival of Sacrifice, commemorates the willingness of Ibrahim (Abraham) to sacrifice his son as an act of obedience to God. Ethiopian Muslims celebrate by performing the Eid prayer, sacrificing livestock (usually sheep or goat), and distributing meat to family, friends, and the poor. It is a time of communal gathering, reflection, and generosity. The holiday lasts for three days.",
            longDescAm = "\u12A2\u12F3 \u12A0\u120D\u12A0\u12F3 \u1290\u121B\u1270 \u12A0\u1235\u12F2\u1272\u1275\u129B \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1290\u121B\u1270\u127D\u1295 \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1272\u1295\u1235\u121B\u1293\u121D \u1260\u123B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D",
            emoji = "\uD83E\uDDAD",
            hijriMonth = 12,
            hijriDay = 10
        ),
        IslamicHoliday(
            id = "mawlid_al_nabi",
            nameEn = "Mawlid al-Nabi",
            nameAm = "\u121B\u1325\u120D\u12CB\u1295",
            shortDescEn = "Prophet Muhammad's Birthday",
            shortDescAm = "\u1290\u1293\u121B\u121D \u121B\u1325\u121B\u121D\u1295 \u1295\u121B\u12EB\u1270",
            longDescEn = "Mawlid al-Nabi celebrates the birthday of the Prophet Muhammad (peace be upon him). In Ethiopia, it is observed with special prayers at mosques, recitation of religious poetry (Madih), and community gatherings. Many Ethiopian Muslims also give to charity and share food with neighbors. The celebration reflects the deep Islamic heritage of Ethiopia, particularly in regions like Harar, Dire Dawa, and the Somali Regional State.",
            longDescAm = "\u121B\u1325\u120D\u12CB\u1295 \u1290\u1293\u121B\u121D \u121B\u1325\u121B\u121D\u1295 \u1295\u121B\u12EB\u1270 \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1290\u121B\u1270\u127D\u1295 \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1272\u1295\u1235\u121B\u1293\u121D \u1260\u123B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D",
            emoji = "\uD83D\uDD4C",
            hijriMonth = 3,
            hijriDay = 12
        ),
        IslamicHoliday(
            id = "ramadan_start",
            nameEn = "Ramadan Start",
            nameAm = "\u1288\u121B\u121D\u1240\u1295\u129B",
            shortDescEn = "Holy month of fasting begins",
            shortDescAm = "\u1288\u121B\u121D\u1240\u1295\u129B \u12A0\u1295\u12CD\u1270",
            longDescEn = "Ramadan is the ninth month of the Islamic calendar and the holiest month in Islam. Ethiopian Muslims observe dawn-to-dusk fasting, increased prayer, charity, and spiritual reflection. The fast is broken each evening with Iftar meals shared with family and community. Ethiopian Iftar typically includes dates, water, soup, and traditional dishes. The spiritual atmosphere intensifies during the last ten days of Ramadan.",
            longDescAm = "\u1288\u121B\u121D\u1240\u1295\u129B \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1290\u121B\u1270\u127D\u1295 \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1272\u1295\u1235\u121B\u1293\u121D \u1260\u123B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D",
            emoji = "\uD83C\uDF1F",
            hijriMonth = 9,
            hijriDay = 1,
            isPeriodStart = true
        ),
        IslamicHoliday(
            id = "isra_miraj",
            nameEn = "Isra and Mi'raj",
            nameAm = "\u12A5\u123B\u1288\u12D0 \u121B\u12CB\u1288\u121B\u130B",
            shortDescEn = "Night Journey and Ascension",
            shortDescAm = "\u1290\u121B\u1272 \u1295\u1293\u1235\u1293\u1270",
            longDescEn = "Isra and Mi'raj commemorates the Prophet Muhammad's night journey from Mecca to Jerusalem and his ascension to heaven. It is observed on the 27th of Rajab. Ethiopian Muslims mark the occasion with special night prayers (Qiyam al-Layl), recitation of the Quran, and spiritual gatherings at mosques.",
            longDescAm = "\u12A5\u123B\u1288\u12D0 \u121B\u12CB\u1288\u121B\u130B \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1290\u121B\u1270\u127D\u1295 \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1272\u1295\u1235\u121B\u1293\u121D \u1260\u123B\u121D \u12ED \u12A0\u1260\u120D\u130B\u121D\u1275\u1293 \u1275\u121B\u127B\u120B\u121D \u12ED \u1275\u1272\u1308\u120B\u121D",
            emoji = "\uD83C\uDF19",
            hijriMonth = 7,
            hijriDay = 27
        )
    )

    /**
     * Get Islamic holidays for a given Gregorian year.
     * Computes the Hijri date, converts to Gregorian, then to Ethiopian.
     *
     * @param gYear Gregorian year
     * @return List of computed Islamic holidays with both Hijri, Gregorian, and Ethiopian dates
     */
    fun getIslamicHolidaysForYear(gYear: Int): List<com.surafel.zemen.data.Holiday> {
        val holidays = mutableListOf<com.surafel.zemen.data.Holiday>()

        for (islamicHoliday in ISLAMIC_HOLIDAYS) {
            try {
                // Create Umm al-Qura calendar date
                val hijriCal = UmmalquraCalendar()
                hijriCal.set(java.util.Calendar.YEAR, gYear)
                hijriCal.set(java.util.Calendar.MONTH, islamicHoliday.hijriMonth - 1)
                hijriCal.set(java.util.Calendar.DAY_OF_MONTH, islamicHoliday.hijriDay)

                // Convert Hijri to Gregorian
                val gcMonth = hijriCal.get(java.util.Calendar.MONTH) + 1
                val gcDay = hijriCal.get(java.util.Calendar.DAY_OF_MONTH)
                val gcYear = hijriCal.get(java.util.Calendar.YEAR)

                // Convert Gregorian to Ethiopian
                val etDate = EthiopianCalendar.gregorianToEthiopian(gcYear, gcMonth, gcDay)

                holidays.add(
                    com.surafel.zemen.data.Holiday(
                        id = islamicHoliday.id,
                        tier = com.surafel.zemen.data.HolidayTier.ISLAMIC,
                        etMonth = etDate.month,
                        etDay = etDate.day,
                        nameEn = islamicHoliday.nameEn,
                        nameAm = islamicHoliday.nameAm,
                        shortDescEn = islamicHoliday.shortDescEn,
                        shortDescAm = islamicHoliday.shortDescAm,
                        longDescEn = islamicHoliday.longDescEn,
                        longDescAm = islamicHoliday.longDescAm,
                        emoji = islamicHoliday.emoji,
                        isMobile = true
                    )
                )
            } catch (e: Exception) {
                // If the date doesn't exist in this year's Hijri calendar, skip
                continue
            }
        }

        return holidays
    }
}
