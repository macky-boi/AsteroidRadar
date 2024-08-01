package com.example.asteroidradar.data.repository

import com.example.asteroidradar.network.NeoApiService

interface AsteroidsRadarRepository {
    suspend fun getNearEarthObjects(): String
}

class AsteroidRadarRepositoryImpl(
    private val neoApiService: NeoApiService
) {

}