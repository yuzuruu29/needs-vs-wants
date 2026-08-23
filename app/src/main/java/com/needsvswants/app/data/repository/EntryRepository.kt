package com.needsvswants.app.data.repository

import com.needsvswants.app.data.db.EntryDao
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.domain.Entitlement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mediates Room [EntryDao] so ViewModels never talk to the DAO directly.
 *
 * Free-tier retention (D122) is a VISIBILITY BOUNDARY, not a deletion. Every
 * read this repository exposes is combined with the trusted entitlement flow:
 * when the effective tier has no Pro access, including Free degraded from a
 * stale paid snapshot (see [com.needsvswants.app.data.entitlement.trustedLocalEntitlement]),
 * queries return only entries inside the last 30 days
 * ([com.needsvswants.app.domain.Entitlement.retentionCutoffAt]); paid and trial
 * tiers see everything. Older rows stay physically stored, so a successful
 * verification after an offline stretch makes lifetime history reappear.
 *
 * Nothing here ever deletes because of entitlement state. The old startup
 * `purgeBefore` path was removed for exactly that reason; the only deletions
 * in the app are explicit user actions (delete entry, start new sheet,
 * clear-all-data). Log, History, Summary, the advisor, the widget, and
 * reminders all consume these bounded flows, so they stay consistent by
 * construction instead of re-filtering per screen.
 */
@Singleton
class EntryRepository @Inject constructor(
    private val dao: EntryDao,
    private val entitlements: EntitlementRepository
) {
    /** Ledger as far as the effective tier can see (Free: last 30 days only). */
    fun observeAll(): Flow<List<Entry>> =
        combine(dao.observeAll(), entitlements.entitlement) { entries, entitlement ->
            visible(entries, entitlement)
        }

    /** Entries recorded against one local calendar day, still retention-bounded. */
    fun observeForDate(date: String): Flow<List<Entry>> =
        combine(dao.observeForDate(date), entitlements.entitlement) { entries, entitlement ->
            visible(entries, entitlement)
        }

    /**
     * Entries from [since] onward, still bounded by the retention visibility
     * window when the effective tier is Free (the tighter bound wins).
     */
    fun observeSince(since: Long): Flow<List<Entry>> =
        combine(dao.observeSince(since), entitlements.entitlement) { entries, entitlement ->
            visible(entries, entitlement)
        }

    suspend fun insert(entry: Entry): Long = dao.insert(entry)

    suspend fun insertAll(entries: List<Entry>): List<Long> = dao.insertAll(entries)

    suspend fun delete(entry: Entry) = dao.delete(entry)

    suspend fun update(entry: Entry) = dao.update(entry)

    /** Re-insert a deleted entry, preserving its id (delete-undo). */
    suspend fun restore(entry: Entry): Long = dao.restore(entry)

    suspend fun deleteAll() = dao.deleteAll()

    suspend fun countForDate(date: String): Int = dao.countForDate(date)

    /** Retention window slice: everything when paid, last 30 days when free. */
    private fun visible(
        entries: List<Entry>,
        entitlement: Entitlement
    ): List<Entry> {
        val cutoff = entitlement.retentionCutoffAt(System.currentTimeMillis())
        if (cutoff == null) return entries
        return entries.filter { it.dateUtc >= cutoff }
    }
}
