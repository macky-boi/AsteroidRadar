package com.example.asteroidradar.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.asteroidradar.AsteroidRadarApplication
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

private const val TAG = "FilterAsteroidsWorker"

class FilterAsteroidsWorker(
    ctx: Context,
    params: WorkerParameters,
): CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        Log.i(TAG,"doWork")

        val appContext = applicationContext  as AsteroidRadarApplication

        val databaseRepository = appContext.container.asteroidDatabaseRepository

        return try {
            Log.i(TAG,"(started) deleteAllAsteroidsFromThePast")
            databaseRepository.deleteAllAsteroidsFromThePast()
            Log.i(TAG,"(finished) deleteAllAsteroidsFromThePast")
            Result.success()
        } catch(e: Exception) {
            Log.e(TAG,"deleteAllAsteroidsFromThePast error: $e")
            Result.failure()
        }
    }
}