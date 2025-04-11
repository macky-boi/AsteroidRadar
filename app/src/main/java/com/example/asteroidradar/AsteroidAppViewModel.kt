package com.example.asteroidradar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.asteroidradar.data.local.asteroid.AsteroidEntity
import com.example.asteroidradar.data.remote.PictureOfTheDay
import com.example.asteroidradar.data.repository.AsteroidRadarRepository
import com.example.asteroidradar.model.Asteroid
import com.example.asteroidradar.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class AsteroidAppViewModel(
    private val asteroidRadarRepository: AsteroidRadarRepository
) : ViewModel() {

    val asteroids: LiveData<List<AsteroidEntity>> = asteroidRadarRepository.getAllAsteroids()

    private var _pictureOfTheDay = MutableLiveData<PictureOfTheDay?>(null)
    val pictureOfTheDay = _pictureOfTheDay

    private val _currentAsteroidEntity = MutableLiveData(EMPTY_ASTEROID)
    val currentAsteroidEntity: LiveData<Asteroid> = _currentAsteroidEntity

    private val _navigateToDetail = SingleLiveEvent<Unit>()
    val navigateToDetail: LiveData<Unit> = _navigateToDetail

    private val _navigateToList = SingleLiveEvent<Unit>()
    val navigateToList: LiveData<Unit> = _navigateToList


    init {
        refreshData()
    }

    private fun refreshData() {
        viewModelScope.launch {
            _pictureOfTheDay.value = asteroidRadarRepository.getPictureOfTheDay()
            asteroidRadarRepository.initializeAsteroids()
        }
    }

    fun updateCurrentAsteroid(asteroidEntity: Asteroid) {
        _currentAsteroidEntity.value = asteroidEntity
    }

    fun navigateToListPage() {
        _navigateToList.call()
    }

    fun navigateToDetailPage() {
        _navigateToDetail.call()
    }

    companion object {
        private val EMPTY_ASTEROID = Asteroid(
            id = "",
            name = "",
            date = "",
            isHazardous = "",
            absoluteMagnitude = "",
            closeApproachDate = "",
            missDistanceAstronomical = "",
            relativeVelocityKilometersPerSecond = ""
        )

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AsteroidRadarApplication)
                val asteroidRadarRepository = application.container.asteroidRadarRepository
                AsteroidAppViewModel(asteroidRadarRepository)
            }
        }
    }
}