package com.needsvswants.app.domain

data class QuotaState(
    val day: String,
    val logsCreated: Int,
    val bonusLogs: Int,
    val adsWatched: Int
)

object DailyLogQuota {
    fun remaining(state: QuotaState, today: String): Int {
        val rolled = rollDayIfNeeded(state, today)
        return (AdsConfig.FREE_DAILY_LOGS + rolled.bonusLogs - rolled.logsCreated).coerceAtLeast(0)
    }

    fun canLog(state: QuotaState, today: String): Boolean =
        remaining(state, today) > 0

    fun incrementCreated(state: QuotaState, today: String): QuotaState {
        val rolled = rollDayIfNeeded(state, today)
        return rolled.copy(logsCreated = rolled.logsCreated + 1)
    }

    fun canWatchAd(state: QuotaState, today: String): Boolean {
        val rolled = rollDayIfNeeded(state, today)
        return rolled.adsWatched < AdsConfig.MAX_REWARDED_ADS_PER_DAY
    }

    fun grantBonus(state: QuotaState, today: String): QuotaState {
        val rolled = rollDayIfNeeded(state, today)
        if (rolled.adsWatched >= AdsConfig.MAX_REWARDED_ADS_PER_DAY) return rolled
        return rolled.copy(
            adsWatched = rolled.adsWatched + 1,
            bonusLogs = rolled.bonusLogs + AdsConfig.EXTRA_LOGS_PER_REWARD
        )
    }

    fun rollDayIfNeeded(state: QuotaState, today: String): QuotaState =
        if (state.day == today) state else QuotaState(day = today, logsCreated = 0, bonusLogs = 0, adsWatched = 0)
}
