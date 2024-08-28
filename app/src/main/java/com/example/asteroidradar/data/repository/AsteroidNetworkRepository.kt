package com.example.asteroidradar.data.repository

import com.example.asteroidradar.data.remote.AsteroidsNetwork
import com.example.asteroidradar.data.remote.PictureOfTheDayNetwork
import com.example.asteroidradar.data.remote.NeoApiService
import retrofit2.HttpException

interface AsteroidNetworkRepository {
    suspend fun fetchNearEarthObjects(startDate: String, endDate: String): Result<AsteroidsNetwork>
    suspend fun fetchPictureOfTheDay(): Result<PictureOfTheDayNetwork>
}

class AsteroidNetworkRepositoryImpl(
    private val neoApiService: NeoApiService
): AsteroidNetworkRepository {

    override suspend fun fetchNearEarthObjects(startDate: String, endDate: String): Result<AsteroidsNetwork> {
        return try {
            val response = neoApiService.getNearEarthObjects(startDate, endDate)
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchPictureOfTheDay(): Result<PictureOfTheDayNetwork> {
        return try {
            val response = neoApiService.getPictureOfTheDay()
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}