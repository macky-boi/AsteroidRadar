package com.example.asteroidradar.ui.screens.asteroidDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.asteroidradar.AsteroidRadarApplication
import com.example.asteroidradar.data.local.asteroid.Asteroid
import com.example.asteroidradar.data.repository.AsteroidDatabaseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

private const val TAG = "AsteroidDetailViewModel"

data class AsteroidDetailsUiState(
    val asteroid: Asteroid = Asteroid(
        id = "",
        name = "",
        date = "",
        isHazardous = false,
        absoluteMagnitude = 0.0,
        closeApproachDate = "",
        missDistanceAstronomical = "",
        relativeVelocityKilometersPerSecond = ""
    )
)

class AsteroidDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val databaseRepository: AsteroidDatabaseRepository
): ViewModel() {

    private val asteroidId: String = checkNotNull(savedStateHandle[AsteroidDetailsDestination.ASTEROID_ID_ARG])

    val uiState: StateFlow<AsteroidDetailsUiState> =
        databaseRepository.getAsteroid(asteroidId)
            .filterNotNull()
            .map {
                AsteroidDetailsUiState(asteroid = it)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AsteroidDetailsUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AsteroidRadarApplication)
                val databaseRepository = application.container.asteroidDatabaseRepository
                AsteroidDetailsViewModel(
                    savedStateHandle = this.createSavedStateHandle(),
                    databaseRepository = databaseRepository
                )
            }
        }
    }
}