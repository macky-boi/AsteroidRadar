package com.example.asteroidradar.ui.asteroids

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.asteroidradar.R
import com.example.asteroidradar.data.local.Asteroid
import com.example.asteroidradar.ui.navigation.NavigationDestination

object AsteroidScreenDestination: NavigationDestination {
    override val route = "asteroid_screen"
    override val titleRes = R.string.app_name
}

@Composable
fun AsteroidsScreen(
    modifier: Modifier = Modifier,
    viewModel: AsteroidsViewModel = viewModel(factory = AsteroidsViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    Log.i("AsteroidsScreen", "asteroids: ${uiState.asteroids} \n pictureOfTheDay: ${uiState.pictureOfTheDay}")

    Scaffold (
        modifier = modifier
    ) { innerPadding ->
        AsteroidsList(
            modifier = modifier,
            contentPadding = innerPadding,
            asteroids = uiState.asteroids,
            onClick = {}
        )
    }

}

@Composable
private fun AsteroidsList(
    asteroids: List<Asteroid>,
    onClick: (Asteroid) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AsteroidsListItem(
    asteroid: Asteroid,
    onItemClick: (Asteroid) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(),
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.card_corner_radius)),
        onClick = { onItemClick(asteroid) }
    ) {
        Column {

        }
    }
}

@Preview
@Composable
private fun AsteroidsListItemPreview() {
    AsteroidsListItem(asteroid = , onItemClick = )
}