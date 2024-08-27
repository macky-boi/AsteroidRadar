package com.example.asteroidradar.ui.screens.asteroids

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.asteroidradar.R
import com.example.asteroidradar.data.local.Asteroid
import com.example.asteroidradar.ui.AsteroidTopAppBar
import com.example.asteroidradar.ui.KeyValueText
import com.example.asteroidradar.ui.navigation.NavigationDestination
import com.example.asteroidradar.ui.sampleAsteroids

object AsteroidsScreenDestination: NavigationDestination {
    override val route = "asteroid_screen"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroidsScreen(
    modifier: Modifier = Modifier,
    navigateToAsteroidDetails: (String) -> Unit,
    viewModel: AsteroidsViewModel = viewModel(factory = AsteroidsViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AsteroidTopAppBar(
                title = stringResource(id = AsteroidsScreenDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        AsteroidsList(
            onItemClick = { navigateToAsteroidDetails(it.id)},
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
            asteroids = uiState.asteroids
        )
    }
}

@Composable
private fun AsteroidsList(
    asteroids: List<Asteroid>,
    onItemClick: (Asteroid) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        contentPadding = contentPadding,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        modifier = modifier
    ) {
        items(asteroids, key = { asteroid -> asteroid.id }) { asteroid ->
            AsteroidsItem(
                item = asteroid,
                modifier = Modifier
                    .clickable { onItemClick(asteroid) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AsteroidsItem(
    item: Asteroid,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(),
        modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.card_corner_radius))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .size(dimensionResource(id = R.dimen.image_height))
        ) {
            AsteroidItemImage(
                isHazardous = item.isHazardous,
                modifier = modifier.size(dimensionResource(id = R.dimen.image_height))
            )
            Column(
                modifier = Modifier
                    .padding(
                        vertical = dimensionResource(id = R.dimen.padding_medium),
                        horizontal = dimensionResource(id = R.dimen.padding_medium)
                    )
            ) {
                KeyValueText(key = "name", value = item.name)
                KeyValueText(key = "distance", value = item.missDistanceAstronomical)
            }
        }
    }
}

@Composable
private fun AsteroidItemImage(isHazardous: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(
                if (isHazardous)
                    R.mipmap.hazardous_comet_img_foreground else  R.mipmap.safe_comet_img_foreground
            ),
            contentDescription = null,
            alignment = Alignment.Center,
            contentScale = ContentScale.FillWidth
        )
    }
}

@Preview
@Composable
private fun AsteroidsListPreview() {
    AsteroidsList(asteroids = sampleAsteroids, onItemClick = {})
}

@Preview
@Composable
private fun AsteroidItemImagePreview() {
    AsteroidItemImage(isHazardous = false)
}

@Preview
@Composable
private fun AsteroidsListItemPreview() {
    AsteroidsItem(item = sampleAsteroids[0])
}

