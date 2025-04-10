package com.example.asteroidradar.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.asteroidradar.data.local.asteroid.AsteroidEntity
import com.example.asteroidradar.data.local.asteroid.AsteroidDao
import com.example.asteroidradar.data.remote.NearEarthObjects
import com.example.asteroidradar.data.remote.NeoApiService
import com.example.asteroidradar.data.remote.PictureOfTheDay
import com.example.asteroidradar.utils.DateUtilities

private const val TAG = "AsteroidRadarRepository"
private const val NUMBER_OF_DAYS = 7

interface AsteroidRadarRepository {
    suspend fun initializeAsteroids()
    fun getAllAsteroids(): LiveData<List<AsteroidEntity>>
    suspend fun getPictureOfTheDay(): PictureOfTheDay?
    fun getAsteroid(id: String): LiveData<AsteroidEntity>
}

class AsteroidRadarRepositoryImpl(
    private val asteroidDao: AsteroidDao,
    private val neoApiService: NeoApiService,
): AsteroidRadarRepository {

    private suspend fun fetchNearEarthObjects(startDate: String, endDate: String): Result<NearEarthObjects> =
        runCatching { neoApiService.getNearEarthObjects(startDate, endDate) }


    override fun getAllAsteroids() = asteroidDao.getAllAsteroids()

    override fun getAsteroid(id: String): LiveData<AsteroidEntity> =
        asteroidDao.getAsteroid(id)

    private suspend fun fetchPictureOfTheDay(): Result<PictureOfTheDay> =
         runCatching { neoApiService.getPictureOfTheDay() }


    override suspend fun getPictureOfTheDay(): PictureOfTheDay? =
        fetchPictureOfTheDay().getOrElse { error ->
            Log.e(TAG, "Failed to fetch Picture of the Day: ${error.localizedMessage}", error)
            null
        }


    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun deleteAsteroidsFromThePast() {
        val today = DateUtilities.getCurrentDateUSTimeZone()
        asteroidDao.deleteAsteroidsFrom(today)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun initializeAsteroids() {
        deleteAsteroidsFromThePast()

        val latestDbDate = asteroidDao.getLatestDate()
        val latestDate = latestDbDate?.let { DateUtilities.dateToString(it) }
            ?: DateUtilities.presentDateString()

        val endDate = DateUtilities.getFutureDateString(NUMBER_OF_DAYS)

        if (latestDate == endDate) return

        val nearEarthObjectsResult = fetchNearEarthObjects(latestDate, endDate)
        nearEarthObjectsResult.onSuccess { nearEarthObjects ->
            nearEarthObjects.toEntity().forEach { (_, asteroid) ->
                asteroidDao.insertAsteroids(asteroid)
            }
        }.onFailure { e ->
            Log.e(TAG, "Failed to fetch NearEarthObjects from $latestDate to $endDate\".", e)
            throw e
        }
    }
}