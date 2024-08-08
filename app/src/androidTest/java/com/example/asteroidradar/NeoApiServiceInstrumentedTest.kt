package com.example.asteroidradar

import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.example.asteroidradar.data.DefaultAppContainer
import com.example.asteroidradar.data.repository.AsteroidNetworkRepository
import junit.framework.TestCase.fail
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class NeoApiServiceInstrumentedTest {
    lateinit var context: Context
    lateinit var container: DefaultAppContainer
    lateinit var networkRepository: AsteroidNetworkRepository

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        container = DefaultAppContainer(context)
    }

    @Test
    fun testGetNearEarthObjects() = runBlocking {
        try {
            val response = networkRepository.fetchNearEarthObjects()
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

