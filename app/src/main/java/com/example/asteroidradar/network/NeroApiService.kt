package com.example.asteroidradar.network

interface NeoApiService {
    suspend fun getNearEarthObjects(): String
}