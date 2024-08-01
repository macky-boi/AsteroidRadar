package com.example.asteroidradar.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NeoFeedResponse(
    val links: Links,
    val page: Page,
    @SerialName("near_earth_objects") val nearEarthObjects: Map<String, List<NearEarthObject>>
)

@Serializable
data class Links(
    val self: String
)

@Serializable
data class Page(
    val size: Int,
    @SerialName("total_elements") val totalElements: Int,
    @SerialName("total_pages") val totalPages: Int,
    val number: Int
)

@Serializable
data class NearEarthObject(
    val id: String,
    val name: String,
    @SerialName("nasa_jpl_url") val nasaJplUrl: String,
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
    val astronomical: String,
    val kilometers: String,
    val lunar: String,
    val miles: String
)

@Serializable
data class RelativeVelocity(
    @SerialName("kilometers_per_second") val kilometersPerSecond: String,
    @SerialName("kilometers_per_hour") val kilometersPerHour: String,
    @SerialName("miles_per_hour") val milesPerHour: String
)