package com.example.asteroidradar.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.asteroidradar.AsteroidRadarApplication
import com.example.asteroidradar.data.remote.AsteroidNetwork
import com.example.asteroidradar.data.repository.AsteroidsRadarRepository
import retrofit2.HttpException

private const val TAG = "FetchAsteroidsWorker"

class FetchAsteroidsWorker(
    ctx: Context,
    params: WorkerParameters,
): CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        return try {
            val appContext = applicationContext  as AsteroidRadarApplication

            val fetchedAsteroids = appContext.container
                .asteroidRadarRepository
                .fetchNearEarthObjectsFromNetwork()

            val outputData = workDataOf("ASTEROIDS-NETWORK" to fetchedAsteroids.toJson())

            Result.success(outputData)
        } catch (e: HttpException) {
            Log.i(TAG,"HTTP error: ${e.message}")
            Result.failure()
        } catch (e: Exception) {
            Log.i(TAG,"Unexpected error: ${e.message}")
            Result.failure()
        }
    }
}