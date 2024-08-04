package com.example.asteroidradar.data.repository

import com.example.asteroidradar.network.AstronomyPictureOfTheDay
import com.example.asteroidradar.network.NeoApiService
import com.example.asteroidradar.network.NeoFeedResponse

interface AsteroidsRadarRepository {
    suspend fun getNearEarthObjects(): NeoFeedResponse
    suspend fun getPictureOfTheDay(): AstronomyPictureOfTheDay
}

class AsteroidRadarRepositoryImpl (
    private val neoApiService: NeoApiService
): AsteroidsRadarRepository {
    override suspend fun getNearEarthObjects(): NeoFeedResponse {
        return neoApiService.getNearEarthObjects("2024-08-01", "2024-08-01")
    }

    override suspend fun getPictureOfTheDay(): AstronomyPictureOfTheDay {
        return neoApiService.getPictureOfTheDay()
    }
}