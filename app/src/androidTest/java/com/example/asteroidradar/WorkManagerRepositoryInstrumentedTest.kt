package comprivate

import android.R.id.input
import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.WorkManagerTestInitHelper
import com.example.asteroidradar.UPDATE_ASTEROIDS_WORK_NAME
import com.example.asteroidradar.data.repository.WorkManagerRepositoryImpl
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test


private const val TAG = "WorkManagerRepositoryInstrumentedTest"

class WorkManagerRepositoryInstrumentedTest {
    private lateinit var context: Context

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testPeriodicallyUpdateAsteroids() = runBlocking {
        context = ApplicationProvider.getApplicationContext()

        // Initialize WorkManager for testing
        WorkManagerTestInitHelper.initializeTestWorkManager(context)

        val repository = WorkManagerRepositoryImpl(context)

        // Start the periodic update (this schedules the work)
        repository.periodicallyUpdateAsteroids()

        var workInfo = WorkManager.getInstance(context)
            .getWorkInfosForUniqueWork(UPDATE_ASTEROIDS_WORK_NAME)
            .get()

        Log.i(TAG, "(BEFORE) workInfo: ${workInfo})")

        val testDriver = WorkManagerTestInitHelper.getTestDriver(context)
        // Tells the testing framework that all constraints are met.
        testDriver?.setAllConstraintsMet(workInfo.first().id)
        // Tells the testing framework the period delay is met
        testDriver?.setPeriodDelayMet(workInfo.first().id)

        workInfo = WorkManager.getInstance(context)
            .getWorkInfosForUniqueWork(UPDATE_ASTEROIDS_WORK_NAME)
            .get()

        Log.i(TAG, "(AFTER) workInfo: ${workInfo})")

        assertTrue(workInfo.first()?.state == WorkInfo.State.SUCCEEDED)
    }
}
