package com.example.asteroidradar

import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.testing.WorkManagerTestInitHelper
import com.example.asteroidradar.data.DefaultAppContainer
import com.example.asteroidradar.data.repository.AsteroidNetworkRepository
import com.example.asteroidradar.domain.FetchAsteroidsUseCase
import junit.framework.TestCase.fail
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ApiServiceInstrumentedTest {
    private lateinit var context: Context
    private lateinit var container: DefaultAppContainer
    private lateinit var networkRepository: AsteroidNetworkRepository

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        container = DefaultAppContainer(context)
        WorkManagerTestInitHelper.initializeTestWorkManager(context)
        networkRepository = container.asteroidNetworkRepository
    }

    @Test
    fun testGetNearEarthObjects(): Unit = runBlocking {
        val calendar = Calendar.getInstance()
        val todayString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        calendar.add(Calendar.DAY_OF_YEAR, FetchAsteroidsUseCase.NUMBER_OF_DAYS)
        val endDateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        val response = networkRepository.fetchNearEarthObjects(todayString, endDateString)

        Log.i("NeoApiServiceInstrumentedTest", "getNearEarthObjects(): $response")
        response.onSuccess { asteroidsNetwork ->
            assert(asteroidsNetwork.asteroids.isNotEmpty())
        }.onFailure {
            fail("HTTP error: ${it.message}")
        }
    }

    @Test
    fun testGetNearEarthObjectsNetworkRequest() = runBlocking {
        try {
            val response = networkRepository.fetchPictureOfTheDay()
            Log.i("NeoApiServiceInstrumentedTest", "getPictureOfTheDay(): $response")
            assert(response.title.isNotEmpty())
        } catch (e: HttpException) {
            fail("HTTP error: ${e.message}")
        } catch (e: Exception) {
            fail("Unexpected error: ${e.message}")
        }
    }
}

