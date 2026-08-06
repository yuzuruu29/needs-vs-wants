package com.needsvswants.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DailyLogQuotaTest {

    @Test
    fun fresh_state_on_new_day_can_log_free_count() {
        val state = QuotaState("2026-08-07", 0, 0, 0)
        assertEquals(10, DailyLogQuota.remaining(state, "2026-08-07"))
        assertTrue(DailyLogQuota.canLog(state, "2026-08-07"))
    }

    @Test
    fun after_free_count_canLog_false() {
        val state = QuotaState("2026-08-07", 10, 0, 0)
        assertEquals(0, DailyLogQuota.remaining(state, "2026-08-07"))
        assertFalse(DailyLogQuota.canLog(state, "2026-08-07"))
    }

    @Test
    fun grantBonus_adds_extra_logs_and_increments_ads() {
        val state = QuotaState("2026-08-07", 0, 0, 0)
        val granted = DailyLogQuota.grantBonus(state, "2026-08-07")
        assertEquals(1, granted.adsWatched)
        assertEquals(8, granted.bonusLogs)
        assertEquals(0, granted.logsCreated)
        assertEquals(18, DailyLogQuota.remaining(granted, "2026-08-07"))
    }

    @Test
    fun grantBonus_after_max_ads_is_noop() {
        val state = QuotaState("2026-08-07", 0, 16, 3)
        val granted = DailyLogQuota.grantBonus(state, "2026-08-07")
        assertEquals(state, granted)
    }

    @Test
    fun canWatchAd_false_after_three() {
        val state = QuotaState("2026-08-07", 0, 0, 3)
        assertFalse(DailyLogQuota.canWatchAd(state, "2026-08-07"))
    }

    @Test
    fun incrementCreated_advances_counter() {
        val state = QuotaState("2026-08-07", 5, 0, 0)
        val incremented = DailyLogQuota.incrementCreated(state, "2026-08-07")
        assertEquals(6, incremented.logsCreated)
        assertEquals(0, incremented.bonusLogs)
        assertEquals(0, incremented.adsWatched)
    }

    @Test
    fun rollDay_resets_when_day_changes() {
        val state = QuotaState("2026-08-06", 18, 24, 3)
        val rolled = DailyLogQuota.rollDayIfNeeded(state, "2026-08-07")
        assertEquals("2026-08-07", rolled.day)
        assertEquals(0, rolled.logsCreated)
        assertEquals(0, rolled.bonusLogs)
        assertEquals(0, rolled.adsWatched)
        assertEquals(10, DailyLogQuota.remaining(rolled, "2026-08-07"))
    }

    @Test
    fun rollDay_idempotent_when_same_day() {
        val state = QuotaState("2026-08-07", 5, 8, 1)
        assertEquals(state, DailyLogQuota.rollDayIfNeeded(state, "2026-08-07"))
    }

    @Test
    fun remaining_floored_at_zero_when_over_count() {
        val state = QuotaState("2026-08-07", 15, 0, 0)
        assertEquals(0, DailyLogQuota.remaining(state, "2026-08-07"))
    }

    @Test
    fun pro_unlimited_handled_by_caller_not_domain() {
        // The domain is entitlement-unaware. Pro/Max callers must bypass quota
        // via Entitlement.hasProAccessAt(now) before calling domain functions.
        // This test documents that contract — domain itself has no concept of "pro".
        val state = QuotaState("2026-08-07", 10, 0, 0)
        assertFalse(DailyLogQuota.canLog(state, "2026-08-07"))
    }

    @Test
    fun maxed_quota_still_allows_via_bonus() {
        // 3 ads watched (+24 bonus) with 6 logs created → 28 remaining.
        val state = QuotaState("2026-08-07", 6, 24, 3)
        assertTrue(DailyLogQuota.canLog(state, "2026-08-07"))
        assertEquals(28, DailyLogQuota.remaining(state, "2026-08-07"))
    }
}
