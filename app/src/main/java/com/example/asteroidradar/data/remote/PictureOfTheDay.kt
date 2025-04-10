package com.example.asteroidradar.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class PictureOfTheDay(
    val date: String,
    val explanation: String,
    val title: String,
    val url: String
)