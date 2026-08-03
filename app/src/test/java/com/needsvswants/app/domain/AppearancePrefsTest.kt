package com.needsvswants.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppearancePrefsTest {
    @Test
    fun fontMultipliers() {
        assertEquals(1.00f, FontScaleStep.DEFAULT.multiplier)
        assertEquals(1.15f, FontScaleStep.LARGE.multiplier)
        assertEquals(1.30f, FontScaleStep.EXTRA_LARGE.multiplier)
    }

    @Test
    fun parseUnknownFallsBack() {
        assertEquals(ThemeId.MARKET_LIGHT, ThemeId.fromStorage(null))
        assertEquals(ThemeId.MARKET_LIGHT, ThemeId.fromStorage("nope"))
        assertEquals(FontScaleStep.DEFAULT, FontScaleStep.fromStorage("x"))
        assertEquals(ThemeId.MARKET_DARK, ThemeId.fromStorage("market_dark"))
        assertEquals(FontScaleStep.EXTRA_LARGE, FontScaleStep.fromStorage("extra_large"))
    }

    @Test
    fun resolveDark() {
        assertFalse(ThemeId.MARKET_LIGHT.resolveIsDark(systemDark = true))
        assertTrue(ThemeId.MARKET_DARK.resolveIsDark(systemDark = false))
        assertTrue(ThemeId.SYSTEM.resolveIsDark(systemDark = true))
        assertFalse(ThemeId.SYSTEM.resolveIsDark(systemDark = false))
        assertFalse(ThemeId.HIGH_CONTRAST.resolveIsDark(systemDark = true))
    }
}
