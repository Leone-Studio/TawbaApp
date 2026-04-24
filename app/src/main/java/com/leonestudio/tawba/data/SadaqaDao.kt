package com.leonestudio.tawba.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SadaqaDao {

    @Query("SELECT * FROM sadaqa ORDER BY timestamp DESC")
    fun getAll(): Flow<List<SadaqaEntry>>

    @Insert
    suspend fun insert(entry: SadaqaEntry): Long

    @Delete
    suspend fun delete(entry: SadaqaEntry)

    @Query("SELECT SUM(amount) FROM sadaqa")
    fun totalAmount(): Flow<Double?>

    @Query("SELECT SUM(amount) FROM sadaqa WHERE timestamp >= :since")
    fun totalAmountSince(since: Long): Flow<Double?>

    @Query("SELECT COUNT(*) FROM sadaqa")
    fun entryCount(): Flow<Int>

    @Query("DELETE FROM sadaqa")
    suspend fun deleteAll()
}
