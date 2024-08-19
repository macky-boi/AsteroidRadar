package com.example.asteroidradar.data.repository

import com.example.asteroidradar.data.local.AsteroidEntity
import com.example.asteroidradar.data.local.AsteroidDao
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface AsteroidDatabaseRepository  {
    fun getAsteroid(id: String): Flow<AsteroidEntity>
    fun getAllAsteroids(): Flow<List<AsteroidEntity>>
    suspend fun insertAsteroids(asteroidEntities: List<AsteroidEntity>)
    suspend fun deleteAllAsteroidsFromThePast()
    suspend fun isDatabaseEmpty(): Boolean
}

private const val TAG = "AsteroidDatabaseRepositoryImpl"

class AsteroidDatabaseRepositoryImpl  (
    private val asteroidDao: AsteroidDao
): AsteroidDatabaseRepository {

    override fun getAsteroid(id: String): Flow<AsteroidEntity> = asteroidDao.getAsteroid(id)


    override fun getAllAsteroids(): Flow<List<AsteroidEntity>> = asteroidDao.getAllAsteroids()


    override suspend fun insertAsteroids(asteroidEntities: List<AsteroidEntity>) = asteroidDao.insertAsteroids(asteroidEntities)


    override suspend fun deleteAllAsteroidsFromThePast() {
        asteroidDao.deleteAsteroidsFrom(Date())
    }

    override suspend fun isDatabaseEmpty(): Boolean = asteroidDao.getCount() == 0

}