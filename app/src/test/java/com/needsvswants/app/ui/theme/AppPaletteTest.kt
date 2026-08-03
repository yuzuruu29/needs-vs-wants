package com.needsvswants.app.ui.theme

import androidx.compose.ui.graphics.Color
import com.needsvswants.app.domain.ThemeId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppPaletteTest {
    @Test
    fun palettesDistinct() {
        val light = AppPalette.marketLight()
        val dark = AppPalette.marketDark()
        val hc = AppPalette.highContrast()
        assertNotEquals(light.background, dark.background)
        assertEquals(Color(0xFF000000), hc.textPrimary)
        assertNotEquals(light.need, light.want)
        assertTrue(light.isLightStatusBars)
        assertFalse(dark.isLightStatusBars)
        assertTrue(hc.isLightStatusBars)
    }

    @Test
    fun forThemeResolves() {
        assertEquals(
            AppPalette.marketLight().background,
            AppPalette.forTheme(ThemeId.MARKET_LIGHT, systemDark = true).background
        )
        assertEquals(
            AppPalette.marketDark().background,
            AppPalette.forTheme(ThemeId.MARKET_DARK, systemDark = false).background
        )
        assertEquals(
            AppPalette.marketDark().background,
            AppPalette.forTheme(ThemeId.SYSTEM, systemDark = true).background
        )
        assertEquals(
            AppPalette.highContrast().textPrimary,
            AppPalette.forTheme(ThemeId.HIGH_CONTRAST, systemDark = true).textPrimary
        )
    }
}
