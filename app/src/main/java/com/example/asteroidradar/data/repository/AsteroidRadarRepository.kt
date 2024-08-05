package com.example.asteroidradar.data.repository

import com.example.asteroidradar.data.remote.AstronomyPictureOfTheDayNetwork
import com.example.asteroidradar.data.remote.NeoApiService
import com.example.asteroidradar.data.remote.AsteroidsNetwork

interface AsteroidsRadarRepository {
    suspend fun getNearEarthObjects(): AsteroidsNetwork
    suspend fun getPictureOfTheDay(): AstronomyPictureOfTheDayNetwork
}

class AsteroidRadarRepositoryImpl (
    private val neoApiService: NeoApiService
): AsteroidsRadarRepository {
    override suspend fun getNearEarthObjects(): AsteroidsNetwork {
        return neoApiService.getNearEarthObjects("2024-08-01", "2024-08-01")
    }

    override suspend fun getPictureOfTheDay(): AstronomyPictureOfTheDayNetwork {
        return neoApiService.getPictureOfTheDay()
    }
}