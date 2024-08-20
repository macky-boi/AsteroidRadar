package com.example.asteroidradar.data.repository

import com.example.asteroidradar.data.remote.AsteroidsNetwork
import com.example.asteroidradar.data.remote.AstronomyPictureOfTheDayNetwork
import com.example.asteroidradar.data.remote.NeoApiService
import com.example.asteroidradar.ui.model.AstronomyPictureOfTheDay
import retrofit2.HttpException
import java.io.IOException

interface AsteroidNetworkRepository {
    suspend fun fetchNearEarthObjects(): Result<AsteroidsNetwork>
    suspend fun fetchPictureOfTheDay(): AstronomyPictureOfTheDay
}

class AsteroidNetworkRepositoryImpl(
    private val neoApiService: NeoApiService
): AsteroidNetworkRepository {

    override suspend fun fetchNearEarthObjects(): Result<AsteroidsNetwork> {
        return try {
            val response = neoApiService.getNearEarthObjects("2024-08-01", "2024-08-01")
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchPictureOfTheDay(): AstronomyPictureOfTheDay {
        return neoApiService.getPictureOfTheDay().toModel()
    }
}