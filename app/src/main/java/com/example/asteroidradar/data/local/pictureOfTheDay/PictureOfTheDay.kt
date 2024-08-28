package com.example.asteroidradar.data.local.pictureOfTheDay

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pictureOfTheDay")
data class PictureOfTheDay (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val explanation: String,
    val title: String,
    val url: String
)