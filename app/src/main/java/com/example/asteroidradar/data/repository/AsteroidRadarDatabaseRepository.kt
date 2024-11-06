package com.example.asteroidradar.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.asteroidradar.data.local.asteroid.AsteroidEntity
import com.example.asteroidradar.data.local.asteroid.AsteroidDao
import com.example.asteroidradar.data.local.pictureOfTheDay.PictureOfTheDay
import com.example.asteroidradar.data.local.pictureOfTheDay.PictureOfTheDayDao
import com.example.asteroidradar.data.remote.NearEarthObjects
import com.example.asteroidradar.data.remote.NeoApiService
import com.example.asteroidradar.data.remote.PictureOfTheDayNetwork
import com.example.asteroidradar.utils.DateUtilities
import retrofit2.HttpException

private const val TAG = "AsteroidRadarRepository"
private const val NUMBER_OF_DAYS = 7

interface AsteroidRadarRepository {
//    suspend fun initializePictureOfTheDay()
    suspend fun initializeAsteroids()
    fun getAllAsteroids(): LiveData<List<AsteroidEntity>>
    suspend fun getPictureOfTheDay(): PictureOfTheDay?
    fun getAsteroid(id: String): LiveData<AsteroidEntity>
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

    override fun getAsteroid(id: String): LiveData<AsteroidEntity> = asteroidDao.getAsteroid(id)

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

//    @RequiresApi(Build.VERSION_CODES.O)
//    override suspend fun initializePictureOfTheDay() {
//        Log.i(TAG, "initializePictureOfTheDay")
//        val today = DateUtilities.getCurrentDateUSTimeZone()
//
//        var pictureOfTheDay = pictureOfTheDayDao.getPictureByDate(today)
//        Log.i(TAG, "pictureOfTheDay (before deletePictures): ${pictureOfTheDay.value}")
//        var count = pictureOfTheDayDao.getCount()
//        Log.i(TAG, "count (before deletion): $count")
//
//        pictureOfTheDayDao.deletePictureFrom(today)
//
//        pictureOfTheDay = pictureOfTheDayDao.getPictureByDate(today)
//        Log.i(TAG, "pictureOfTheDay (after deletePictures): ${pictureOfTheDay.value}")
//        count = pictureOfTheDayDao.getCount()
//        Log.i(TAG, "count (after deletion): $count")
//
//        if (pictureOfTheDay.value == null) {
//            fetchPictureOfTheDayNetwork().onSuccess {
//                Log.i(TAG, "inserting pictureOfTheDay: $it")
//                pictureOfTheDayDao.insertPicture(it.toEntity())
//
//                count = pictureOfTheDayDao.getCount()
//                Log.i(TAG, "count (after insertion): $count")
//            }.onFailure { e ->
//                Log.e(TAG, e.localizedMessage ?: "Failed to fetch pictureOfTheDay")
//                throw e
//            }
//        }
//    }

    override fun getAllAsteroids() = asteroidDao.getAllAsteroids()


    override suspend fun getPictureOfTheDay(): PictureOfTheDay? {
        var result: PictureOfTheDay? = null
        fetchPictureOfTheDayNetwork().onSuccess {
            result =  it.toEntity()
        }.onFailure {
            Log.e(TAG, it.localizedMessage ?: "Failed to fetch pictureOfTheDay")
        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun deleteAsteroidsFromThePast() {
        Log.i(TAG, "deleteAsteroidsFromThePast")

        val usDate = DateUtilities.getCurrentDateUSTimeZone()
        Log.i(TAG, "usDate: $usDate")

        asteroidDao.deleteAsteroidsFrom(usDate)
    }

    private suspend fun isAsteroidsEmpty(): Boolean {
        return asteroidDao.getCount() == 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun initializeAsteroids() {
        Log.i(TAG, "initializeAsteroids")
        deleteAsteroidsFromThePast()

        val latestDate = if (isAsteroidsEmpty()) {
            Log.i(TAG, "asteroid database is empty")
            DateUtilities.presentDateString()
        } else {
            Log.i(TAG, "asteroid database is not empty")
            val latestDate = asteroidDao.getLatestDate()!!
            DateUtilities.dateToString(latestDate)
        }
        Log.i(TAG, "startDate: $latestDate")

        val endDate = DateUtilities.getFutureDateString(NUMBER_OF_DAYS)
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