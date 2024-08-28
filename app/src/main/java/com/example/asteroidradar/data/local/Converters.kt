package com.example.asteroidradar.data.local

import android.util.Log
import androidx.room.TypeConverter
import com.example.asteroidradar.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "Converters"

class Converters {
    @TypeConverter
    fun stringToDate(dateString: String): Date? = DateUtils().stringToDate(dateString)

    @TypeConverter
    fun dateToString(date: Date): String = DateUtils().dateToString(date)
}