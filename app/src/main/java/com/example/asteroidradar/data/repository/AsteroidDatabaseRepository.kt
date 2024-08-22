package com.example.asteroidradar.data.repository

import com.example.asteroidradar.data.local.Asteroid
import com.example.asteroidradar.data.local.AsteroidDao
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface AsteroidDatabaseRepository  {
    fun getAsteroid(id: String): Flow<Asteroid>
    fun getAllAsteroids(): Flow<List<Asteroid>>
    suspend fun insertAsteroids(asteroidEntities: List<Asteroid>)
    suspend fun deleteAllAsteroidsFromThePast()
    suspend fun isDatabaseEmpty(): Boolean
    suspend fun getCount(): Int
    suspend fun getLatestDate(): Date?
}

private const val TAG = "AsteroidDatabaseRepositoryImpl"

class AsteroidDatabaseRepositoryImpl  (
    private val asteroidDao: AsteroidDao
): AsteroidDatabaseRepository {

    override fun getAsteroid(id: String): Flow<Asteroid> = asteroidDao.getAsteroid(id)


    override fun getAllAsteroids(): Flow<List<Asteroid>> = asteroidDao.getAllAsteroids()


    override suspend fun insertAsteroids(asteroidEntities: List<Asteroid>) = asteroidDao.insertAsteroids(asteroidEntities)


    override suspend fun deleteAllAsteroidsFromThePast() {
        asteroidDao.deleteAsteroidsFrom(Date())
    }

    override suspend fun isDatabaseEmpty(): Boolean = asteroidDao.getCount() == 0

    override suspend fun getCount(): Int  = asteroidDao.getCount()

    override suspend fun getLatestDate(): Date? = asteroidDao.getLatestDate()
}