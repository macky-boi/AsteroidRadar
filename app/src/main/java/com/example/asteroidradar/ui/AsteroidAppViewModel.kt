package com.example.asteroidradar.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.asteroidradar.AsteroidRadarApplication
import com.example.asteroidradar.data.local.asteroid.Asteroid
import com.example.asteroidradar.data.local.pictureOfTheDay.PictureOfTheDay
import com.example.asteroidradar.data.repository.AsteroidRadarRepository
import com.example.asteroidradar.utils.AsteroidsContentType
import kotlinx.coroutines.Dispatchers
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
    val asteroids: List<Asteroid> = listOf(),
    val currentAsteroid: Asteroid = emptyAsteroid,
    val isShowingListPage: Boolean = true,
    val pictureOfTheDay: PictureOfTheDay? = null,
    val contentType: AsteroidsContentType = AsteroidsContentType.ListOnly
) {
    val isShowingDetailPage: Boolean
        get() = !isShowingListPage
}

private const val TAG = "AsteroidsViewModel"

class AsteroidAppViewModel(
    private val asteroidRadarRepository: AsteroidRadarRepository
) : ViewModel() {

    private val _asteroids = asteroidRadarRepository.getAllAsteroids()
    val asteroids: LiveData<List<Asteroid>> = _asteroids

    private val _pictureOfTheDay = asteroidRadarRepository.getPictureOfTheDay()
    val pictureOfTheDay: LiveData<PictureOfTheDay?> = _pictureOfTheDay

    private val _currentAsteroid = MutableLiveData<Asteroid?>(null)
    val currentAsteroid: LiveData<Asteroid?> = _currentAsteroid

    private val _isShowingListPage = MutableLiveData<Boolean>(true)
    val isShowingListPage: LiveData<Boolean> = _isShowingListPage

    init {
        Log.i(TAG, "init")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                asteroidRadarRepository.initializePictureOfTheDay()
                asteroidRadarRepository.initializeAsteroids()
            }
        }
    }

    fun updateCurrentAsteroid(selectedAsteroid: Asteroid) {
        _currentAsteroid.value = selectedAsteroid
    }

    fun navigateToListPage() {
        _isShowingListPage.value = true
    }

    fun navigateToDetailPage() {
        _isShowingListPage.value = false
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