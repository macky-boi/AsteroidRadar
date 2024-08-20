package com.example.asteroidradar.data.repository

import android.util.Log
import com.example.asteroidradar.data.local.AsteroidEntity
import com.example.asteroidradar.ui.model.AstronomyPictureOfTheDay
import kotlinx.coroutines.flow.Flow

private const val TAG = "AsteroidRepository"

class AsteroidRepository(
    private val databaseRepository: AsteroidDatabaseRepository,
    private val networkRepository: AsteroidNetworkRepository
) {

    fun getAsteroids(): Flow<List<AsteroidEntity>> =
        databaseRepository.getAllAsteroids()


    suspend fun fetchAndSaveAsteroidsIfDatabaseIsEmpty() {
        if (databaseRepository.isDatabaseEmpty()) {
            val networkAsteroidsResponse = networkRepository.fetchNearEarthObjects()
            networkAsteroidsResponse.onSuccess {
                val networkAsteroids = it
                val asteroidsEntity = networkAsteroids.toEntity()
                asteroidsEntity.forEach { (_, asteroid) ->
                    databaseRepository.insertAsteroids(asteroid)
                }
            }.onFailure { exception ->
                val errorMessage = exception.localizedMessage
                Log.e(TAG, errorMessage ?: exception.toString())
            }
        }
    }

    suspend fun getPictureOfTheDay(): AstronomyPictureOfTheDay =
        networkRepository.fetchPictureOfTheDay().toModel()

}