package com.example.asteroidradar.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Asteroids(
    @SerialName("asteroids") val asteroids: Map<String, List<NearEarthObject>>
)

@Serializable
data class NearEarthObject(
    val id: String,
    val name: String,
    @SerialName("is_potentially_hazardous_asteroid") val isHazardous: Boolean,
    @SerialName("absolute_magnitude_h") val absoluteMagnitude: Double,
    @SerialName("close_approach_data") val closeApproachData: List<CloseApproachData>
)

@Serializable
data class CloseApproachData(
    @SerialName("close_approach_date") val closeApproachDate: String,
    @SerialName("miss_distance") val missDistance: MissDistance,
    @SerialName("relative_velocity") val relativeVelocity: RelativeVelocity
)

@Serializable
data class MissDistance(
    val astronomical: String
)


@Serializable
data class RelativeVelocity(
    @SerialName("kilometers_per_second") val kilometersPerSecond: String
)