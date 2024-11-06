package com.example.asteroidradar.data.local.asteroid

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.asteroidradar.model.Asteroid

@Entity(tableName = "asteroid")
data class AsteroidEntity (
    @PrimaryKey val id: String,
    val name: String,
    val date: String,
    @ColumnInfo("is_potentially") val isHazardous: Boolean,
    @ColumnInfo("absolute_magnitude_h") val absoluteMagnitude: Double,
    @ColumnInfo("close_approach_date") val closeApproachDate: String,
    @ColumnInfo("miss_distance_astronomical") val missDistanceAstronomical: String,
    @ColumnInfo("relative_velocity_kilometers_per_second") val relativeVelocityKilometersPerSecond: String
) {
    fun toModel(): Asteroid {
        return Asteroid(
            id = this.id,
            name = this.name,
            date = this.date,
            isHazardous = this.isHazardous.toString(),
            absoluteMagnitude = this.absoluteMagnitude.toString(),
            closeApproachDate = this.closeApproachDate,
            missDistanceAstronomical = this.missDistanceAstronomical,
            relativeVelocityKilometersPerSecond = this.relativeVelocityKilometersPerSecond
        )
    }
}