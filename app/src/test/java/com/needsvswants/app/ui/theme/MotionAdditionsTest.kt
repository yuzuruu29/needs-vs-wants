package com.needsvswants.app.ui.theme

import com.needsvswants.app.ui.screens.history.tearShearDegrees
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pins for the D195 motion additions: budget-dial tension mapping, origami
 * unfold phases, stamp-bleed progress curves, tear shear, and the organic
 * SFX pitch variance bounds.
 */
class MotionAdditionsTest {

    @Test
    fun `dial tension stays zero below 90 percent`() {
        assertEquals(0f, dialTension(0f), 0f)
        assertEquals(0f, dialTension(0.5f), 0f)
        assertEquals(0f, dialTension(0.89f), 1e-6f)
    }

    @Test
    fun `dial tension ramps from 90 to 100 percent and clamps`() {
        assertEquals(0.5f, dialTension(0.95f), 1e-4f)
        assertEquals(1f, dialTension(1f), 1e-6f)
        assertEquals(1f, dialTension(1.4f), 1e-6f)
    }

    @Test
    fun `dial glow alpha stays within token bounds`() {
        assertEquals(0f, dialTensionGlowAlpha(0f, 1f), 0f)
        assertEquals(0f, dialTensionGlowAlpha(1f, 0f), 0f)
        assertEquals(0.40f, dialTensionGlowAlpha(1f, 1f), 1e-4f)
        assertTrue(dialTensionGlowAlpha(0.6f, 0.8f) in 0f..0.40f)
    }

    @Test
    fun `origami unfold sheet opens across the sheet portion`() {
        assertEquals(-Motion.UnfoldMaxDegrees, unfoldSheetRotationX(0f), 1e-4f)
        assertEquals(0f, unfoldSheetRotationX(Motion.UnfoldSheetPortion), 1e-4f)
        assertEquals(0f, unfoldSheetRotationX(1f), 1e-4f)
        assertEquals(1f, unfoldSheetProgress(1f), 1e-6f)
        assertEquals(1f, unfoldSheetProgress(0.8f), 1e-6f)
    }

    @Test
    fun `origami seal lands only after the sheet opens`() {
        assertEquals(0f, unfoldSealProgress(0f), 1e-6f)
        assertEquals(0f, unfoldSealProgress(Motion.UnfoldSheetPortion), 1e-6f)
        assertEquals(1f, unfoldSealProgress(1f), 1e-6f)
    }

    @Test
    fun `stamp bleed front expands and matte fades`() {
        assertEquals(0.22f, stampBleedFront(0f), 1e-4f)
        assertEquals(0.46f, stampBleedFront(1f), 1e-4f)
        assertEquals(1f, stampBleedMatte(0.6f), 1e-4f)
        assertEquals(0.85f, stampBleedMatte(1f), 1e-4f)
    }

    @Test
    fun `stamp bleed matte eases on a smoothstep, not a straight line`() {
        // The endpoints agree either way, which is exactly why the Kotlin copy
        // drifted to a linear ramp while the shader kept smoothstep. Pin the
        // middle of the fade window, where the two curves actually disagree.
        assertEquals(0.9766f, stampBleedMatte(0.7f), 1e-3f)
        assertEquals(0.925f, stampBleedMatte(0.8f), 1e-3f)
    }

    @Test
    fun `tear shear grows linearly to 4_5 degrees`() {
        assertEquals(0f, tearShearDegrees(0f), 1e-6f)
        assertEquals(2.25f, tearShearDegrees(0.5f), 1e-4f)
        assertEquals(4.5f, tearShearDegrees(1f), 1e-4f)
    }

    @Test
    fun `organic sfx rate stays within 2 percent and is deterministic per seed`() {
        val seeded = organicRate(seed = 42L)
        assertEquals(seeded, organicRate(seed = 42L), 0f)
        for (seed in 1L..200L) {
            val rate = organicRate(seed = seed)
            assertTrue("rate $rate out of bounds", rate in 0.98f..1.02f)
        }
    }
}
