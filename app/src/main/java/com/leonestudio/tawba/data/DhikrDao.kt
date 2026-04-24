package com.leonestudio.tawba.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DhikrDao {

    @Query("SELECT * FROM adhkar ORDER BY id ASC")
    fun getAll(): Flow<List<Dhikr>>

    @Query("SELECT * FROM adhkar WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Dhikr?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Dhikr>)

    @Update
    suspend fun update(dhikr: Dhikr)

    @Query("UPDATE adhkar SET currentCount = :count WHERE id = :id")
    suspend fun updateCount(id: Int, count: Int)

    @Query("UPDATE adhkar SET currentCount = 0")
    suspend fun resetAllCounts()

    @Query("SELECT SUM(currentCount) FROM adhkar")
    fun totalCount(): Flow<Int?>
}
