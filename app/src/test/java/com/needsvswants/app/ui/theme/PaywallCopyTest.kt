package com.needsvswants.app.ui.theme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pins the paywall price + billing copy matrix (D171). Every peso amount the
 * paywall shows — plan tags, price rows, payment selector, sticky footer,
 * trial timeline — comes from [PaywallCopy]; these tests make a future price
 * change a one-file edit whose surfaces cannot drift apart.
 */
class PaywallCopyTest {

    // ── D146/D147 locked pricing ─────────────────────────────────────────

    @Test
    fun proPrice_matchesLockedPricing() {
        assertEquals("₱49", PaywallCopy.proPrice(isAnnual = false))
        assertEquals("₱490", PaywallCopy.proPrice(isAnnual = true))
    }

    @Test
    fun maxPrice_matchesLockedPricing() {
        assertEquals("₱99", PaywallCopy.maxPrice(isAnnual = false))
        assertEquals("₱990", PaywallCopy.maxPrice(isAnnual = true))
    }

    @Test
    fun freePrice_rendersFromThisObject() {
        assertEquals("₱0", PaywallCopy.FREE_PRICE)
    }

    // ── One amount per tier/period across every surface ──────────────────

    @Test
    fun paypalSurfaces_allCarryTheSameProAmount() {
        listOf(
            PaywallCopy.paypalProDetail(false),
            PaywallCopy.paypalProFooter(false),
            PaywallCopy.paypalProTrialEndLine(false)
        ).forEach {
            assertTrue("Monthly Pro surface missing ₱49: $it", it.contains(PaywallCopy.PRO_MONTHLY))
        }
        listOf(
            PaywallCopy.paypalProDetail(true),
            PaywallCopy.paypalProFooter(true),
            PaywallCopy.paypalProTrialEndLine(true)
        ).forEach {
            assertTrue("Annual Pro surface missing ₱490: $it", it.contains(PaywallCopy.PRO_ANNUAL))
        }
    }

    @Test
    fun paypalSurfaces_allCarryTheSameMaxAmount() {
        listOf(
            PaywallCopy.paypalMaxDetail(false),
            PaywallCopy.paypalMaxFooter(false),
            PaywallCopy.paypalMaxChargeLine(false)
        ).forEach {
            assertTrue("Monthly Max surface missing ₱99: $it", it.contains(PaywallCopy.MAX_MONTHLY))
        }
        listOf(
            PaywallCopy.paypalMaxDetail(true),
            PaywallCopy.paypalMaxFooter(true),
            PaywallCopy.paypalMaxChargeLine(true)
        ).forEach {
            assertTrue("Annual Max surface missing ₱990: $it", it.contains(PaywallCopy.MAX_ANNUAL))
        }
    }

    @Test
    fun paymongoDetail_namesTheRightAmountAndWindow() {
        assertEquals("One-time ₱49 · 30 days · no auto-charge", PaywallCopy.paymongoDetail(false))
        assertEquals("One-time ₱490 · 365 days · no auto-charge", PaywallCopy.paymongoDetail(true))
    }

    // ── Copy rules ───────────────────────────────────────────────────────

    @Test
    fun trialClaim_isUnconditional_everywhere() {
        val surfaces = listOf(
            PaywallCopy.paypalProDetail(false),
            PaywallCopy.paypalProDetail(true),
            PaywallCopy.paypalProFooter(false),
            PaywallCopy.paypalProFooter(true)
        )
        surfaces.forEach {
            assertFalse("Trial hedge leaked back into copy: $it", it.contains("if enabled"))
        }
        surfaces.forEach {
            assertTrue("Pro PayPal copy must state the 3-day trial: $it", it.contains("3-day free trial"))
        }
    }

    @Test
    fun periodSuffix_tracksTheSelectedCycle() {
        assertTrue(PaywallCopy.paypalProFooter(true).contains("/yr"))
        assertTrue(PaywallCopy.paypalProFooter(false).contains("/mo"))
        assertTrue(PaywallCopy.paypalMaxChargeLine(true).contains("year"))
        assertTrue(PaywallCopy.paypalMaxChargeLine(false).contains("month"))
    }
}
