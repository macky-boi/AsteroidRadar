package com.example.asteroidradar

import com.example.asteroidradar.data.remote.NeoApiService
import com.example.asteroidradar.utils.DateUtils
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import junit.framework.TestCase.fail
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class ApiServiceInstrumentedTest {
    private lateinit var service: NeoApiService

    @Before
    fun setUp() {
        val baseUrl = "https://api.nasa.gov/"

        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }

        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory(contentType))
            .baseUrl(baseUrl)
            .build()
        service = retrofit.create(NeoApiService::class.java)
    }

    @Test
    fun testGetNearEarthObjects(): Unit = runBlocking {
        val todayString = DateUtils().presentDateString()
        val endDateString = DateUtils().getFutureDateString(DateUtils.NUMBER_OF_DAYS)

        try {
            val nearEarthObjects = service.getNearEarthObjects(todayString, endDateString)
            assert(nearEarthObjects.asteroids.isNotEmpty())
        } catch(e: Exception) {
            fail("error fetching nearEarthObjects: ${e.localizedMessage}")
        }
    }

    @Test
    fun testGetPictureOfTheDay(): Unit = runBlocking {
        try {
            val pictureOfTheDayNetwork = service.getPictureOfTheDay()
            assert(pictureOfTheDayNetwork.url.isNotEmpty())
        } catch(e: Exception) {
            fail("error fetching pictureOfTheDayNetwork: ${e.localizedMessage}")
        }
    }
}

