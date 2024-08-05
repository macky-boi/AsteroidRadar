package com.example.asteroidradar.data.remote


import com.example.asteroidradar.data.local.Asteroid
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AsteroidsNetwork(
    @SerialName("near_earth_objects") val asteroids: Map<String, List<AsteroidNetwork>>
)

@Serializable
data class AsteroidNetwork(
    val id: String,
    val name: String,
    @SerialName("is_potentially_hazardous_asteroid") val isHazardous: Boolean,
    @SerialName("absolute_magnitude_h") val absoluteMagnitude: Double,
    @SerialName("close_approach_data") val closeApproachData: List<CloseApproachData>
) {
    fun toEntity(): Asteroid {
        return Asteroid(
            id = id,
            name = name,
            isHazardous = isHazardous,
            absoluteMagnitude = absoluteMagnitude,
            closeApproachDate = closeApproachData[0].closeApproachDate,
            missDistanceAstronomical = closeApproachData[0].missDistance.astronomical,
            relativeVelocityKilometersPerSecond = closeApproachData[0].relativeVelocity.kilometersPerSecond
        )
    }
}

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
