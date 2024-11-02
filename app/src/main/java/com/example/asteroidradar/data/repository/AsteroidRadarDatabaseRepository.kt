package com.example.asteroidradar.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.asteroidradar.data.local.asteroid.Asteroid
import com.example.asteroidradar.data.local.asteroid.AsteroidDao
import com.example.asteroidradar.data.local.pictureOfTheDay.PictureOfTheDay
import com.example.asteroidradar.data.local.pictureOfTheDay.PictureOfTheDayDao
import com.example.asteroidradar.data.remote.NearEarthObjects
import com.example.asteroidradar.data.remote.NeoApiService
import com.example.asteroidradar.data.remote.PictureOfTheDayNetwork
import com.example.asteroidradar.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.util.Date

private const val TAG = "AsteroidRadarRepository"
private const val NUMBER_OF_DAYS = 7

interface AsteroidRadarRepository {
    suspend fun initializePictureOfTheDay()
    suspend fun initializeAsteroids()
    fun getAllAsteroids(): LiveData<List<Asteroid>>
    fun getPictureOfTheDay(): LiveData<PictureOfTheDay?>
    fun getAsteroid(id: String): LiveData<Asteroid>
}

class AsteroidRadarRepositoryImpl(
    private val asteroidDao: AsteroidDao,
    private val pictureOfTheDayDao: PictureOfTheDayDao,
    private val neoApiService: NeoApiService,
): AsteroidRadarRepository {
    private suspend fun fetchPictureOfTheDayNetwork(): Result<PictureOfTheDayNetwork> {
        return try {
            val response = neoApiService.getPictureOfTheDay()
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAsteroid(id: String): LiveData<Asteroid> = asteroidDao.getAsteroid(id)

    private suspend fun fetchNearEarthObjects(startDate: String, endDate: String): Result<NearEarthObjects> {
        return try {
            val response = neoApiService.getNearEarthObjects(startDate, endDate)
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun initializePictureOfTheDay() {
        Log.i(TAG, "initializePictureOfTheDay")
        val today = Date()

        pictureOfTheDayDao.deletePictureFrom(today)

        val pictureOfTheDay = pictureOfTheDayDao.getPictureByDate(today)
        Log.i(TAG, "pictureOfTheDay: ${pictureOfTheDay.value}")

        if (pictureOfTheDay.value == null) {
            fetchPictureOfTheDayNetwork().onSuccess {
                Log.i(TAG, "inserting pictureOfTheDay: $it")
                pictureOfTheDayDao.insertPicture(it.toEntity())
            }.onFailure { e ->
                Log.e(TAG, e.localizedMessage ?: "Failed to fetch pictureOfTheDay")
                throw e
            }
        }
    }

    override fun getAllAsteroids() = asteroidDao.getAllAsteroids()

    override fun getPictureOfTheDay(): LiveData<PictureOfTheDay?> = pictureOfTheDayDao.getPictureByDate(Date())

    private suspend fun deleteAsteroidsFromThePast() = asteroidDao.deleteAsteroidsFrom(Date())

    private suspend fun isAsteroidsEmpty(): Boolean {
        return asteroidDao.getCount() == 0
    }

    override suspend fun initializeAsteroids() {
        Log.i(TAG, "initializeAsteroids")
        deleteAsteroidsFromThePast()

        val latestDate = if (isAsteroidsEmpty()) {
            Log.i(TAG, "asteroid database is empty")
            DateUtils().presentDateString()
        } else {
            Log.i(TAG, "asteroid database is not empty")
            val latestDate = asteroidDao.getLatestDate()!!
            DateUtils().dateToString(latestDate)
        }
        Log.i(TAG, "startDate: $latestDate")

        val endDate = DateUtils().getFutureDateString(NUMBER_OF_DAYS)
        Log.i(TAG, "endDate: $endDate")

        if (latestDate != endDate) {
            val nearEarthObjectsResult = fetchNearEarthObjects(latestDate, endDate)
            nearEarthObjectsResult.onSuccess { nearEarthObjects ->
                nearEarthObjects.toEntity().forEach { (_, asteroid) ->
                    Log.i(TAG, "inserting asteroids: $asteroid")
                    asteroidDao.insertAsteroids(asteroid)
                }
            }.onFailure {e ->
                Log.e(TAG, e.localizedMessage ?: "Failed to fetch nearEarthObjects")
                throw e
            }
        }
    }


}