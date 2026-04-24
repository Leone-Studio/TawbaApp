package com.leonestudio.tawba.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A single sadaqa entry logged by the user.
 * Amount is stored in the user's configured currency (default MAD).
 */
@Entity(tableName = "sadaqa")
data class SadaqaEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val currency: String = "MAD",
    val note: String = "",
    val category: String = "general", // "general", "zakat", "fitr", "masjid", "orphan", "other"
    val timestamp: Long = System.currentTimeMillis()
)
