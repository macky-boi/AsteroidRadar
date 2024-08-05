package com.example.asteroidradar

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.example.asteroidradar.data.DefaultAppContainer
import junit.framework.TestCase.fail
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.HttpException

class NeoApiServiceInstrumentedTest {

    @Test
    fun testGetNearEarthObjects() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val container = DefaultAppContainer(context)

        try {
            val response = container.asteroidRadarRepository.fetchNearEarthObjectsFromNetwork()
            Log.i("NeoApiServiceInstrumentedTest", "getNearEarthObjects(): $response")
            assert(response.asteroids.isNotEmpty())
        } catch (e: HttpException) {
            fail("HTTP error: ${e.message}")
        } catch (e: Exception) {
            fail("Unexpected error: ${e.message}")
        }
    }

    @Test
    fun testGetNearEarthObjectsNetworkRequest() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val container = DefaultAppContainer(context)

        try {
            val response = container.asteroidRadarRepository.fetchPictureOfTheDayFromNetwork()
            Log.i("NeoApiServiceInstrumentedTest", "getPictureOfTheDay(): $response")
            assert(response.title.isNotEmpty())
        } catch (e: HttpException) {
            fail("HTTP error: ${e.message}")
        } catch (e: Exception) {
            fail("Unexpected error: ${e.message}")
        }
    }
}

