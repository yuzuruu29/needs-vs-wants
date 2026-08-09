package com.needsvswants.app.domain

import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * Single source of truth for Summary period cutoffs.
 * Day / Week / All windows must match UI range labels and stats/insights filters.
 */
object PeriodWindow {
    private val dayMs = TimeUnit.DAYS.toMillis(1)

    fun startOfDay(epochMs: Long): Long {
        val cal = Calendar.getInstance().apply {
            timeInMillis = epochMs
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }

    /**
     * Inclusive window start for [period].
     * - DAY: local midnight today
     * - WEEK: local midnight 6 days ago (7 calendar days including today)
     * - MONTH: local midnight 29 days ago (30 calendar days including today)
     * - ALL: [retentionCutoffAt] or 0 when unlimited (Free clamps to 30 days via
     *   the cutoff; paid passes null → lifetime)
     */
    fun sinceEpochMs(
        period: Period,
        nowMs: Long = System.currentTimeMillis(),
        retentionCutoffAt: Long? = null
    ): Long {
        val today = startOfDay(nowMs)
        return when (period) {
            Period.DAY -> today
            Period.WEEK -> today - 6L * dayMs
            Period.MONTH -> today - 29L * dayMs
            Period.ALL -> retentionCutoffAt ?: 0L
        }
    }
}
