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
import com.example.asteroidradar.data.repository.AsteroidDatabaseRepository
import com.example.asteroidradar.data.repository.AsteroidNetworkRepository
import com.example.asteroidradar.data.repository.PictureOfTheDayRepository
import com.example.asteroidradar.domain.FetchAsteroidsUseCase
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
    private val databaseRepository: AsteroidDatabaseRepository,
    private val networkRepository: AsteroidNetworkRepository,
    private val fetchAsteroidsUseCase: FetchAsteroidsUseCase,
    private val pictureOfTheDayRepository: PictureOfTheDayRepository

) : ViewModel() {

    private val _uiState = MutableStateFlow(AsteroidsUiState())
    val uiState: StateFlow<AsteroidsUiState> = _uiState.asStateFlow()

    init {
        Log.i(TAG, "init")
        initializeAsteroids()
//        initializePictureOfTheDay()
    }

    private fun initializeAsteroids() {
        Log.i(TAG, "initializeData")

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                databaseRepository.deleteAllAsteroidsFromThePast()
                fetchAndSaveAsteroidsIfDatabaseIsEmpty()

                databaseRepository.getAllAsteroids().collect { asteroids ->
                    _uiState.value = AsteroidsUiState(
                        asteroids = asteroids)
                }
            }
        }
    }

//    private fun initializePictureOfTheDay() {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                var pictureOfTheDay = pictureOfTheDayRepository.getPictureOfTheDay()
//
//                if (pictureOfTheDay.first.isNullOrEmpty()) {
//                    val fetchPictureOfTheDayResponse = networkRepository.fetchPictureOfTheDay()
//
//                    fetchPictureOfTheDayResponse.onSuccess {
//                        pictureOfTheDayRepository.savePictureOfTheDay(it.url, it.date)
//                    }.onFailure { e ->
//                        Log.e(TAG, e.localizedMessage ?: "Failed to fetch pictureOfTheDay")
//                    }
//
//                    pictureOfTheDay =  pictureOfTheDayRepository.getPictureOfTheDay()
//                }
//
//                val url = pictureOfTheDay.first
//                val date = pictureOfTheDay.second
//
//                _uiState.value = _uiState.value.copy(pictureOfTheDay = AstronomyPictureOfTheDay(date, url))
//            }
//        }
//    }

    private suspend fun fetchAndSaveAsteroidsIfDatabaseIsEmpty() {
        Log.i(TAG, "fetchAndSaveAsteroidsIfDatabaseIsEmpty")

        if (databaseRepository.isDatabaseEmpty()) {
            Log.i(TAG, "database is empty")
            val networkAsteroidsResponse = fetchAsteroidsUseCase()
            networkAsteroidsResponse.onSuccess { asteroidsNetwork ->
                Log.i(TAG, "asteroidsNetwork: $asteroidsNetwork")
                asteroidsNetwork.toEntity().forEach { (_, asteroid) ->
                    Log.i(TAG, "inserting asteroids: ${asteroidsNetwork.asteroids}")
                    databaseRepository.insertAsteroids(asteroid)
                }
            }.onFailure { exception ->
                Log.e(TAG, exception.localizedMessage ?: "Failed to fetch asteroids")
            }
        } else {
            Log.i(TAG, "database IS NOT empty")
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AsteroidRadarApplication)
                val networkRepository = application.container.asteroidNetworkRepository
                val databaseRepository = application.container.asteroidDatabaseRepository
                val fetchAsteroidsUseCase = application.container.fetchAsteroidsUseCase
                val pictureOfTheDayRepository = application.container.pictureOfTheDayRepository
                AsteroidsViewModel(databaseRepository, networkRepository, fetchAsteroidsUseCase, pictureOfTheDayRepository)
            }
        }
    }
}