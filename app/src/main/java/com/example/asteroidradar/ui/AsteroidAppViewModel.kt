package com.example.asteroidradar.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.asteroidradar.AsteroidRadarApplication
import com.example.asteroidradar.data.local.asteroid.Asteroid
import com.example.asteroidradar.data.local.pictureOfTheDay.PictureOfTheDay
import com.example.asteroidradar.data.repository.AsteroidRadarRepository
import com.example.asteroidradar.ui.screens.asteroidList.AsteroidListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val emptyAsteroid = Asteroid(
    id = "",
    name = "",
    date = "",
    isHazardous = false,
    absoluteMagnitude = 0.0,
    closeApproachDate = "",
    missDistanceAstronomical = "",
    relativeVelocityKilometersPerSecond = ""
)

data class AsteroidUiState (
    val currentAsteroid: Asteroid = emptyAsteroid,
    val asteroids: List<Asteroid> = listOf(),
    val isShowingListPage: Boolean = true,
    val pictureOfTheDay: PictureOfTheDay? = null
)

private const val TAG = "AsteroidsViewModel"

class AsteroidAppViewModel(
    private val asteroidRadarRepository: AsteroidRadarRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AsteroidUiState())
    val uiState: StateFlow<AsteroidUiState> = _uiState.asStateFlow()

    init {
        Log.i(TAG, "init")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                asteroidRadarRepository.initializePictureOfTheDay()
                asteroidRadarRepository.initializeAsteroids()
            }

            launch {
                val asteroids = asteroidRadarRepository.getAllAsteroids()
                Log.i(TAG, "asteroids: ${asteroids.first()}")
                asteroids.collect {
                    Log.i(TAG, "collecting asteroids: $it")
                    _uiState.value = _uiState.value.copy(asteroids = it, currentAsteroid = it.first())
                }
            }

            launch {
                val pictureOfTheDay = asteroidRadarRepository.getPictureOfTheDay()
                Log.i(TAG, "pictureOfTheDay: ${pictureOfTheDay.first()}")
                pictureOfTheDay.collect {
                    Log.i(TAG, "collecting pictureOfTheDay: $it")
                    _uiState.value = _uiState.value.copy(pictureOfTheDay = it)
                }
            }
        }
    }

    fun updateCurrentAsteroid(selectedSport: Asteroid) {
        _uiState.update {
            it.copy(currentAsteroid = selectedSport)
        }
    }

    fun navigateToListPage() {
        _uiState.update {
            it.copy(isShowingListPage = true)
        }
    }

    fun navigateToDetailPage() {
        _uiState.update {
            it.copy(isShowingListPage = false)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AsteroidRadarApplication)
                val asteroidRadarRepository = application.container.asteroidRadarRepository
                AsteroidAppViewModel(asteroidRadarRepository)
            }
        }
    }
}