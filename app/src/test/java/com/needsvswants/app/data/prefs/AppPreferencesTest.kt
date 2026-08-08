package com.needsvswants.app.data.prefs

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pins the pure TTL gate behind the durable PayPal-return pending flag:
 * active strictly within the TTL, expired at the boundary, and 0 = inactive.
 */
class AppPreferencesTest {

    private val dayMillis = 24 * 60 * 60 * 1000L

    @Test
    fun paypalReturnPendingActive_trueWithinTtl() {
        val now = 100_000_000L
        assertTrue(paypalReturnPendingActive(storedAt = now - 1_000L, nowMillis = now))
        // One millisecond before the boundary is still active.
        assertTrue(paypalReturnPendingActive(storedAt = now - dayMillis + 1L, nowMillis = now))
    }

    @Test
    fun paypalReturnPendingActive_falseAtTtlBoundaryAndBeyond() {
        val now = 100_000_000L
        // Exactly at the boundary the flag is stale (strictly-less-than TTL).
        assertFalse(paypalReturnPendingActive(storedAt = now - dayMillis, nowMillis = now))
        assertFalse(paypalReturnPendingActive(storedAt = now - dayMillis - 1_000L, nowMillis = now))
    }

    @Test
    fun paypalReturnPendingActive_falseWhenZeroOrNeverStored() {
        val now = 100_000_000L
        assertFalse(paypalReturnPendingActive(storedAt = 0L, nowMillis = now))
        assertFalse(paypalReturnPendingActive(storedAt = -5L, nowMillis = now))
    }

    @Test
    fun paypalReturnPendingActive_honorsCustomTtl() {
        val now = 100_000_000L
        assertTrue(paypalReturnPendingActive(storedAt = now - 9_000L, nowMillis = now, ttlMillis = 10_000L))
        assertFalse(paypalReturnPendingActive(storedAt = now - 10_000L, nowMillis = now, ttlMillis = 10_000L))
    }
}
