package com.needsvswants.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entries")
data class Entry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateUtc: Long,
    val date: String,
    val time: String,
    val item: String,
    val costCents: Long,
    val type: EntryType
)

enum class EntryType { NEED, WANT }
