package com.example.asteroidradar.data.repository

import com.example.asteroidradar.data.remote.AsteroidsNetwork
import com.example.asteroidradar.data.remote.AstronomyPictureOfTheDayNetwork
import com.example.asteroidradar.data.remote.NeoApiService
import com.example.asteroidradar.ui.model.AstronomyPictureOfTheDay
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface AsteroidNetworkRepository {
    suspend fun fetchNearEarthObjects(startDate: String, endDate: String): Result<AsteroidsNetwork>
    suspend fun fetchPictureOfTheDay(): AstronomyPictureOfTheDay
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

    override suspend fun fetchPictureOfTheDay(): AstronomyPictureOfTheDay {
        return neoApiService.getPictureOfTheDay().toModel()
    }

}