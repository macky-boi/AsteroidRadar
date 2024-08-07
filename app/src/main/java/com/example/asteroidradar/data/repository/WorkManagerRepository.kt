package com.example.asteroidradar.data.repository

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.asteroidradar.data.workers.FetchAsteroidsWorker
import java.util.concurrent.TimeUnit

interface WorkManagerRepository {
    fun periodically_fetch_asterpoids()
}

class WorkManagerRepositoryImpl(context: Context): WorkManagerRepository {
    private val workManager = WorkManager.getInstance(context)

    override fun periodically_fetch_asterpoids() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<FetchAsteroidsWorker>(
            repeatInterval = 24,
            repeatIntervalTimeUnit = TimeUnit.HOURS)
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                10, TimeUnit.MINUTES
            ).build()

        workManager.enqueueUniquePeriodicWork(
            "unique_fetch_asteroids_worker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }


}