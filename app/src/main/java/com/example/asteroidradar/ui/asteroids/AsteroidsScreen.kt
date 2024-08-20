package com.example.asteroidradar.ui.asteroids

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AsteroidsScreen(
    modifier: Modifier = Modifier,
    viewModel: AsteroidsViewModel = viewModel(factory = AsteroidsViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Log.i("AsteroidsScreen", "asteroids: ${uiState.asteroids} \n pictureOfTheDay: ${uiState.pictureOfTheDay}")
}