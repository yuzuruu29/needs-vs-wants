package com.needsvswants.app.ui.theme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppSfxTest {

    @Test
    fun creditsNameAuthorAndLicense() {
        assertEquals("UI Audio", SfxCredits.PACK_NAME)
        assertEquals("Kenney Vleugels", SfxCredits.AUTHOR)
        assertTrue(SfxCredits.SITE.contains("kenney.nl"))
        assertTrue(SfxCredits.LICENSE.contains("CC0"))
        assertTrue(SfxCredits.ABOUT_LINE.contains(SfxCredits.AUTHOR))
        assertTrue(SfxCredits.ABOUT_LINES.any { it.contains("Kenney") || it.contains("kenney") })
    }

    @Test
    fun mappingCoversThreeInteractionSlots() {
        assertEquals(3, SfxCredits.MAPPING.size)
        assertTrue(SfxCredits.MAPPING.keys.containsAll(listOf("sfx_tap", "sfx_long_press", "sfx_orb")))
        assertTrue(SfxCredits.MAPPING["sfx_tap"]!!.contains("click1"))
        assertTrue(SfxCredits.MAPPING["sfx_long_press"]!!.contains("switch19"))
        assertTrue(SfxCredits.MAPPING["sfx_orb"]!!.contains("switch3"))
    }

    @Test
    fun silentSfxIsSafeNoOp() {
        SilentAppSfx.enabled = true
        SilentAppSfx.tap()
        SilentAppSfx.longPress()
        SilentAppSfx.orb()
        SilentAppSfx.enabled = false
        SilentAppSfx.tap()
        assertFalse(SilentAppSfx.enabled)
    }

    @Test
    fun silentSfx_honorsEnabledFlagWithoutPlaying() {
        SilentAppSfx.enabled = false
        SilentAppSfx.tap()
        SilentAppSfx.enabled = true
        SilentAppSfx.tap()
        assertTrue(SilentAppSfx.enabled)
    }
}
