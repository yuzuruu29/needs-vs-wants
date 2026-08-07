package com.needsvswants.app.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.hypot

/**
 * Pure unit checks for the paper-ink wave geometry and tokens
 * (D98 + D102 quiet polish).
 */
class InkWaveTest {

    @Test
    fun radius_fromCenterReachesEveryCorner() {
        val size = Size(100f, 40f)
        val origin = Offset(50f, 20f)
        val r = farthestCornerRadius(size, origin)
        assertEquals(hypot(50.0, 20.0).toFloat(), r, 0.0001f)
    }

    @Test
    fun radius_fromCornerReachesOppositeCorner() {
        val r = farthestCornerRadius(Size(100f, 40f), Offset(0f, 0f))
        assertEquals(hypot(100.0, 40.0).toFloat(), r, 0.0001f)
    }

    @Test
    fun radius_coversTargetFromAnyPressPoint() {
        val size = Size(320f, 48f)
        for (origin in listOf(
            Offset(2f, 2f), Offset(318f, 2f), Offset(2f, 46f),
            Offset(318f, 46f), Offset(160f, 24f)
        )) {
            val r = farthestCornerRadius(size, origin)
            val corners = listOf(
                Offset(0f, 0f), Offset(size.width, 0f),
                Offset(0f, size.height), Offset(size.width, size.height)
            )
            corners.forEach { c ->
                assertTrue(
                    "radius $r misses corner $c from $origin",
                    (c - origin).getDistance() <= r + 0.0001f
                )
            }
        }
    }

    @Test
    fun waveTokens_areQuietAndTight() {
        // D102: single-layer wave — no multi-crest lag, modest travel.
        assertEquals(0L, WaveRingLagMs)
        assertEquals(0L, WaveCrestLagMs)
        assertTrue(WaveTravelScale in 1.02f..1.12f)
        assertTrue(WaveRingInset in 0.82f..0.95f)
        assertTrue(WaveMinRingDp.value >= 48f)
    }
}
