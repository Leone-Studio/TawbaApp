package com.leonestudio.tawba.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.leonestudio.tawba.data.JuzDao
import com.leonestudio.tawba.data.JuzEntry
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

data class QuranProgress(
    val juzList: List<JuzEntry> = emptyList(),
    val readCount: Int = 0,
    val totalCount: Int = 30,
    val progressPercent: Float = 0f,
    val streakDays: Int = 0,
    val isKhatmaComplete: Boolean = false,
    val readToday: Boolean = false
) {
    val remainingJuz: Int get() = totalCount - readCount
}

class QuranViewModel(private val juzDao: JuzDao) : ViewModel() {

    val progress: StateFlow<QuranProgress> = combine(
        juzDao.getAll(),
        juzDao.readCount(),
        juzDao.lastReadTimestamp()
    ) { list, read, lastRead ->
        val total = 30
        val percent = if (total > 0) read.toFloat() / total else 0f
        val isComplete = read >= total
        val readTodayFlag = isToday(lastRead)
        val streak = calculateStreak(list)

        QuranProgress(
            juzList = list,
            readCount = read,
            totalCount = total,
            progressPercent = percent,
            streakDays = streak,
            isKhatmaComplete = isComplete,
            readToday = readTodayFlag
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        QuranProgress()
    )

    /**
     * Toggle a specific juz. If marking as read, sets timestamp to now.
     */
    fun toggleJuz(juzNumber: Int, currentlyRead: Boolean) {
        viewModelScope.launch {
            val newRead = !currentlyRead
            val timestamp = if (newRead) System.currentTimeMillis() else null
            juzDao.markJuz(juzNumber, newRead, timestamp)
        }
    }

    /**
     * Quick action: mark the next unread juz as read.
     * If all are already read (khatma complete), does nothing.
     */
    fun markNextJuzRead() {
        viewModelScope.launch {
            val next = juzDao.nextUnreadJuz() ?: return@launch
            juzDao.markJuz(next, true, System.currentTimeMillis())
        }
    }

    /**
     * Reset all juz — start a new khatma.
     */
    fun resetKhatma() {
        viewModelScope.launch {
            juzDao.resetAllJuz()
        }
    }

    private fun isToday(timestamp: Long?): Boolean {
        if (timestamp == null) return false
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        val tomorrow = today + 24 * 60 * 60 * 1000
        return timestamp in today until tomorrow
    }

    /**
     * Streak = consecutive days (including today or yesterday as starting point)
     * where at least one juz was read.
     * Simple implementation: count distinct days from lastReadAt values going backward.
     */
    private fun calculateStreak(list: List<JuzEntry>): Int {
        val timestamps = list.mapNotNull { it.lastReadAt }.sortedDescending()
        if (timestamps.isEmpty()) return 0

        val daysRead = timestamps.map { dayStart(it) }.toSet()
        val today = dayStart(System.currentTimeMillis())
        val yesterday = today - 24 * 60 * 60 * 1000

        // Streak must include today or yesterday to count as "active"
        if (today !in daysRead && yesterday !in daysRead) return 0

        var streak = 0
        var cursor = if (today in daysRead) today else yesterday
        while (cursor in daysRead) {
            streak++
            cursor -= 24 * 60 * 60 * 1000
        }
        return streak
    }

    private fun dayStart(timestamp: Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    class Factory(private val juzDao: JuzDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return QuranViewModel(juzDao) as T
        }
    }
}