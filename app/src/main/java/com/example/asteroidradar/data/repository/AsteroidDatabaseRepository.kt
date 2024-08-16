package com.example.asteroidradar.data.repository

import android.util.Log
import com.example.asteroidradar.data.local.Asteroid
import com.example.asteroidradar.data.local.AsteroidDao
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface AsteroidDatabaseRepository  {
    fun getAsteroid(id: String): Flow<Asteroid>
    fun getAllAsteroids(): Flow<List<Asteroid>>
    suspend fun insertAsteroids(asteroids: List<Asteroid>)
    suspend fun deleteAllAsteroidsFromThePast()
}

private const val TAG = "AsteroidDatabaseRepositoryImpl"

class AsteroidDatabaseRepositoryImpl  (
    private val asteroidDao: AsteroidDao
): AsteroidDatabaseRepository {

    override fun getAsteroid(id: String): Flow<Asteroid> {
        return asteroidDao.getAsteroid(id)
    }

    override fun getAllAsteroids(): Flow<List<Asteroid>> {
        return asteroidDao.getAllAsteroids()
    }

    override suspend fun insertAsteroids(asteroids: List<Asteroid>) {
        asteroidDao.insertAsteroids(asteroids)
    }

    override suspend fun deleteAllAsteroidsFromThePast() {
        Log.i(TAG,"(started) deleteAllAsteroidsFromThePast")
        val currentDate = Date()
        asteroidDao.deleteAsteroidsFromThePast(currentDate)
        Log.i(TAG,"(finished) deleteAllAsteroidsFromThePast")
    }
}