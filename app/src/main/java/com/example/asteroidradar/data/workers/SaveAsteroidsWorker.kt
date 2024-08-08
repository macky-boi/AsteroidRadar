package com.example.asteroidradar.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.asteroidradar.AsteroidRadarApplication
import com.example.asteroidradar.KEY_FETCHED_ASTEROIDS
import com.example.asteroidradar.data.remote.toAsteroidsNetwork

private const val TAG = "SaveAsteroidsWorker"

class SaveAsteroidsWorker(ctx: Context, params: WorkerParameters): CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        Log.e(TAG,"doWork")

        val appContext = applicationContext  as AsteroidRadarApplication
        val databaseRepository = appContext.container.asteroidDatabaseRepository

        // Retrieve the data passed from FetchDataWorker
        val data = inputData.getString(KEY_FETCHED_ASTEROIDS) ?: return Result.failure()
        val asteroidNetwork = data.toAsteroidsNetwork()
        val asteroids = asteroidNetwork.toEntity()

        return try {
            asteroids.forEach { (_, asteroid) ->
                databaseRepository.insertAsteroids(asteroid)
            }
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG,"error saving asteroids: $e" )
            Result.failure()
        }
    }
}
