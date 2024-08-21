package com.example.asteroidradar.domain.usecase

import com.example.asteroidradar.data.remote.AsteroidsNetwork
import com.example.asteroidradar.data.repository.AsteroidDatabaseRepository
import com.example.asteroidradar.data.repository.AsteroidNetworkRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FetchAsteroidsUseCase(
    private val networkRepository: AsteroidNetworkRepository,
    private val databaseRepository: AsteroidDatabaseRepository
) {

    suspend operator fun invoke(): Result<AsteroidsNetwork> {

        val today = Calendar.getInstance()
        val startDate = if (databaseRepository.isDatabaseEmpty()) {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(today.time)
        } else {
            val latestDate = databaseRepository.getLatestDate()!!
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(latestDate)
        }

        today.add(Calendar.DAY_OF_YEAR, NUMBER_OF_DAYS)
        val endDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(today.time)

        return networkRepository.fetchNearEarthObjects(startDate, endDate)
    }

    companion object {
        const val NUMBER_OF_DAYS = 7
    }
}