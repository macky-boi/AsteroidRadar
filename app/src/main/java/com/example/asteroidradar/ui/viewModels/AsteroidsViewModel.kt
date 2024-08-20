package com.example.asteroidradar.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.asteroidradar.AsteroidRadarApplication
import com.example.asteroidradar.data.local.AsteroidEntity
import com.example.asteroidradar.data.repository.AsteroidDatabaseRepository
import com.example.asteroidradar.data.repository.AsteroidNetworkRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AsteroidsUiState (
    val asteroids: List<AsteroidEntity> = listOf()
)

class AsteroidViewModel(
    private val networkRepository: AsteroidNetworkRepository,
    private val databaseRepository: AsteroidDatabaseRepository
) : ViewModel() {

    val uiState: StateFlow<AsteroidsUiState> =
        databaseRepository.getAllAsteroids().map { AsteroidsUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AsteroidsUiState()
            )

    init {
        initializeData()
    }

    private fun initializeData() {
        viewModelScope.launch {
            if (databaseRepository.isDatabaseEmpty()) {
                val networkAsteroids = networkRepository.fetchNearEarthObjects()
                val asteroidsEntity = networkAsteroids.toEntity()
                asteroidsEntity.forEach { (_, asteroid) ->
                    databaseRepository.insertAsteroids(asteroid)
                }
            }

        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AsteroidRadarApplication)
                val networkRepository = application.container.asteroidNetworkRepository
                val databaseRepository = application.container.asteroidDatabaseRepository
                AsteroidViewModel(networkRepository, databaseRepository)
            }
        }
    }
}