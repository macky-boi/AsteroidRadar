package com.example.asteroidradar.data.remote

import com.example.asteroidradar.ui.model.AstronomyPictureOfTheDay
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AstronomyPictureOfTheDayNetwork(
    val date: String,
    val explanation: String,
    @SerialName("media_type") val mediaType: String,
    @SerialName("service_version") val serviceVersion: String,
    val title: String,
    val url: String
) {
    fun toModel(): AstronomyPictureOfTheDay {
        return AstronomyPictureOfTheDay(
            date = date,
            explanation = explanation,
            mediaType = mediaType,
            serviceVersion = serviceVersion,
            title = title,
            url = url
        )
    }
}