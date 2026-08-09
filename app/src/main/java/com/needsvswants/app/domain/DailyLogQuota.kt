package com.needsvswants.app.domain

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Quota state for the simple local Free tier.
 *
 * [day] is the local calendar day (yyyy-MM-dd) the counters belong to.
 * [logsCreated] is how many Free logs were sealed that day.
 * [carriedLogs] is the unused allowance carried in from the previous
 * consecutive active day (0 on a fresh/gapped start).
 *
 * Ad fields (bonusLogs / adsWatched) were removed with the AdMob call-off.
 */
data class QuotaState(
    val day: String,
    val logsCreated: Int,
    val carriedLogs: Int
)

object DailyLogQuota {

    /**
     * Remaining Free logs for [today]: the base allowance plus any carried
     * allowance, minus what was already sealed today, floored at zero.
     */
    fun remaining(state: QuotaState, today: String): Int {
        val rolled = rollDayIfNeeded(state, today)
        return (FreeQuotaConfig.FREE_DAILY_LOGS + rolled.carriedLogs - rolled.logsCreated)
            .coerceAtLeast(0)
    }

    fun canLog(state: QuotaState, today: String): Boolean =
        remaining(state, today) > 0

    fun incrementCreated(state: QuotaState, today: String): QuotaState {
        val rolled = rollDayIfNeeded(state, today)
        return rolled.copy(logsCreated = rolled.logsCreated + 1)
    }

    /**
     * Rolls quota state to [today] deterministically (API-24-safe Calendar):
     * - Same day: preserve state.
     * - Immediately following day, with at least one log on the prior day:
     *   carry `max(0, base + prior carried - prior logsCreated)` into the new
     *   day (unused allowance rolls over while the streak stays active).
     * - Any gap / missed day: reset [carriedLogs] to zero.
     * - New day starts with [logsCreated] = 0.
     */
    fun rollDayIfNeeded(state: QuotaState, today: String): QuotaState {
        if (state.day == today) return state
        val carried = if (isConsecutiveActive(state, today)) {
            (FreeQuotaConfig.FREE_DAILY_LOGS + state.carriedLogs - state.logsCreated)
                .coerceAtLeast(0)
        } else {
            0
        }
        return QuotaState(day = today, logsCreated = 0, carriedLogs = carried)
    }

    /** True when [state] is the immediately preceding day of [today] AND it logged at least once. */
    private fun isConsecutiveActive(state: QuotaState, today: String): Boolean {
        if (state.logsCreated <= 0) return false
        val prev = parseDay(state.day) ?: return false
        val current = parseDay(today) ?: return false
        val cal = Calendar.getInstance().apply {
            timeInMillis = prev
            add(Calendar.DAY_OF_MONTH, 1)
        }
        return cal.timeInMillis == current
    }

    private fun parseDay(day: String): Long? =
        runCatching {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply { isLenient = false }
                .parse(day)?.time
        }.getOrNull()
}