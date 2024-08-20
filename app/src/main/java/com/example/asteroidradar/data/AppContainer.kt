package com.example.asteroidradar.data

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.asteroidradar.data.local.AsteroidDao
import com.example.asteroidradar.data.local.AsteroidDatabase
import com.example.asteroidradar.data.remote.NeoApiService
import com.example.asteroidradar.data.repository.AsteroidDatabaseRepository
import com.example.asteroidradar.data.repository.AsteroidDatabaseRepositoryImpl
import com.example.asteroidradar.data.repository.AsteroidNetworkRepository
import com.example.asteroidradar.data.repository.AsteroidNetworkRepositoryImpl
import com.example.asteroidradar.data.repository.WorkManagerRepository
import com.example.asteroidradar.data.repository.WorkManagerRepositoryImpl
import com.example.asteroidradar.domain.usecase.FetchAsteroidsUseCase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.jetbrains.annotations.VisibleForTesting
import retrofit2.Retrofit

interface AppContainer {
    val asteroidDatabaseRepository: AsteroidDatabaseRepository
    val asteroidNetworkRepository: AsteroidNetworkRepository
    val workManagerRepository: WorkManagerRepository
    val fetchAsteroidsUseCase: FetchAsteroidsUseCase
}

@RequiresApi(Build.VERSION_CODES.O)
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

    private val asteroidDao = AsteroidDatabase.getDatabase(context).asteroidDao()

    override val asteroidDatabaseRepository: AsteroidDatabaseRepository by lazy {
        AsteroidDatabaseRepositoryImpl(asteroidDao)
    }

    override val asteroidNetworkRepository: AsteroidNetworkRepository by lazy {
        AsteroidNetworkRepositoryImpl(service)
    }

    override val workManagerRepository: WorkManagerRepository by lazy {
        WorkManagerRepositoryImpl(context)
    }

    override val fetchAsteroidsUseCase: FetchAsteroidsUseCase by lazy {
       FetchAsteroidsUseCase(asteroidNetworkRepository)
    }
}