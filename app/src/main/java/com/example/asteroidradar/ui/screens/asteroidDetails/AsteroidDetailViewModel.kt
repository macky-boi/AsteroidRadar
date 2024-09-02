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
import com.example.asteroidradar.data.repository.AsteroidRadarRepository
import com.example.asteroidradar.ui.AsteroidUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

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
): ViewModel() {

    private val _uiState = MutableStateFlow(AsteroidDetailsUiState())
    val uiState: StateFlow<AsteroidDetailsUiState> = _uiState.asStateFlow()

    fun updateAsteroid(asteroid: Asteroid) {
        _uiState.update {
            it.copy(asteroid = asteroid)
        }
    }
}