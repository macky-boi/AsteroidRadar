package com.example.asteroidradar.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.asteroidradar.AsteroidRadarApplication
import com.example.asteroidradar.data.local.AsteroidEntity
import com.example.asteroidradar.data.repository.AsteroidDatabaseRepository
import com.example.asteroidradar.data.repository.AsteroidNetworkRepository
import com.example.asteroidradar.data.repository.AsteroidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class AsteroidsUiState (
    val asteroids: List<AsteroidEntity> = listOf()
)

private const val TAG = "AsteroidsViewModel"

class AsteroidsViewModel(
    private val asteroidRepository: AsteroidRepository
) : ViewModel() {

    val uiState: StateFlow<AsteroidsUiState> =
        asteroidRepository.getAsteroids().map { AsteroidsUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AsteroidsUiState()
            )

    init {
        Log.i(TAG, "init")
        initializeData()
    }

    private fun initializeData() {
        Log.i(TAG, "initializeData")

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                asteroidRepository.fetchAndSaveAsteroidsIfDatabaseIsEmpty()
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
                val asteroidRepository = AsteroidRepository(databaseRepository, networkRepository)
                AsteroidsViewModel(asteroidRepository)
            }
        }
    }
}