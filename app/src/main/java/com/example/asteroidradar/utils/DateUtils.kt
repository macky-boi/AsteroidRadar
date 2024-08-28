package com.example.asteroidradar.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateUtils {
    fun stringToDate(dateString: String): Date? {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            format.parse(dateString)
        } catch (e: Exception) {
            Log.e("DateUtils", "Error parsing date", e)
            null
        }
    }

    fun dateToString(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }

    fun getFutureDateString(daysFromToday: Int): String {
        val today = Calendar.getInstance()
        today.add(Calendar.DAY_OF_YEAR, daysFromToday)
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(today.time)
    }

    fun presentDateString(): String {
        val today = Calendar.getInstance()
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(today.time)
    }

    companion object {
        const val NUMBER_OF_DAYS = 7
    }
}