package com.leonestudio.tawba.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface JuzDao {

    @Query("SELECT * FROM quran_juz ORDER BY juzNumber ASC")
    fun getAll(): Flow<List<JuzEntry>>

    @Query("SELECT * FROM quran_juz WHERE juzNumber = :num LIMIT 1")
    suspend fun getByNumber(num: Int): JuzEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<JuzEntry>)

    @Query("UPDATE quran_juz SET isRead = :read, lastReadAt = :timestamp WHERE juzNumber = :num")
    suspend fun markJuz(num: Int, read: Boolean, timestamp: Long?)

    @Query("UPDATE quran_juz SET isRead = 0, lastReadAt = NULL")
    suspend fun resetAllJuz()

    @Query("SELECT COUNT(*) FROM quran_juz WHERE isRead = 1")
    fun readCount(): Flow<Int>

    @Query("SELECT MAX(lastReadAt) FROM quran_juz")
    fun lastReadTimestamp(): Flow<Long?>

    /**
     * Get the smallest juz number that is still unread.
     * Used for "قرأت اليوم" quick-mark button.
     * Returns null if all juz are read (khatma complete).
     */
    @Query("SELECT MIN(juzNumber) FROM quran_juz WHERE isRead = 0")
    suspend fun nextUnreadJuz(): Int?
}