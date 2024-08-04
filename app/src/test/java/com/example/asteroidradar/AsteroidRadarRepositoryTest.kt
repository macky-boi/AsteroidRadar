package com.example.asteroidradar

import android.util.Log
import com.example.asteroidradar.data.AppContainer
import com.example.asteroidradar.data.DefaultAppContainer
import com.example.asteroidradar.network.NeoFeedResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.junit.Test

class AsteroidRadarRepositoryTest {
    @Test
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