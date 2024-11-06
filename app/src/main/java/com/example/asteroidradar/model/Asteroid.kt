package com.example.asteroidradar.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Asteroid (
    val id: String,
    val name: String,
    val date: String,
    val isHazardous: String,
    val absoluteMagnitude: String,
    val closeApproachDate: String,
    val missDistanceAstronomical: String,
    val relativeVelocityKilometersPerSecond: String
)