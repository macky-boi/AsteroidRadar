package com.example.asteroidradar.ui.screens.asteroids

import android.util.Log
import androidx.lifecycle.SavedStateHandle
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
import com.example.asteroidradar.ui.screens.asteroidDetails.AsteroidDetailsDestination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class AsteroidsUiState (
    val asteroids: List<Asteroid> = listOf(),
    val pictureOfTheDay: PictureOfTheDay? = null
)

private const val TAG = "AsteroidsViewModel"

class AsteroidsViewModel(
    private val asteroidRadarRepository: AsteroidRadarRepository,
    private val savedStateHandle: SavedStateHandle
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(AsteroidsUiState())
    val uiState: StateFlow<AsteroidsUiState> = _uiState.asStateFlow()

    fun setSelectedAsteroidId(id: String) {
        savedStateHandle[AsteroidDetailsDestination.ASTEROID_ID_KEY] = id
    }

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
                    _uiState.value = _uiState.value.copy(asteroids = it)
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

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AsteroidRadarApplication)
                val asteroidRadarRepository = application.container.asteroidRadarRepository
                AsteroidsViewModel(asteroidRadarRepository, this.createSavedStateHandle())
            }
        }
    }
}