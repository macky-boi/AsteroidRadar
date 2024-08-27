package com.example.asteroidradar.ui.screens.asteroids

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.asteroidradar.AsteroidRadarApplication
import com.example.asteroidradar.data.local.Asteroid
import com.example.asteroidradar.data.repository.AsteroidDatabaseRepository
import com.example.asteroidradar.data.repository.AsteroidNetworkRepository
import com.example.asteroidradar.domain.FetchAsteroidsUseCase
import com.example.asteroidradar.ui.model.AstronomyPictureOfTheDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class AsteroidsUiState (
    val asteroids: List<Asteroid> = listOf(),
    val pictureOfTheDay: AstronomyPictureOfTheDay? = null
)

private const val TAG = "AsteroidsViewModel"

class AsteroidsViewModel(
    private val databaseRepository: AsteroidDatabaseRepository,
    private val networkRepository: AsteroidNetworkRepository,
    private val fetchAsteroidsUseCase: FetchAsteroidsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AsteroidsUiState())
    val uiState: StateFlow<AsteroidsUiState> = _uiState.asStateFlow()

    init {
        Log.i(TAG, "init")
        initializeData()
    }

    private fun initializeData() {
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
                AsteroidsViewModel(databaseRepository, networkRepository, fetchAsteroidsUseCase)
            }
        }
    }
}