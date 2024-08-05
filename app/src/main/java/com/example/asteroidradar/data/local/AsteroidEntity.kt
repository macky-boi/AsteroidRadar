package com.example.asteroidradar.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.asteroidradar.data.remote.CloseApproachData
import com.example.asteroidradar.data.remote.MissDistance
import com.example.asteroidradar.data.remote.RelativeVelocity
import kotlinx.serialization.SerialName

@Entity
data class AsteroidEntity (
    @PrimaryKey val id: String,
    val name: String,
    @ColumnInfo("is_potentially") val isHazardous: Boolean,
    @ColumnInfo("absolute_magnitude_h") val absoluteMagnitude: Double,
    @ColumnInfo("close_approach_date") val closeApproachDate: String,
    @ColumnInfo("miss_distance_astronomical") val missDistanceAstronomical: String,
    @ColumnInfo("relative_velocity_kilometers_per_second") val relativeVelocityKilometersPerSecond: String
)