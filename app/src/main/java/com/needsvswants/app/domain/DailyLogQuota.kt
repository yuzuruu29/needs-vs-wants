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
 * [bonusLogs] is the allowance granted by rewarded ads completed today.
 * [adsWatched] is how many rewarded ads were completed today.
 */
data class QuotaState(
    val day: String,
    val logsCreated: Int,
    val carriedLogs: Int,
    val bonusLogs: Int = 0,
    val adsWatched: Int = 0
)

object DailyLogQuota {

    /**
     * Remaining Free logs for [today]: the base allowance plus any carried
     * and ad-bonus allowance, minus what was already sealed today, floored at
     * zero.
     */
    fun remaining(state: QuotaState, today: String): Int {
        val rolled = rollDayIfNeeded(state, today)
        return (AdsConfig.FREE_DAILY_LOGS + rolled.carriedLogs + rolled.bonusLogs - rolled.logsCreated)
            .coerceAtLeast(0)
    }

    fun canLog(state: QuotaState, today: String): Boolean =
        remaining(state, today) > 0

    /** True while the daily rewarded-ad cap for [today] is not reached. */
    fun canWatchAd(state: QuotaState, today: String): Boolean {
        val rolled = rollDayIfNeeded(state, today)
        return rolled.adsWatched < AdsConfig.MAX_REWARDED_ADS_PER_DAY
    }

    /**
     * Grants one rewarded-ad bonus for [today]: counts the ad and adds
     * [AdsConfig.EXTRA_LOGS_PER_REWARD] bonus logs. A no-op once the daily cap
     * is reached. Callers must invoke this only after a real rewarded
     * completion (onUserEarnedReward) — never on close or failure.
     */
    fun grantBonus(state: QuotaState, today: String): QuotaState {
        val rolled = rollDayIfNeeded(state, today)
        if (rolled.adsWatched >= AdsConfig.MAX_REWARDED_ADS_PER_DAY) return rolled
        return rolled.copy(
            adsWatched = rolled.adsWatched + 1,
            bonusLogs = rolled.bonusLogs + AdsConfig.EXTRA_LOGS_PER_REWARD
        )
    }

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
     * - New day starts with [logsCreated] = 0; ad bonus and watched count
     *   never carry into a new day.
     */
    fun rollDayIfNeeded(state: QuotaState, today: String): QuotaState {
        if (state.day == today) return state
        val carried = if (isConsecutiveActive(state, today)) {
            (AdsConfig.FREE_DAILY_LOGS + state.carriedLogs - state.logsCreated)
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