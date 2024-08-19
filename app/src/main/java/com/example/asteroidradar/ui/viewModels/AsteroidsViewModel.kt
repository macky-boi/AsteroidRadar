package com.example.asteroidradar.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.asteroidradar.AsteroidRadarApplication
import com.example.asteroidradar.data.local.AsteroidEntity
import com.example.asteroidradar.data.repository.AsteroidDatabaseRepository
import com.example.asteroidradar.data.repository.AsteroidNetworkRepository
import kotlinx.coroutines.launch

sealed interface AsteroidsUiState {
    data class Success(val asteroidEntities: List<AsteroidEntity>) : AsteroidsUiState
    data object Error : AsteroidsUiState
    data object Loading : AsteroidsUiState
}

class AsteroidViewModel(
    private val networkRepository: AsteroidNetworkRepository,
    private val databaseRepository: AsteroidDatabaseRepository
) : ViewModel() {

    var asteroidUiState: AsteroidsUiState by mutableStateOf(AsteroidsUiState.Loading)
        private set

    init {

    }

    fun initializeData() {
        viewModelScope.launch {
            if (databaseRepository.isDatabaseEmpty()) {
                // If the database is empty, fetch from the network
                val networkAsteroids = networkRepository.fetchNearEarthObjects()
                val asteroidsEntity = networkAsteroids.toEntity()
                asteroidsEntity.forEach { (_, asteroid) ->
                    databaseRepository.insertAsteroids(asteroid)
                }
            }

        }
    }

    companion object {
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