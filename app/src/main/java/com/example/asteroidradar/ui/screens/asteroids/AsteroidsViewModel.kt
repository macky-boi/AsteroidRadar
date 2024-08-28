package com.example.asteroidradar.ui.screens.asteroids

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.asteroidradar.AsteroidRadarApplication
import com.example.asteroidradar.data.local.asteroid.Asteroid
import com.example.asteroidradar.data.local.pictureOfTheDay.PictureOfTheDay
import com.example.asteroidradar.data.repository.AsteroidRadarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class AsteroidsUiState (
    val asteroids: List<Asteroid> = listOf(),
    val pictureOfTheDay: PictureOfTheDay? = null
)

private const val TAG = "AsteroidsViewModel"

class AsteroidsViewModel(
    private val asteroidRadarRepository: AsteroidRadarRepository

) : ViewModel() {

    private val _uiState = MutableStateFlow(AsteroidsUiState())
    val uiState: StateFlow<AsteroidsUiState> = _uiState.asStateFlow()

    init {
        Log.i(TAG, "init")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                asteroidRadarRepository.initializePictureOfTheDay()
                asteroidRadarRepository.initializeAsteroids()
            }

            val asteroids = asteroidRadarRepository.getAllAsteroids()
            asteroids.collect {
                _uiState.value = _uiState.value.copy(asteroids = it)
            }

            val pictureOfTheDay = asteroidRadarRepository.getPictureOfTheDay()
            pictureOfTheDay.collect {
                _uiState.value = _uiState.value.copy(pictureOfTheDay = it)
            }
            Log.i(TAG, "uiState: $uiState")
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AsteroidRadarApplication)
                val asteroidRadarRepository = application.container.asteroidRadarRepository
                AsteroidsViewModel(asteroidRadarRepository)
            }
        }
    }
}