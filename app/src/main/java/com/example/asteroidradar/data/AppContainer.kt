package com.example.asteroidradar.data

import com.example.asteroidradar.data.repository.AsteroidRadarRepositoryImpl
import com.example.asteroidradar.data.repository.AsteroidsRadarRepository
import com.example.asteroidradar.network.NeoApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val asteroidRadarRepository: AsteroidsRadarRepository
}

class DefaultAppContainer: AppContainer {

    private val baseUrl = "https://api.nasa.gov/"

    private val contentType = "application/json".toMediaType()
    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory(contentType))
        .baseUrl(baseUrl)
        .build()

    private val service: NeoApiService by lazy {
        retrofit.create(NeoApiService::class.java)
    }

    override val asteroidRadarRepository: AsteroidsRadarRepository by lazy {
        AsteroidRadarRepositoryImpl(service)
    }
}