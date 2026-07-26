package com.surafel.zemen.calendar

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for BahraHasab computus engine.
 * Tests Evangelist cycle, Fasika date calculation, and mobile feast derivation.
 */
class BahraHasabTest {

    // ─── Evangelist Cycle ────────────────────────────────────────────────

    @Test
    fun `Year 2015 Evangelist is John`() {
        val evangelist = BahraHasab.getEvangelist(2015)
        assertEquals(BahraHasab.Evangelist.JOHN, evangelist)
    }

    @Test
    fun `Year 2016 Evangelist is Matthew`() {
        val evangelist = BahraHasab.getEvangelist(2016)
        assertEquals(BahraHasab.Evangelist.MATTHEW, evangelist)
    }

    @Test
    fun `Year 2017 Evangelist is Mark`() {
        val evangelist = BahraHasab.getEvangelist(2017)
        assertEquals(BahraHasab.Evangelist.MARK, evangelist)
    }

    @Test
    fun `Year 2018 Evangelist is Luke`() {
        val evangelist = BahraHasab.getEvangelist(2018)
        assertEquals(BahraHasab.Evangelist.LUKE, evangelist)
    }

    @Test
    fun `Evangelist cycle repeats every 4 years`() {
        val ev1 = BahraHasab.getEvangelist(2015)
        val ev2 = BahraHasab.getEvangelist(2019)
        assertEquals(ev1, ev2)
    }

    @Test
    fun `All 4 evangelists appear in a 4-year cycle`() {
        val evangelists = (2015..2018).map { BahraHasab.getEvangelist(it) }.toSet()
        assertEquals(4, evangelists.size)
        assertTrue(evangelists.contains(BahraHasab.Evangelist.JOHN))
        assertTrue(evangelists.contains(BahraHasab.Evangelist.MATTHEW))
        assertTrue(evangelists.contains(BahraHasab.Evangelist.MARK))
        assertTrue(evangelists.contains(BahraHasab.Evangelist.LUKE))
    }

    // ─── Fasika Date Calculation ────────────────────────────────────────

    @Test
    fun `Fasika 2017 is in Megabit (month 7)`() {
        val fasika = BahraHasab.calculateFasika(2017)
        assertEquals(7, fasika.month) // Megabit
        assertTrue(fasika.day in 1..30)
    }

    @Test
    fun `Fasika 2015 is in Megabit (month 7)`() {
        val fasika = BahraHasab.calculateFasika(2015)
        assertEquals(7, fasika.month) // Megabit
        assertTrue(fasika.day in 1..30)
    }

    @Test
    fun `Fasika always falls between Megabit 1-30`() {
        for (year in 2010..2050) {
            val fasika = BahraHasab.calculateFasika(year)
            assertEquals(7, fasika.month)
            assertTrue(fasika.day in 1..30)
        }
    }

    @Test
    fun `Fasika GC date falls between March 22 and April 25`() {
        for (year in 2010..2050) {
            val fasika = BahraHasab.calculateFasika(year)
            val gc = EthiopianCalendar.ethiopianToGregorian(fasika.year, fasika.month, fasika.day)
            assertTrue(
                "Fasika $year GC: ${gc.month}/${gc.day} outside Easter range",
                (gc.month == 3 && gc.day >= 22) || (gc.month == 4 && gc.day <= 25)
            )
        }
    }

    @Test
    fun `Fasika 2017 GC date is in April`() {
        val fasika = BahraHasab.calculateFasika(2017)
        val gc = EthiopianCalendar.ethiopianToGregorian(fasika.year, fasika.month, fasika.day)
        assertEquals(2025, gc.year)
        assertEquals(4, gc.month) // April
        assertTrue(gc.day in 13..20) // Approximate range for 2025 Easter
    }

    // ─── Full Computation ───────────────────────────────────────────────

    @Test
    fun `Full computation returns evangelist and fasika date`() {
        val result = BahraHasab.compute(2017)
        assertEquals(BahraHasab.Evangelist.MARK, result.evangelist)
        assertEquals(7, result.fasikaDate.month)
        assertNotNull(result.fasikaGregorian)
        assertTrue(result.mobileFeasts.isNotEmpty())
    }

    @Test
    fun `Full computation includes at least 5 mobile feasts`() {
        val result = BahraHasab.compute(2017)
        assertTrue(result.mobileFeasts.size >= 5)
    }

    @Test
    fun `Full computation includes Fasika in mobile feasts`() {
        val result = BahraHasab.compute(2017)
        val fasika = result.mobileFeasts.find { it.nameEn.contains("Fasika") }
        assertNotNull(fasika)
    }

    @Test
    fun `Full computation includes Fast of the Prophets`() {
        val result = BahraHasab.compute(2017)
        val tsome = result.mobileFeasts.find { it.nameEn.contains("Prophets") }
        assertNotNull(tsome)
    }

    @Test
    fun `Full computation includes Palm Sunday`() {
        val result = BahraHasab.compute(2017)
        val palmSunday = result.mobileFeasts.find { it.nameEn.contains("Palm Sunday") }
        assertNotNull(palmSunday)
    }

    @Test
    fun `Fast of Prophets starts before Fasika`() {
        val result = BahraHasab.compute(2017)
        val tsome = result.mobileFeasts.find { it.nameEn.contains("Prophets") }!!
        val fasika = result.mobileFeasts.find { it.nameEn.contains("Fasika") }!!
        val tsomeJdn = EthiopianCalendar.ethiopianToJdn(tsome.etDate.year, tsome.etDate.month, tsome.etDate.day)
        val fasikaJdn = EthiopianCalendar.ethiopianToJdn(fasika.etDate.year, fasika.etDate.month, fasika.etDate.day)
        assertTrue(tsomeJdn < fasikaJdn)
    }

    @Test
    fun `Palm Sunday is 7 days before Fasika`() {
        val result = BahraHasab.compute(2017)
        val palmSunday = result.mobileFeasts.find { it.nameEn.contains("Palm Sunday") }!!
        val fasika = result.mobileFeasts.find { it.nameEn.contains("Fasika") }!!
        val psJdn = EthiopianCalendar.ethiopianToJdn(palmSunday.etDate.year, palmSunday.etDate.month, palmSunday.etDate.day)
        val fasikaJdn = EthiopianCalendar.ethiopianToJdn(fasika.etDate.year, fasika.etDate.month, fasika.etDate.day)
        assertEquals(7, fasikaJdn - psJdn)
    }

    @Test
    fun `Mobile feast dates have valid Ethiopian calendar dates`() {
        val result = BahraHasab.compute(2017)
        for (feast in result.mobileFeasts) {
            assertTrue(feast.etDate.month in 1..13)
            assertTrue(feast.etDate.day in 1..30)
        }
    }

    @Test
    fun `Multiple years computation is consistent`() {
        for (year in 2015..2018) {
            val result = BahraHasab.compute(year)
            assertNotNull(result.evangelist)
            assertTrue(result.fasikaDate.month in 1..13)
            assertTrue(result.mobileFeasts.isNotEmpty())
        }
    }

    // ─── Evangelist Properties ───────────────────────────────────────────

    @Test
    fun `Evangelist has both English and Amharic names`() {
        BahraHasab.Evangelist.entries.forEach { ev ->
            assertTrue(ev.nameEn.isNotEmpty())
            assertTrue(ev.nameAm.isNotEmpty())
        }
    }

    @Test
    fun `Evangelist has exactly 4 entries`() {
        assertEquals(4, BahraHasab.Evangelist.entries.size)
    }
}
