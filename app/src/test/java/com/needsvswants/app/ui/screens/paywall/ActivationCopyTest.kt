package com.needsvswants.app.ui.screens.paywall

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** Pure copy-matrix tests for the Pro/Max activation seal (D136). */
class ActivationCopyTest {

    @Test
    fun max_wins_over_pro() {
        val copy = ActivationCopy.forEntitlement(isPro = true, hasMaxAccess = true)
        assertEquals(ActivationTier.Max, copy!!.tier)
        assertEquals("You're on Max", copy.title)
        assertEquals("SUCCESSFULLY ACTIVATED", copy.eyebrow)
    }

    @Test
    fun pro_only() {
        val copy = ActivationCopy.forEntitlement(isPro = true, hasMaxAccess = false)
        assertEquals(ActivationTier.Pro, copy!!.tier)
        assertEquals("You're on Pro", copy.title)
        assertEquals(
            "Unlimited sheets, lifetime history, and full period analytics are unlocked on this device.",
            copy.body
        )
    }

    @Test
    fun free_returns_null() {
        assertNull(ActivationCopy.forEntitlement(isPro = false, hasMaxAccess = false))
    }

    @Test
    fun quietStatusLine_matches_tier() {
        assertEquals(
            "Successfully activated · You're on Pro",
            ActivationCopy.quietStatusLine(ActivationTier.Pro)
        )
        assertEquals(
            "Successfully activated · You're on Max",
            ActivationCopy.quietStatusLine(ActivationTier.Max)
        )
    }
}