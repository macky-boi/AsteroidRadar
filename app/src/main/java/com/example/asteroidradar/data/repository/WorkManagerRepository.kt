package com.example.asteroidradar.data.repository

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.asteroidradar.data.workers.FetchAsteroidsWorker
import java.util.concurrent.TimeUnit

interface WorkManagerRepository {
    fun fetchAndSaveAsteroids()
}

class WorkManagerRepositoryImpl(context: Context): WorkManagerRepository {
    private val workManager = WorkManager.getInstance(context)
    val myWorkRequest = OneTimeWorkRequest.from(FetchAsteroidsWorker::class.java)
    val saveRequest = PeriodicWorkRequestBuilder<FetchAsteroidsWorker>(
        repeatInterval = 1,
        repeatIntervalTimeUnit = TimeUnit.HOURS
    )

    override fun fetchAndSaveAsteroids() {
        TODO("Not yet implemented")
    }
}