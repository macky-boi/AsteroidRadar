package com.example.asteroidradar.data.remote

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
)