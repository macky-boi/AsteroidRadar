package com.example.asteroidradar.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class NetworkModule {
    companion object {
        private const val BASE_URL = "https://api.nasa.gov/"
        private val CONTENT_TYPE = "application/json".toMediaType()
    }

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory(CONTENT_TYPE))
            .baseUrl(BASE_URL)
            .build()
    }

    val service: NeoApiService by lazy {
        retrofit.create(NeoApiService::class.java)
    }
}