package com.example.asteroidradar

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.example.asteroidradar.data.workers.FetchAsteroidsWorker
import junit.framework.TestCase.fail
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class WorkerInstrumentatedTest {
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun fetchAsteroidsWorker_doWork_resultSuccess() {
        val worker = TestListenableWorkerBuilder<FetchAsteroidsWorker>(context)
            .build()

        runBlocking {
            try {
                val result = worker.doWork()
                assert(result is ListenableWorker.Result.Success)

                val fetchedData = result.outputData.getString("ASTEROIDS-NETWORK")
                assert(!fetchedData.isNullOrEmpty())
            } catch (e: Exception) {
                fail("Unexpected exception: $e")
            }
        }
    }
}