package com.example.asteroidradar.data.repository

import android.content.SharedPreferences

class PictureOfTheDayRepository (private val sharedPreferences: SharedPreferences) {

    fun savePictureOfTheDay(imgUrl: String, date: String) {
        sharedPreferences.edit().putString("img_url", imgUrl).apply()
        sharedPreferences.edit().putString("date", imgUrl).apply()
    }

    fun getPictureOfTheDay(): Pair<String?, String?> {
        val url = sharedPreferences.getString("img_url", null)
        val date = sharedPreferences.getString("date", null)
        return Pair(url, date)
    }


    fun clearData() {
        sharedPreferences.edit().clear().apply()
    }
}