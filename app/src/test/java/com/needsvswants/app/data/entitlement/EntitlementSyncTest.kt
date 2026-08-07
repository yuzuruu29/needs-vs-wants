package com.needsvswants.app.data.entitlement

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pins the pure retry-decision logic of [EntitlementSync] without real delays:
 * the schedule, max attempts, and early stop once Pro access is confirmed.
 */
class EntitlementSyncTest {

    @Test
    fun checkoutRetryDelays_areImmediateThen2s5s10s() {
        assertEquals(
            listOf(0L, 2_000L, 5_000L, 10_000L),
            EntitlementSync.checkoutRetryDelaysMillis
        )
    }

    @Test
    fun shouldStop_keepsRetryingWhileFree_andAttemptsRemain() {
        // 4 scheduled attempts (indices 0..3): the first three done-and-free
        // still allow the next retry.
        assertFalse(EntitlementSync.shouldStop(0, hasProAccess = false))
        assertFalse(EntitlementSync.shouldStop(1, hasProAccess = false))
        assertFalse(EntitlementSync.shouldStop(2, hasProAccess = false))
    }

    @Test
    fun shouldStop_stopsAfterLastAttempt() {
        // Last scheduled attempt (index 3) always ends the loop — max attempts.
        assertTrue(EntitlementSync.shouldStop(3, hasProAccess = false))
    }

    @Test
    fun shouldStop_stopsEarlyOnceProConfirmed() {
        // Stop-on-pro at every index, including the first immediate attempt.
        for (index in EntitlementSync.checkoutRetryDelaysMillis.indices) {
            assertTrue("index $index should stop on pro", EntitlementSync.shouldStop(index, hasProAccess = true))
        }
    }

    @Test
    fun shouldStop_outOfRangeIndexStops() {
        assertTrue(EntitlementSync.shouldStop(4, hasProAccess = false))
        assertEquals(4, EntitlementSync.checkoutRetryDelaysMillis.size)
    }
}
