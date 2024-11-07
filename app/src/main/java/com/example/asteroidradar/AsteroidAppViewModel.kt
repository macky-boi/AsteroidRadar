package com.example.asteroidradar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.asteroidradar.data.local.asteroid.AsteroidEntity
import com.example.asteroidradar.data.local.pictureOfTheDay.PictureOfTheDay
import com.example.asteroidradar.data.repository.AsteroidRadarRepository
import kotlinx.coroutines.launch

val emptyAsteroidEntity = AsteroidEntity(
    id = "",
    name = "",
    date = "",
    isHazardous = false,
    absoluteMagnitude = 0.0,
    closeApproachDate = "",
    missDistanceAstronomical = "",
    relativeVelocityKilometersPerSecond = ""
)

private const val TAG = "AsteroidsViewModel"

class AsteroidAppViewModel(
    private val asteroidRadarRepository: AsteroidRadarRepository
) : ViewModel() {

    private val _asteroids = asteroidRadarRepository.getAllAsteroids()
    val asteroids: LiveData<List<AsteroidEntity>> = _asteroids

    private var _pictureOfTheDay = MutableLiveData<PictureOfTheDay?>(null)
    val pictureOfTheDay = _pictureOfTheDay

    private val _currentAsteroidEntity = MutableLiveData<AsteroidEntity?>(null)
    val currentAsteroidEntity: LiveData<AsteroidEntity?> = _currentAsteroidEntity

    private val _isShowingListPage = MutableLiveData<Boolean>(true)
    val isShowingListPage: LiveData<Boolean> = _isShowingListPage

    private val _navigateToDetail = MutableLiveData<Boolean>(true)
    val navigateToDetail: LiveData<Boolean> = _navigateToDetail

    private val _navigateToList = MutableLiveData<Boolean>(true)
    val navigateToList: LiveData<Boolean> = _navigateToList

    init {
        Log.i(TAG, "init")
        viewModelScope.launch {
            _pictureOfTheDay.value = asteroidRadarRepository.getPictureOfTheDay()
            Log.i(TAG, "_pictureOfTheDay: ${_pictureOfTheDay.value}")
            asteroidRadarRepository.initializeAsteroids()
        }
    }

    fun updateCurrentAsteroid(asteroidEntity: AsteroidEntity) {
        viewModelScope.launch {
            _currentAsteroidEntity.value = asteroidEntity
        }
    }

    fun navigateToListPage() {
        _navigateToList.value = true
    }

    fun navigatedToListPage() {
        _navigateToList.value = false
    }

    fun navigateToDetailPage() {
        _navigateToDetail.value = true
    }

    fun navigatedToDetailPage() {
        _navigateToDetail.value = false
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