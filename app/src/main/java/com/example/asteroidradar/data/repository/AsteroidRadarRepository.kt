package com.example.asteroidradar.data.repository

import com.example.asteroidradar.network.NeoApiService

interface AsteroidsRadarRepository {
    suspend fun getNearEarthObjects(): String
}

class AsteroidRadarRepositoryImpl (
    private val neoApiService: NeoApiService
): AsteroidsRadarRepository {
    override suspend fun getNearEarthObjects(): String {
        return neoApiService.getNearEarthObjects("2024-08-01", "2024-08-01")
    }
}