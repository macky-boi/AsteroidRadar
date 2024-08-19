package com.example.asteroidradar.ui.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName

data class AstronomyPictureOfTheDay (
    val date: String,
    val explanation: String,
    val mediaType: String,
    val serviceVersion: String,
    val title: String,
    val url: String
)