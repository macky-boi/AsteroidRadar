package com.example.asteroidradar.data.repository

import com.example.asteroidradar.data.remote.AsteroidsNetwork
import com.example.asteroidradar.data.remote.AstronomyPictureOfTheDayNetwork
import com.example.asteroidradar.data.remote.NeoApiService

interface AsteroidNetworkRepository {
    suspend fun fetchNearEarthObjects(): AsteroidsNetwork
    suspend fun fetchPictureOfTheDay(): AstronomyPictureOfTheDayNetwork
}

class AsteroidNetworkRepositoryImpl(
    private val neoApiService: NeoApiService
): AsteroidNetworkRepository {

    override suspend fun fetchNearEarthObjects(): AsteroidsNetwork {
        return neoApiService.getNearEarthObjects("2024-08-01", "2024-08-01")
    }

    override suspend fun fetchPictureOfTheDay(): AstronomyPictureOfTheDayNetwork {
        return neoApiService.getPictureOfTheDay()
    }
}