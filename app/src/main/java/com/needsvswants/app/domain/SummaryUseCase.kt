package com.needsvswants.app.domain

import com.needsvswants.app.data.db.EntryDao
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.model.EntryType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.util.Calendar

data class SummaryStats(
    val needsTotalCents: Long = 0,
    val wantsTotalCents: Long = 0,
    val needsCount: Int = 0,
    val wantsCount: Int = 0,
    val totalCents: Long = 0,
    val needsPct: Int = 0,
    val wantsPct: Int = 0
)

enum class Period { DAY, WEEK, ALL }

class SummaryUseCase(
    private val dao: EntryDao,
    private val entitlementRepository: EntitlementRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getStats(period: Period): Flow<SummaryStats> {
        return entitlementRepository.entitlement.flatMapLatest { ent ->
            val since = when (period) {
                Period.DAY -> startOfToday()
                Period.WEEK -> startOfToday() - 7L * 24 * 60 * 60 * 1000
                Period.ALL -> ent.retentionCutoffAt(System.currentTimeMillis()) ?: 0L
            }
            dao.observeSince(since).map { entries ->
                val needs = entries.filter { it.type == EntryType.NEED }
                val wants = entries.filter { it.type == EntryType.WANT }
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
                    wantsPct = if (total > 0) ((wantsTotal * 100) / total).toInt() else 0
                )
            }
        }
    }

    private fun startOfToday(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
