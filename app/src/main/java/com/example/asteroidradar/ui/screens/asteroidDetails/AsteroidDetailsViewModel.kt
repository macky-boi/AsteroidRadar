package com.example.asteroidradar.ui.screens.asteroidDetails

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.asteroidradar.AsteroidRadarApplication
import com.example.asteroidradar.data.local.asteroid.Asteroid
import com.example.asteroidradar.data.repository.AsteroidRadarRepository
import com.example.asteroidradar.ui.screens.asteroids.AsteroidsUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    currentAsteroidId: String? = null,
    savedStateHandle: SavedStateHandle,
    asteroidRadarRepository: AsteroidRadarRepository
): ViewModel() {

//    private val asteroidId: String? = savedStateHandle[AsteroidDetailsDestination.ASTEROID_ID_KEY
    private val asteroidId: StateFlow<String?> = savedStateHandle.getStateFlow("selected_asteroid_id", null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<AsteroidDetailsUiState> =
        asteroidId
            .filterNotNull()// make sure the id is not null
            .flatMapLatest { id ->
                asteroidRadarRepository.getAsteroid(id).filterNotNull()
            }
            .map { asteroid ->
                AsteroidDetailsUiState(asteroid = asteroid)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AsteroidDetailsUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AsteroidRadarApplication)
                val asteroidRadarRepository = application.container.asteroidRadarRepository
                AsteroidDetailsViewModel(
                    savedStateHandle = this.createSavedStateHandle(),
                    asteroidRadarRepository = asteroidRadarRepository
                )
            }
        }
    }
}