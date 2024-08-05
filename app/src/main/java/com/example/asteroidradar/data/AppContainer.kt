package com.example.asteroidradar.data

import android.content.Context
import com.example.asteroidradar.data.local.AsteroidDatabase
import com.example.asteroidradar.data.repository.AsteroidRadarRepositoryImpl
import com.example.asteroidradar.data.repository.AsteroidsRadarRepository
import com.example.asteroidradar.data.remote.NeoApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.jetbrains.annotations.VisibleForTesting
import retrofit2.Retrofit

interface AppContainer {
    val asteroidRadarRepository: AsteroidsRadarRepository
}

class DefaultAppContainer(context: Context): AppContainer {

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

    private val asteroidDatabase = AsteroidDatabase.getDatabase(context).asteroidDao()

    override val asteroidRadarRepository: AsteroidsRadarRepository by lazy {
        AsteroidRadarRepositoryImpl(service, asteroidDatabase)
    }
}