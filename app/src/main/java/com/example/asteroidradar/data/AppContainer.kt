package com.example.asteroidradar.data

import com.example.asteroidradar.data.repository.AsteroidsRadarRepository
import retrofit2.Retrofit

interface AppContainer {
    val asteroidRadarRepository: AsteroidsRadarRepository
}

class DefaultAppContainer: AppContainer {

    private val baseUrl = "http://neo.jpl.nasa.gov"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()
}