package com.needsvswants.app.ui.theme

import androidx.compose.ui.unit.sp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pure unit checks for the Extra-large text scale system: the damped chrome
 * spacing curve, the ledger column width budget, and money size floors
 * (D91 Extra-large consistency pass).
 */
class TypeScaleTest {

    // ── scaledSpacingFactor (damped chrome curve) ────────────────────────

    @Test
    fun scaledSpacingFactor_isIdentityAtDefaultScale() {
        assertEquals(1f, scaledSpacingFactor(1f), 0.0001f)
    }

    @Test
    fun scaledSpacingFactor_dampsHalfTheDelta() {
        // XL app step (1.18): only 55% of the delta reaches padding/heights.
        assertEquals(1f + 0.18f * 0.55f, scaledSpacingFactor(1.18f), 0.0001f)
    }

    @Test
    fun scaledSpacingFactor_clampsAtCombinedLimit() {
        // System 1.3 × app XL would be 1.53 — the curve clamps at 1.45.
        assertEquals(1f + 0.45f * 0.55f, scaledSpacingFactor(1.53f), 0.0001f)
    }

    // ── ledgerScaledFactor (text columns track scale 1:1) ────────────────

    @Test
    fun ledgerScaledFactor_tracksFontScaleAtFullRate() {
        assertEquals(1.18f, ledgerScaledFactor(1.18f), 0.0001f)
    }

    @Test
    fun ledgerScaledFactor_clampsLikeTheRestOfTheApp() {
        assertEquals(1.45f, ledgerScaledFactor(1.6f), 0.0001f)
    }

    // ── ledger column budget (no trailing-cluster overflow at XL) ────────

    @Test
    fun ledgerCluster_fitsSmallPhoneAtEveryScale() {
        // 360dp phone − 40dp page padding − 24dp card padding = 296dp row budget.
        // No delete column since D97 (long-press delete) — the cluster is
        // TIME/COST/TYPE plus gutters only.
        val budget = 296f
        for (fs in listOf(1.0f, 1.10f, 1.18f, 1.45f)) {
            val m = ledgerColumnMetricsAt(fs)
            val fixed = m.timeW.value + m.gutter.value + m.costW.value +
                m.trail.value + m.typeW.value
            assertTrue("fixed cluster $fixed overflows $budget at fs=$fs", fixed <= budget)
        }
    }

    @Test
    fun ledgerItem_keepsReadableWidthAtExtraLarge() {
        // At app-XL on a 360dp phone the item column keeps ≈ 87dp (the old
        // delete column + 0.65-damped cluster left it ~0 at Extra large).
        val m = ledgerColumnMetricsAt(1.18f)
        val fixed = m.timeW.value + m.gutter.value + m.costW.value +
            m.trail.value + m.typeW.value
        assertTrue("item column squeezed to ${296f - fixed}dp", 296f - fixed >= 72f)
    }

    // ── money size floors ─────────────────────────────────────────────────

    @Test
    fun adaptiveMoneySize_neverDropsBelow11sp() {
        // 15+ glyph amounts hit the 0.90/0.78/0.66 factors — must floor at 11sp,
        // not fall to the old 9sp that vanished at Extra large.
        assertTrue(adaptiveMoneySize("₱ 1,234,567,890", 13.sp).value >= 11f)
    }

    @Test
    fun adaptiveMoneySize_shrinksMonotonicallyWithLength() {
        val short = adaptiveMoneySize("₱ 12.50", 13.sp).value
        val medium = adaptiveMoneySize("₱ 12,345,678.90", 13.sp).value
        val long = adaptiveMoneySize("₱ 123,456,789,012.34", 13.sp).value
        assertTrue(short > medium)
        assertTrue(medium > long)
        assertTrue(long >= 11f)
    }

    @Test
    fun typicalMoney_keepsFullSizeInLedgerColumn() {
        // Everyday amounts (≤ 13 glyphs) stay at the full base in the ledger:
        // "₱ 1,234.56" = 4.177em measured — at 15sp×1.18 it still fits costW.
        val m = ledgerColumnMetricsAt(1.18f)
        val size = adaptiveMoneySize("₱ 1,234.56", 15.sp)
        assertEquals(15f, size.value, 0.0001f)
        assertTrue(4.177f * 15f * 1.18f <= m.costW.value)
    }

    @Test
    fun typicalMoney_staysCloseToItemSize() {
        // Money must not look like a footnote next to the 16sp item name:
        // at XL the ledger cost renders ≥ 15sp×1.18 while item is 16sp×1.18.
        assertTrue(adaptiveMoneySize("₱ 1,234.56", 15.sp).value >= 15f)
    }

    @Test
    fun fullSizeMoney_fitsCostColumnAtEveryScale() {
        // "₱ 1,234,567.89" = 5.917em measured — at the FULL 15sp base it fits
        // the 92dp cost column at every scale (both grow with fontScale, so
        // the ratio is scale-invariant: 5.917 × 15 = 88.8 ≤ 92).
        for (fs in listOf(1.0f, 1.18f, 1.45f)) {
            val m = ledgerColumnMetricsAt(fs)
            assertTrue(5.917f * 15f * fs <= m.costW.value)
        }
    }

    @Test
    fun longMoney_fitsScaledCostColumn() {
        // "₱ 12,345,678.90" = 6.414em measured; 15 glyphs step to 0.90× and
        // must still fit the scaled cost column through Extra large.
        val m = ledgerColumnMetricsAt(1.18f)
        val rendered = adaptiveMoneySize("₱ 12,345,678.90", 13.sp).value * 1.18f
        assertTrue(6.414f * rendered <= m.costW.value)
    }

    @Test
    fun bigAmountAtFloor_fitsCostColumnAtClamp() {
        // System 1.3 × app XL = clamp 1.45: the floor-sized 20-glyph amount
        // still fits the fully-scaled cost column (92dp × 1.45).
        val m = ledgerColumnMetricsAt(1.45f)
        val rendered = adaptiveMoneySize("₱ 123,456,789,012.34", 13.sp).value * 1.45f
        assertTrue(8.05f * rendered <= m.costW.value)
    }
}
