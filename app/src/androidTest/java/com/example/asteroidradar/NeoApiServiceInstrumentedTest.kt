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
        val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        try {
            val response = container.asteroidRadarRepository.getNearEarthObjects() // Get the raw response as a string
            Log.i("NeoApiServiceInstrumentedTest", "getNearEarthObjects | response: ${response.toString()}")
            assert(response.nearEarthObjects.isNotEmpty())
        } catch (e: HttpException) {
            fail("HTTP error: ${e.message}")
        } catch (e: Exception) {
            fail("Unexpected error: ${e.message}")
        }
    }

    fun getNearEarthObjectsNetworkRequest() {
        val container = DefaultAppContainer()
        val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        appScope.launch {
            var networkRequest_success = false
            try {
                val response = container.asteroidRadarRepository.getNearEarthObjects() // Get the raw response as a string
                println("getNearEarthObjects | response: ${response.toString()}")
                networkRequest_success = true
            } catch (e: Exception) {
                println("getNearEarthObjects | Unexpected error: ${e.message}")
            }
            assert(networkRequest_success)
        }
    }
}

