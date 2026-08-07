package com.needsvswants.app.ui.theme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pure unit checks for paper page-turn tokens and offset math helpers.
 * PagerState itself needs Compose runtime — offset formula is verified via
 * the same arithmetic [pagerPageOffset] uses.
 */
class PaperPageFlipTest {

    @Test
    fun pageOffset_settledCurrentIsZero() {
        // (currentPage - page) + fraction
        val currentPage = 2
        val page = 2
        val fraction = 0f
        assertEquals(0f, (currentPage - page) + fraction)
    }

    @Test
    fun pageOffset_leavingForwardIsPositive() {
        // Swiping toward next: fraction > 0, current page still old index.
        val currentPage = 0
        val page = 0
        val fraction = 0.4f
        val offset = (currentPage - page) + fraction
        assertTrue(offset > 0f)
        assertEquals(0.4f, offset)
    }

    @Test
    fun pageOffset_incomingNextIsNegative() {
        val currentPage = 0
        val page = 1
        val fraction = 0.4f
        val offset = (currentPage - page) + fraction
        assertEquals(-0.6f, offset, 0.0001f)
        assertTrue(offset < 0f)
    }

    @Test
    fun pageFlipTokens_areNotebookPace() {
        // Snappy tab turns (nested with vertical scroll); lighter tilt for responsiveness.
        assertTrue(Motion.PageFlipMs in 250..400)
        assertTrue(Motion.PageFlipMaxDegrees in 30f..55f)
        assertTrue(Motion.PageFlipCameraDistance >= 12f)
    }
}
