package com.needsvswants.app.data.db

import androidx.room.*
import com.needsvswants.app.data.model.Entry
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {
    @Insert
    suspend fun insert(entry: Entry): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<Entry>): List<Long>

    @Query("SELECT * FROM entries WHERE dateUtc >= :since ORDER BY dateUtc DESC")
    fun observeSince(since: Long): Flow<List<Entry>>

    @Query("SELECT * FROM entries ORDER BY dateUtc DESC")
    fun observeAll(): Flow<List<Entry>>

    @Query("SELECT * FROM entries WHERE date = :date ORDER BY dateUtc DESC")
    fun observeForDate(date: String): Flow<List<Entry>>

    // NOTE: there is deliberately NO purge/delete-older-than query here.
    // Free-tier retention (D122) is enforced as a visibility boundary in
    // [com.needsvswants.app.data.repository.EntryRepository]: rows are never
    // physically deleted because an entitlement snapshot is stale, failed,
    // unverified, or even confirmed Free.

    @Query("SELECT COUNT(*) FROM entries WHERE date = :date")
    suspend fun countForDate(date: String): Int

    @Query("DELETE FROM entries")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(entry: Entry)

    @Update
    suspend fun update(entry: Entry)

    /**
     * Re-insert a previously deleted entry, preserving its original [Entry.id].
     * Used to undo a delete (delete + restore round-trip). No new schema.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun restore(entry: Entry): Long
}
