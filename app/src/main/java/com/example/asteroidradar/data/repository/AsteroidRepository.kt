package com.example.asteroidradar.data.repository

import com.example.asteroidradar.data.local.AsteroidEntity
import com.example.asteroidradar.ui.model.AstronomyPictureOfTheDay
import kotlinx.coroutines.flow.Flow

class AsteroidRepository(
    private val databaseRepository: AsteroidDatabaseRepository,
    private val networkRepository: AsteroidNetworkRepository,
    private val workManagerRepository: WorkManagerRepository
) {

    suspend fun getAsteroids(): Flow<List<AsteroidEntity>> {
        return if (databaseRepository.isDatabaseEmpty()) {
            databaseRepository.getAllAsteroids()
        } else {
            val asteroids = networkRepository.fetchNearEarthObjects()
            val asteroidsEntity = asteroids.toEntity()

            asteroidsEntity.forEach { (_, asteroid) ->
                databaseRepository.insertAsteroids(asteroid)
            }
            databaseRepository.getAllAsteroids()
        }
    }

    suspend fun getPictureOfTheDay(): AstronomyPictureOfTheDay {
        return networkRepository.fetchPictureOfTheDay().toModel()
    }

    fun schedulePeriodicUpdates() {
        workManagerRepository.periodicallyUpdateAsteroids()
    }
}