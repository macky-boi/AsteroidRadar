package com.example.asteroidradar.data.repository

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.asteroidradar.data.workers.UpdateAsteroidsWorker
import java.util.concurrent.TimeUnit

interface WorkManagerRepository {
    fun periodicallyUpdateAsteroids()
}

class WorkManagerRepositoryImpl(context: Context): WorkManagerRepository {
    companion object {
        const val UPDATE_ASTEROIDS_WORK_NAME = "update_asteroids_periodic_work"
        const val REPEAT_INTERVAL_DAYS = 1L
    }

    private val workManager = WorkManager.getInstance(context)

    override fun periodicallyUpdateAsteroids() {
        val workRequest = createPeriodicWorkRequest()

        workManager.enqueueUniquePeriodicWork(
            UPDATE_ASTEROIDS_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    private fun createPeriodicWorkRequest(): PeriodicWorkRequest {
        val constraints = buildWorkConstraints()

        return PeriodicWorkRequestBuilder<UpdateAsteroidsWorker>(
            repeatInterval = REPEAT_INTERVAL_DAYS,
            repeatIntervalTimeUnit = TimeUnit.DAYS)
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                10, TimeUnit.MINUTES
            ).build()
    }

    private fun buildWorkConstraints(): Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()
    }
}