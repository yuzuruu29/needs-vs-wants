package com.needsvswants.app.data.db

import androidx.room.*
import com.needsvswants.app.data.model.Entry
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {
    @Insert
    suspend fun insert(entry: Entry): Long

    @Query("SELECT * FROM entries WHERE dateUtc >= :since ORDER BY dateUtc DESC")
    fun observeSince(since: Long): Flow<List<Entry>>

    @Query("SELECT * FROM entries ORDER BY dateUtc DESC")
    fun observeAll(): Flow<List<Entry>>

    @Query("DELETE FROM entries WHERE dateUtc < :before")
    suspend fun purgeBefore(before: Long): Int

    @Query("SELECT COUNT(*) FROM entries WHERE date = :date")
    suspend fun countForDate(date: String): Int

    @Query("DELETE FROM entries")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(entry: Entry)
}
