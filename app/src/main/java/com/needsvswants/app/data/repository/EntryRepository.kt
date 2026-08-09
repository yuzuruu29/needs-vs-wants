package com.needsvswants.app.data.repository

import com.needsvswants.app.data.db.EntryDao
import com.needsvswants.app.data.model.Entry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mediates Room [EntryDao] so ViewModels never talk to the DAO directly.
 * Behavior-preserving wrapper — no business logic here.
 */
@Singleton
class EntryRepository @Inject constructor(
    private val dao: EntryDao
) {
    fun observeAll(): Flow<List<Entry>> = dao.observeAll()

    fun observeSince(since: Long): Flow<List<Entry>> = dao.observeSince(since)

    suspend fun insert(entry: Entry): Long = dao.insert(entry)

    suspend fun delete(entry: Entry) = dao.delete(entry)

    suspend fun update(entry: Entry) = dao.update(entry)

    /** Re-insert a deleted entry, preserving its id (delete-undo). */
    suspend fun restore(entry: Entry): Long = dao.restore(entry)

    suspend fun deleteAll() = dao.deleteAll()

    suspend fun purgeBefore(before: Long): Int = dao.purgeBefore(before)

    suspend fun countForDate(date: String): Int = dao.countForDate(date)
}
