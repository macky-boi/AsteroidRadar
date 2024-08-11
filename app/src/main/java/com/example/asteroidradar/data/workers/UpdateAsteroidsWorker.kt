package com.example.asteroidradar.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.asteroidradar.AsteroidRadarApplication

class UpdateAsteroidsWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        Log.i("UpdateAsteroidsWorker", "doWork")
        val appContext = applicationContext as AsteroidRadarApplication
        val workManager = WorkManager.getInstance(appContext)

        val filterWorkRequest = OneTimeWorkRequestBuilder<FilterAsteroidsWorker>()
            .build()

        val fetchWorkRequest = OneTimeWorkRequestBuilder<FetchAsteroidsWorker>()
            .build()

        val saveWorkRequest = OneTimeWorkRequestBuilder<SaveAsteroidsWorker>()
            .build()

        // Chain the workers
        workManager.beginWith(filterWorkRequest)
            .then(fetchWorkRequest)
            .then(saveWorkRequest)
            .enqueue()

        return Result.success()
    }
}