package com.example.asteroidradar

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.example.asteroidradar.data.DefaultAppContainer
import junit.framework.TestCase.fail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.HttpException

class NeoApiServiceInstrumentedTest {

    @Test
    fun testGetNearEarthObjects() = runBlocking {
        val container = DefaultAppContainer()

        try {
            val response = container.asteroidRadarRepository.getNearEarthObjects()
            Log.i("NeoApiServiceInstrumentedTest", "getNearEarthObjects(): $response")
            assert(response.nearEarthObjects.isNotEmpty())
        } catch (e: HttpException) {
            fail("HTTP error: ${e.message}")
        } catch (e: Exception) {
            fail("Unexpected error: ${e.message}")
        }
    }

    @Test
    fun testGetNearEarthObjectsNetworkRequest() = runBlocking {
        val container = DefaultAppContainer()

        try {
            val response = container.asteroidRadarRepository.getPictureOfTheDay()
            Log.i("NeoApiServiceInstrumentedTest", "getPictureOfTheDay(): $response")
            assert(response.title.isNotEmpty())
        } catch (e: HttpException) {
            fail("HTTP error: ${e.message}")
        } catch (e: Exception) {
            fail("Unexpected error: ${e.message}")
        }
    }
}

