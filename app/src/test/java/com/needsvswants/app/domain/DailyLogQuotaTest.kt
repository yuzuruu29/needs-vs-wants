package com.needsvswants.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DailyLogQuotaTest {

    @Test
    fun fresh_state_on_new_day_can_log_free_count() {
        val state = QuotaState("2026-08-07", 0, 0)
        assertEquals(FreeQuotaConfig.FREE_DAILY_LOGS, DailyLogQuota.remaining(state, "2026-08-07"))
        assertTrue(DailyLogQuota.canLog(state, "2026-08-07"))
    }

    @Test
    fun after_free_count_canLog_false() {
        val state = QuotaState("2026-08-07", FreeQuotaConfig.FREE_DAILY_LOGS, 0)
        assertEquals(0, DailyLogQuota.remaining(state, "2026-08-07"))
        assertFalse(DailyLogQuota.canLog(state, "2026-08-07"))
    }

    @Test
    fun incrementCreated_advances_counter() {
        val state = QuotaState("2026-08-07", 5, 0)
        val incremented = DailyLogQuota.incrementCreated(state, "2026-08-07")
        assertEquals(6, incremented.logsCreated)
        assertEquals(0, incremented.carriedLogs)
    }

    @Test
    fun rollDay_resets_when_day_changes() {
        val state = QuotaState("2026-08-06", 18, 0)
        val rolled = DailyLogQuota.rollDayIfNeeded(state, "2026-08-07")
        assertEquals("2026-08-07", rolled.day)
        assertEquals(0, rolled.logsCreated)
        assertEquals(0, rolled.carriedLogs)
        assertEquals(FreeQuotaConfig.FREE_DAILY_LOGS, DailyLogQuota.remaining(rolled, "2026-08-07"))
    }

    @Test
    fun rollDay_idempotent_when_same_day() {
        val state = QuotaState("2026-08-07", 5, 2)
        assertEquals(state, DailyLogQuota.rollDayIfNeeded(state, "2026-08-07"))
    }

    @Test
    fun remaining_floored_at_zero_when_over_count() {
        val state = QuotaState("2026-08-07", 15, 0)
        assertEquals(0, DailyLogQuota.remaining(state, "2026-08-07"))
    }

    @Test
    fun pro_unlimited_handled_by_caller_not_domain() {
        // The domain is entitlement-unaware. Pro/Max callers must bypass quota
        // via Entitlement.hasProAccessAt(now) before calling domain functions.
        // This test documents that contract — domain itself has no concept of "pro".
        val state = QuotaState("2026-08-07", 10, 0)
        assertFalse(DailyLogQuota.canLog(state, "2026-08-07"))
    }

    // --- Carry-forward (consecutive active days) ---------------------------

    @Test
    fun unused_allowance_carries_to_next_consecutive_day() {
        // Day 1: sealed 2 of 5 → 3 unused. Day 1 at least one log → carry 3.
        val day1 = QuotaState(day = "2026-08-10", logsCreated = 2, carriedLogs = 0)
        val rolled = DailyLogQuota.rollDayIfNeeded(day1, "2026-08-11")
        assertEquals("2026-08-11", rolled.day)
        assertEquals(0, rolled.logsCreated)
        assertEquals(3, rolled.carriedLogs)
        // Next day allowance = base 5 + carried 3.
        assertEquals(FreeQuotaConfig.FREE_DAILY_LOGS + 3, DailyLogQuota.remaining(rolled, "2026-08-11"))
    }

    @Test
    fun full_usage_carries_zero() {
        // Day 1: sealed all 5 → nothing unused to carry.
        val day1 = QuotaState(day = "2026-08-10", logsCreated = 5, carriedLogs = 0)
        val rolled = DailyLogQuota.rollDayIfNeeded(day1, "2026-08-11")
        assertEquals(0, rolled.carriedLogs)
        assertEquals(FreeQuotaConfig.FREE_DAILY_LOGS, DailyLogQuota.remaining(rolled, "2026-08-11"))
    }

    @Test
    fun carried_allowance_itself_rolls_forward_again() {
        // Day 2 entered with 3 carried from day 1; sealed 6 of (5+3)=8 → 2 unused
        // (active day) → carry 2 into day 3.
        val day2 = QuotaState(day = "2026-08-11", logsCreated = 6, carriedLogs = 3)
        val rolled = DailyLogQuota.rollDayIfNeeded(day2, "2026-08-12")
        assertEquals(2, rolled.carriedLogs)
        assertEquals(FreeQuotaConfig.FREE_DAILY_LOGS + 2, DailyLogQuota.remaining(rolled, "2026-08-12"))
    }

    @Test
    fun missed_day_resets_carry() {
        // Day 1 (08-10) had 3 unused; day 2 is skipped (08-11) → 08-12 gap resets carry to 0.
        val day1 = QuotaState(day = "2026-08-10", logsCreated = 2, carriedLogs = 0)
        val rolled = DailyLogQuota.rollDayIfNeeded(day1, "2026-08-12")
        assertEquals(0, rolled.carriedLogs)
        assertEquals(FreeQuotaConfig.FREE_DAILY_LOGS, DailyLogQuota.remaining(rolled, "2026-08-12"))
    }

    @Test
    fun day_with_no_log_does_not_carry() {
        // Prior day logged 0 → not active → no carry even when consecutive.
        val day1 = QuotaState(day = "2026-08-10", logsCreated = 0, carriedLogs = 0)
        val rolled = DailyLogQuota.rollDayIfNeeded(day1, "2026-08-11")
        assertEquals(0, rolled.carriedLogs)
        assertEquals(FreeQuotaConfig.FREE_DAILY_LOGS, DailyLogQuota.remaining(rolled, "2026-08-11"))
    }

    @Test
    fun carry_is_included_in_next_day_remaining() {
        val day1 = QuotaState(day = "2026-08-10", logsCreated = 1, carriedLogs = 0)
        val day2 = DailyLogQuota.rollDayIfNeeded(day1, "2026-08-11")
        // 4 unused carry → allow 9; seal 2 → remaining 7.
        val day2AfterSealing = DailyLogQuota.incrementCreated(day2, "2026-08-11")
        val day2AfterSealing2 = DailyLogQuota.incrementCreated(day2AfterSealing, "2026-08-11")
        assertEquals(4, day2.carriedLogs)
        assertEquals(FreeQuotaConfig.FREE_DAILY_LOGS + 4 - 2, DailyLogQuota.remaining(day2AfterSealing2, "2026-08-11"))
    }

    @Test
    fun carried_allowance_never_overlaps_into_a_gap() {
        // 08-14 active with 2 unused; 08-15 and 08-16 skipped → 08-17 gap resets carry.
        val dayA = QuotaState(day = "2026-08-14", logsCreated = 3, carriedLogs = 0)
        val afterGap = DailyLogQuota.rollDayIfNeeded(dayA, "2026-08-17")
        assertEquals(0, afterGap.carriedLogs)
        assertEquals(FreeQuotaConfig.FREE_DAILY_LOGS, DailyLogQuota.remaining(afterGap, "2026-08-17"))
    }
}