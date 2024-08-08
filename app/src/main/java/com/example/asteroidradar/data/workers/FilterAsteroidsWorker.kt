package com.example.asteroidradar.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.asteroidradar.AsteroidRadarApplication

class FilterAsteroidsWorker(
    ctx: Context,
    params: WorkerParameters,
): CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        val appContext = applicationContext  as AsteroidRadarApplication

        val repository = appContext.container.asteroidRadarRepository

        return try {
            repository.deleteAllAsteroidsFromThePast()
            Result.success()
        } catch(e: Exception) {
            Log.e("FilterAsteroidsWorker","deleteAllAsteroidsFromThePast error: $e")
            Result.failure()
        }
    }
}