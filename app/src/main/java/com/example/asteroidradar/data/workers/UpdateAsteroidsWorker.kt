package com.example.asteroidradar.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.await
import com.example.asteroidradar.AsteroidRadarApplication
import com.example.asteroidradar.FETCH_ASTEROIDS_WORK_NAME
import com.example.asteroidradar.FILTER_ASTEROIDS_WORK_NAME
import com.example.asteroidradar.SAVE_ASTEROIDS_WORK_NAME

private const val TAG = "UpdateAsteroidsWorker"

class UpdateAsteroidsWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        Log.i("UpdateAsteroidsWorker", "doWork")

        val appContext = applicationContext as AsteroidRadarApplication
        val workManager = WorkManager.getInstance(appContext)

        val filterWorkRequest = OneTimeWorkRequestBuilder<FilterAsteroidsWorker>()
            .addTag(FILTER_ASTEROIDS_WORK_NAME)
            .build()

        val fetchWorkRequest = OneTimeWorkRequestBuilder<FetchAsteroidsWorker>()
            .addTag(FETCH_ASTEROIDS_WORK_NAME)
            .build()

        val saveWorkRequest = OneTimeWorkRequestBuilder<SaveAsteroidsWorker>()
            .addTag(SAVE_ASTEROIDS_WORK_NAME)
            .build()

        return try {
            val workContinuation = workManager.beginWith(filterWorkRequest)
                .then(fetchWorkRequest)
                .then(saveWorkRequest)

            workContinuation.enqueue()

            return Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error executing chain of workers: $e")
            Result.failure()
        }

    }
}