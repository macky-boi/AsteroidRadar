package com.example.asteroidradar

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import com.example.asteroidradar.data.workers.UpdateAsteroidsWorker
import junit.framework.Assert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

private const val TAG = "UpdateAsteroidsWorkerInstrumentedTest"

class UpdateAsteroidsWorkerInstrumentedTest {
    private lateinit var context: Context
    private lateinit var workManager: WorkManager

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        WorkManagerTestInitHelper.initializeTestWorkManager(context)
        workManager = WorkManager.getInstance(context)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testPeriodicallyUpdateAsteroids() {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<UpdateAsteroidsWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS)
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                10, TimeUnit.MINUTES
            ).build()

        val testDriver = WorkManagerTestInitHelper.getTestDriver(context)
        requireNotNull(testDriver) { "TestDriver is required for controlling WorkManager" }

        runBlocking {
            workManager.enqueueUniquePeriodicWork(
                UPDATE_ASTEROIDS_WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                periodicWorkRequest
            )

            var workInfo = workManager.getWorkInfosForUniqueWork(UPDATE_ASTEROIDS_WORK_NAME).get()
            Log.i(TAG, "(BEFORE) workInfo: ${workInfo})")
            assert(workInfo.first().state == WorkInfo.State.ENQUEUED)

            val periodicWorkRequestId = workInfo.first().id
            testDriver.setAllConstraintsMet(periodicWorkRequestId)

            workInfo = workManager.getWorkInfosForUniqueWork(UPDATE_ASTEROIDS_WORK_NAME).get()
            Log.i(TAG, "(AFTER) workInfo: ${workInfo})")
            assert(workInfo.first().state == WorkInfo.State.RUNNING)
        }
    }
}