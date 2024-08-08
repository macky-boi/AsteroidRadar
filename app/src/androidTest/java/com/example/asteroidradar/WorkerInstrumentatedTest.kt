package com.example.asteroidradar

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import com.example.asteroidradar.data.local.Asteroid
import com.example.asteroidradar.data.local.AsteroidDatabase
import com.example.asteroidradar.data.remote.AsteroidNetwork
import com.example.asteroidradar.data.remote.AsteroidsNetwork
import com.example.asteroidradar.data.remote.CloseApproachData
import com.example.asteroidradar.data.remote.MissDistance
import com.example.asteroidradar.data.remote.RelativeVelocity
import com.example.asteroidradar.data.workers.FetchAsteroidsWorker
import com.example.asteroidradar.data.workers.FilterAsteroidsWorker
import com.example.asteroidradar.data.workers.SaveAsteroidsWorker
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class WorkerInstrumentatedTest {
    private lateinit var context: Context

    val sampleAsteroidsNetwork = AsteroidsNetwork(
        asteroids = mapOf(
            "2024-08-01" to listOf(
                AsteroidNetwork(
                    id = "20240801A",
                    name = "Asteroid A",
                    isHazardous = true,
                    absoluteMagnitude = 22.5,
                    closeApproachData = listOf(
                        CloseApproachData(
                            closeApproachDate = "2024-08-01",
                            missDistance = MissDistance(astronomical = "0.05"),
                            relativeVelocity = RelativeVelocity(kilometersPerSecond = "20.5")
                        )
                    )
                )
            )
        )
    )

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

                val fetchedData = result.outputData.getString(KEY_FETCHED_ASTEROIDS)
                assert(!fetchedData.isNullOrEmpty())
            } catch (e: Exception) {
                fail("Unexpected exception: $e")
            }
        }
    }

    @Test
    fun saveAsteroidsWorker_doWork_resultSuccess() {

        // create database
        val asteroidDatabase = Room.inMemoryDatabaseBuilder(context, AsteroidDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()

        val asteroidDao = asteroidDatabase.asteroidDao()

        val worker = TestListenableWorkerBuilder<SaveAsteroidsWorker>(context)
            .setInputData(workDataOf(KEY_FETCHED_ASTEROIDS to sampleAsteroidsNetwork.toJson()))
            .build()

        runBlocking {
            val result = worker.doWork()
            assert(result is ListenableWorker.Result.Success)
        }
    }

    @Test
    fun filterAsteroidWorker_doWork_resultSuccess() {
        val appContext = context as AsteroidRadarApplication
        val repository = appContext.container.asteroidRadarRepository

        val saveAsteroidWorker = TestListenableWorkerBuilder<SaveAsteroidsWorker>(context)
            .setInputData(workDataOf(KEY_FETCHED_ASTEROIDS to sampleAsteroidsNetwork.toJson()))
            .build()

        val filterAsteroidsWorker = TestListenableWorkerBuilder<FilterAsteroidsWorker>(context)
            .setInputData(workDataOf(KEY_FETCHED_ASTEROIDS to sampleAsteroidsNetwork.toJson()))
            .build()

        runBlocking {
            saveAsteroidWorker.doWork()
            var allAsteroids = repository.getAllAsteroids()
            assertEquals(1, allAsteroids.first().size)

            filterAsteroidsWorker.doWork()

            allAsteroids = repository.getAllAsteroids()
            assertEquals(0, allAsteroids.first().size)
        }
    }

}