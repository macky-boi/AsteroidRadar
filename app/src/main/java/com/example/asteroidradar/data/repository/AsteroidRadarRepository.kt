package com.example.asteroidradar.data.repository

import com.example.asteroidradar.data.local.Asteroid
import com.example.asteroidradar.data.local.AsteroidDao
import com.example.asteroidradar.data.remote.AstronomyPictureOfTheDayNetwork
import com.example.asteroidradar.data.remote.NeoApiService
import com.example.asteroidradar.data.remote.AsteroidsNetwork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface AsteroidsRadarRepository {
    suspend fun fetchNearEarthObjectsFromNetwork(): AsteroidsNetwork
    suspend fun fetchPictureOfTheDayFromNetwork(): AstronomyPictureOfTheDayNetwork
    fun getAsteroid(id: String): Flow<Asteroid>
    fun getAllAsteroids(): Flow<List<Asteroid>>
    suspend fun insertAsteroids(asteroids: List<Asteroid>)
    suspend fun deleteAsteroids(asteroids: List<Asteroid>)
}

class AsteroidRadarRepositoryImpl (
    private val neoApiService: NeoApiService,
    private val asteroidDao: AsteroidDao
): AsteroidsRadarRepository {

    override suspend fun fetchNearEarthObjectsFromNetwork(): AsteroidsNetwork {
        return neoApiService.getNearEarthObjects("2024-08-01", "2024-08-01")
    }

    override suspend fun fetchPictureOfTheDayFromNetwork(): AstronomyPictureOfTheDayNetwork {
        return neoApiService.getPictureOfTheDay()
    }

    override fun getAsteroid(id: String): Flow<Asteroid> {
        return asteroidDao.getAsteroid(id)
    }

    override fun getAllAsteroids(): Flow<List<Asteroid>> {
        return asteroidDao.getAllAsteroids()
    }

    override suspend fun insertAsteroids(asteroids: List<Asteroid>) {
        asteroidDao.insertAsteroids(asteroids)
    }

    override suspend fun deleteAsteroids(asteroids: List<Asteroid>) {
        asteroidDao.deleteAsteroids(asteroids)
    }
}