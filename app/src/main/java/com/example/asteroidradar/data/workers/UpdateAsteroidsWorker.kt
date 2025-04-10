package com.example.asteroidradar.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.asteroidradar.AsteroidRadarApplication

private const val TAG = "UpdateAsteroidsWorker"

class UpdateAsteroidsWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    private val appContext = applicationContext as AsteroidRadarApplication
    private val asteroidRadarRepository = appContext.container.asteroidRadarRepository

    override suspend fun doWork(): Result {

        try {
            asteroidRadarRepository.initializeAsteroids()
        } catch (e: Exception) {
            Log.e(TAG, "UpdateAsteroidsWorker | Error initializing asteroids", e)
            return Result.failure()
        }

        return Result.success()
    }
}