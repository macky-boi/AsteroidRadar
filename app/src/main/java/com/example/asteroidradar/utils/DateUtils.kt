package com.example.asteroidradar.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtilities {
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateUSTimeZone(): Date {
        val usTimeZone = ZoneId.of("America/New_York") // Use your desired US time zone
        val zonedDateTime  = ZonedDateTime.now(usTimeZone)!!

        val instant = zonedDateTime.toInstant()

        return Date.from(instant)
    }

}


