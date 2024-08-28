package com.example.asteroidradar.data.remote

import com.example.asteroidradar.data.local.pictureOfTheDay.PictureOfTheDay
import kotlinx.serialization.Serializable

@Serializable
data class PictureOfTheDayNetwork(
    val date: String,
    val explanation: String,
    val title: String,
    val url: String
) {
    fun toEntity(): PictureOfTheDay {
        return PictureOfTheDay(
            date = date,
            explanation = explanation,
            title = title,
            url = url
        )
    }
}