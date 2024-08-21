package com.example.asteroidradar.domain.usecase

import com.example.asteroidradar.data.remote.AsteroidsNetwork
import com.example.asteroidradar.data.repository.AsteroidNetworkRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FetchAsteroidsUseCase(
    private val networkRepository: AsteroidNetworkRepository
) {

    suspend operator fun invoke(): Result<AsteroidsNetwork> {
        val calendar = Calendar.getInstance()
        val todayString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        calendar.add(Calendar.DAY_OF_YEAR, NUMBER_OF_DAYS)
        val endDateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        return networkRepository.fetchNearEarthObjects(todayString, endDateString)
    }

    companion object {
        const val NUMBER_OF_DAYS = 7
    }
}

// if database empty
//      fetchAsteroid(startDate: today | endDate: 7 days from now)
// else
//      delete past asteroids
//      latestDate = get the the latest date from database
//      fetchAsteroid(startDate: latestDate + 1 | endDate: 7 days from now)