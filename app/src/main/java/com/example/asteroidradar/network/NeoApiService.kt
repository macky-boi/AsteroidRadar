package com.example.asteroidradar.network

import retrofit2.http.GET
import retrofit2.http.Query

interface NeoApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getNearEarthObjects(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = "DEMO_KEY"
    ): Asteroids

    @GET("/planetary/apod")
    suspend fun getPictureOfTheDay(
        @Query("thumbs") thumbs: Boolean = true,
        @Query("api_key") apiKey: String = "DEMO_KEY"
    ): AstronomyPictureOfTheDay
}