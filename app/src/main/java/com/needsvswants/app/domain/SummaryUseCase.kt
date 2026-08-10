package com.needsvswants.app.domain

import com.needsvswants.app.data.db.EntryDao
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

/** One day's spend for a Summary period — drives the sparkline / day-of-week charts. */
data class DailySpend(
    val date: String,       // yyyy-MM-dd
    val needsCents: Long,
    val wantsCents: Long
) {
    val totalCents: Long get() = needsCents + wantsCents
}

data class SummaryStats(
    val needsTotalCents: Long = 0,
    val wantsTotalCents: Long = 0,
    val needsCount: Int = 0,
    val wantsCount: Int = 0,
    val totalCents: Long = 0,
    val needsPct: Int = 0,
    val wantsPct: Int = 0,
    // NEW (design audit #5): per-day breakdown for the sparkline + trend comparison.
    val dailyTotals: List<DailySpend> = emptyList(),
    val previousPeriodTotalCents: Long = 0
)

enum class Period { DAY, WEEK, MONTH, ALL }

class SummaryUseCase(
    private val dao: EntryDao,
    private val entitlementRepository: EntitlementRepository
) {
    private val dayMs = TimeUnit.DAYS.toMillis(1)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getStats(period: Period): Flow<SummaryStats> {
        return entitlementRepository.entitlement.flatMapLatest { ent ->
            val now = System.currentTimeMillis()
            val cutoff = ent.retentionCutoffAt(now)
            val since = PeriodWindow.sinceEpochMs(period, now, cutoff)
            val prevStart = previousPeriodStart(period, since)
            // Observe the previous-period window too so the trend comparison is
            // computed from real data, not a second DAO hop.
            dao.observeSince(prevStart).map { all ->
                val current = all.filter { it.dateUtc >= since }
                val prev = all.filter { it.dateUtc in prevStart until since }
                val needs = current.filter { it.type == EntryType.NEED }
                val wants = current.filter { it.type == EntryType.WANT }
                val needsTotal = needs.sumOf { it.costCents }
                val wantsTotal = wants.sumOf { it.costCents }
                val total = needsTotal + wantsTotal
                SummaryStats(
                    needsTotalCents = needsTotal,
                    wantsTotalCents = wantsTotal,
                    needsCount = needs.size,
                    wantsCount = wants.size,
                    totalCents = total,
                    needsPct = if (total > 0) ((needsTotal * 100) / total).toInt() else 0,
                    wantsPct = if (total > 0) ((wantsTotal * 100) / total).toInt() else 0,
                    dailyTotals = dailyBuckets(period, now, since, current),
                    previousPeriodTotalCents = prev.sumOf { it.costCents }
                )
            }
        }
    }

    /** Start of the window immediately before the current period (same duration). */
    private fun previousPeriodStart(period: Period, currentSince: Long): Long = when (period) {
        Period.DAY -> currentSince - dayMs
        Period.WEEK -> currentSince - 7L * dayMs
        Period.MONTH -> currentSince - 30L * dayMs
        Period.ALL -> currentSince - 30L * dayMs
    }

    /**
     * Produces one [DailySpend] per calendar day in the current period, filling
     * zero days so the sparkline is a continuous series. ALL is capped to the
     * trailing 30 days (a lifetime sparkline is not visually useful).
     */
    private fun dailyBuckets(period: Period, nowMs: Long, since: Long, entries: List<Entry>): List<DailySpend> {
        val byDate = entries.groupBy { it.date }
        val today = PeriodWindow.startOfDay(nowMs)
        val bucketStart = maxOf(since, today - 29L * dayMs)
        val days = ((today - bucketStart) / dayMs + 1L).toInt().coerceAtLeast(1)
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val cal = Calendar.getInstance().apply { timeInMillis = bucketStart }
        val result = ArrayList<DailySpend>(days)
        repeat(days) {
            val key = fmt.format(cal.time)
            val dayEntries = byDate[key].orEmpty()
            result.add(
                DailySpend(
                    date = key,
                    needsCents = dayEntries.filter { it.type == EntryType.NEED }.sumOf { it.costCents },
                    wantsCents = dayEntries.filter { it.type == EntryType.WANT }.sumOf { it.costCents }
                )
            )
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        return result
    }
}