package com.example.asteroidradar.ui.screens.asteroidDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.asteroidradar.data.local.Asteroid
import com.example.asteroidradar.data.repository.AsteroidDatabaseRepository
import com.example.asteroidradar.ui.sampleAsteroids


@Composable
fun AsteroidDetailScreen(
    modifier: Modifier = Modifier,
    asteroid: Asteroid,
    onBackPressed: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Scaffold(

    ) {

    }
}

@Preview
@Composable
fun AsteroidDetailScreenPreview() {
    AsteroidDetailScreen(
        asteroid = sampleAsteroids[0],
        onBackPressed = {}
    )
}