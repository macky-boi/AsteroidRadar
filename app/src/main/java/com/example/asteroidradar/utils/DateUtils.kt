package com.example.asteroidradar.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFutureDate(daysAhead: Int): Date {
        val zoneId = ZoneId.systemDefault()
        return  Date.from(
            LocalDate.now().plusDays(daysAhead.toLong())
            .atStartOfDay(zoneId)
            .toInstant())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateUSTimeZone(): Date {
        val usTimeZone = ZoneId.of("America/New_York") // Use your desired US time zone
        val zonedDateTime  = ZonedDateTime.now(usTimeZone)!!

        val instant = zonedDateTime.toInstant()

        return Date.from(instant)
    }

}


