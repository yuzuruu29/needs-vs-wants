package com.needsvswants.app.ui.theme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pure structural tests for the paper ledger surface system.
 * Compose recipes are exercised via [PaperSpec] construction contracts.
 */
class PaperTokensTest {

    @Test
    fun paperKinds_includeReceiptAndNoGlassKinds() {
        val kinds = PaperKind.entries.map { it.name }.toSet()
        assertTrue(kinds.contains("RAISED"))
        assertTrue(kinds.contains("CHIP"))
        assertTrue(kinds.contains("RECEIPT"))
        assertFalse(kinds.contains("GLASS"))
    }

    @Test
    fun chipSpec_contract_hasNoRules() {
        // CHIP must never rule under dense labels (contrast).
        val one = androidx.compose.ui.unit.Dp(1f)
        val chip = PaperSpec(
            fill = androidx.compose.ui.graphics.Color.White,
            ruleColor = androidx.compose.ui.graphics.Color.Black,
            ruleAlpha = 0f,
            ruleSpacingDp = 8f,
            marginRuleColor = null,
            marginRuleXFrac = 0.08f,
            inkBorder = androidx.compose.ui.graphics.Color.Gray,
            inkBorderWidth = one,
            deskShadow = androidx.compose.ui.graphics.Color.Black,
            elevation = one,
            creaseAlpha = 0f,
            grainAlpha = 0f,
            serration = false,
        )
        assertEquals(0f, chip.ruleAlpha, 0.0001f)
        assertFalse(chip.serration)
    }

    @Test
    fun receiptSpec_contract_serrationWithoutRules() {
        val receipt = PaperSpec(
            fill = androidx.compose.ui.graphics.Color(0xFFF7F4EC),
            ruleColor = androidx.compose.ui.graphics.Color.Black,
            ruleAlpha = 0f,
            ruleSpacingDp = 8f,
            marginRuleColor = null,
            marginRuleXFrac = 0.08f,
            inkBorder = androidx.compose.ui.graphics.Color.Gray,
            inkBorderWidth = androidx.compose.ui.unit.Dp(1f),
            deskShadow = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.14f),
            elevation = androidx.compose.ui.unit.Dp(3f),
            creaseAlpha = 0.06f,
            grainAlpha = 0.025f,
            serration = true,
        )
        assertTrue(receipt.serration)
        assertEquals(0f, receipt.ruleAlpha, 0.0001f)
    }

    @Test
    fun inkMotionTokens_arePaperPace() {
        assertTrue(Motion.InkSettleMs in 200..400)
        assertTrue(Motion.InkDrawMs in 350..600)
        assertTrue(Motion.ReceiptPrintMs in 250..500)
    }

    @Test
    fun inkSettle_collapsesWhenReducedMotion() {
        Motion.updateEnabled(0f)
        assertEquals(1, Motion.inkSettle<Float>().durationMillis)
        assertEquals(1, Motion.inkDraw<Float>().durationMillis)
        assertEquals(1, Motion.receiptPrint<Float>().durationMillis)
        Motion.updateEnabled(1f)
        assertEquals(Motion.InkSettleMs, Motion.inkSettle<Float>().durationMillis)
    }
}
