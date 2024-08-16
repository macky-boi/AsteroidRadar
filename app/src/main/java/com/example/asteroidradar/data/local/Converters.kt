package com.example.asteroidradar.data.local

import android.util.Log
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "Converters"

class Converters {
    @TypeConverter
    fun stringToDate(dateString: String): Date? {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            format.parse(dateString)
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing date", e)
            null
        }
    }

    @TypeConverter
    fun dateToString(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }
}