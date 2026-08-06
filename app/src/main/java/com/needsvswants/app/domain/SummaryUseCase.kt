package com.needsvswants.app.domain

import com.needsvswants.app.data.db.EntryDao
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.model.EntryType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

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
            val now = System.currentTimeMillis()
            val since = PeriodWindow.sinceEpochMs(
                period = period,
                nowMs = now,
                retentionCutoffAt = ent.retentionCutoffAt(now)
            )
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
}
