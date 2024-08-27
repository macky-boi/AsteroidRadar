package com.example.asteroidradar.data.repository

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.asteroidradar.UPDATE_ASTEROIDS_WORK_NAME
import com.example.asteroidradar.data.workers.UpdateAsteroidsWorker
import java.util.concurrent.TimeUnit

interface WorkManagerRepository {
    fun periodicallyUpdateAsteroids()
}

class WorkManagerRepositoryImpl(context: Context): WorkManagerRepository {
    private val workManager = WorkManager.getInstance(context)

    override fun periodicallyUpdateAsteroids() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<UpdateAsteroidsWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS)
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                10, TimeUnit.MINUTES
            ).build()

        workManager.enqueueUniquePeriodicWork(
            UPDATE_ASTEROIDS_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}