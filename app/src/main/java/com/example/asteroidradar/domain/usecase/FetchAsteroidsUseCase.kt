package com.example.asteroidradar.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.asteroidradar.data.remote.AsteroidsNetwork
import com.example.asteroidradar.data.repository.AsteroidNetworkRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class FetchAsteroidsUseCase(
    private val asteroidRepository: AsteroidNetworkRepository
) {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    suspend operator fun invoke(): Result<AsteroidsNetwork> {
        val today = LocalDate.now()
        val todayString = today.format(formatter)

        val endDate = today.plusDays(NUMBER_OF_DAYS.toLong())
        val endDateString = endDate.format(formatter)

        return asteroidRepository.fetchNearEarthObjects(todayString, endDateString)
    }

    companion object {
        private const val NUMBER_OF_DAYS = 7
    }
}