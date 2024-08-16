package com.example.asteroidradar.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.await
import com.example.asteroidradar.AsteroidRadarApplication
import com.example.asteroidradar.FETCH_ASTEROIDS_WORK_NAME
import com.example.asteroidradar.FILTER_ASTEROIDS_WORK_NAME
import com.example.asteroidradar.SAVE_ASTEROIDS_WORK_NAME
import com.example.asteroidradar.data.remote.AsteroidNetwork
import com.example.asteroidradar.data.remote.AsteroidsNetwork
import retrofit2.HttpException
import java.io.IOException

private const val TAG = "UpdateAsteroidsWorker"

class UpdateAsteroidsWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    private val appContext = applicationContext as AsteroidRadarApplication
    private val databaseRepository = appContext.container.asteroidDatabaseRepository
    private val networkRepository = appContext.container.asteroidNetworkRepository

    override suspend fun doWork(): Result {
        Log.i("UpdateAsteroidsWorker", "doWork")

        return try {
            databaseRepository.deleteAllAsteroidsFromThePast()
            val fetchedData = networkRepository.fetchNearEarthObjects()
            val asteroidsEntity = fetchedData.toEntity()
            asteroidsEntity.forEach { (_, asteroid) ->
                databaseRepository.insertAsteroids(asteroid)
            }
            Result.success()
        } catch (httpException: HttpException) {
            Log.e(TAG, "HTTP error: ${httpException.message}")
            Result.failure()
        } catch (e: Exception) {
            Log.e(TAG, "unexpected exception: ${e.message}")
            Result.failure()
        }
    }


}