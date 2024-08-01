package com.example.asteroidradar.data

import com.example.asteroidradar.data.repository.AsteroidRadarRepositoryImpl
import com.example.asteroidradar.data.repository.AsteroidsRadarRepository
import com.example.asteroidradar.network.NeoApiService
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

interface AppContainer {
    val asteroidRadarRepository: AsteroidsRadarRepository
}

class DefaultAppContainer: AppContainer {

    private val baseUrl = "https://neo.jpl.nasa.gov"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    val service = retrofit.create(NeoApiService::class.java)

    override val asteroidRadarRepository: AsteroidsRadarRepository by lazy {
        AsteroidRadarRepositoryImpl(service)
    }
}