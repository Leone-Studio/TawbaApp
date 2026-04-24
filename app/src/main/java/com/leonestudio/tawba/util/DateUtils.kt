package com.leonestudio.tawba.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {

    fun startOfToday(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun startOfWeek(): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = startOfToday()
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        return cal.timeInMillis
    }

    fun startOfMonth(): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = startOfToday()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        return cal.timeInMillis
    }

    fun formatDate(timestamp: Long, locale: Locale = Locale.getDefault()): String {
        val fmt = SimpleDateFormat("dd MMM yyyy", locale)
        return fmt.format(timestamp)
    }
}
