package com.example.asteroidradar.ui.screens.asteroids

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.asteroidradar.R
import com.example.asteroidradar.data.local.asteroid.Asteroid
import com.example.asteroidradar.data.local.pictureOfTheDay.PictureOfTheDay
import com.example.asteroidradar.sampleAsteroids
import com.example.asteroidradar.samplePictureOfTheDay
import com.example.asteroidradar.ui.AsteroidTopAppBar
import com.example.asteroidradar.ui.navigation.NavigationDestination
import com.example.asteroidradar.ui.screens.asteroidDetails.AsteroidDetailScreen
import com.example.asteroidradar.utils.AsteroidsContentType

object AsteroidsScreenDestination: NavigationDestination {
    override val route = "asteroid_screen"
    override val titleRes = R.string.app_name
}

private const val TAG = "AsteroidsScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroidsScreen(
    modifier: Modifier = Modifier,
    windowSize: WindowWidthSizeClass,
    onAsteroidItemPressed: (String) -> Unit,
    viewModel: AsteroidsViewModel = viewModel(factory = AsteroidsViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    Log.i(TAG, "asteroids: ${uiState.asteroids} \npictureOfTheDay: ${uiState.pictureOfTheDay}")

    val contentType = when (windowSize) {
        WindowWidthSizeClass.Compact,
        WindowWidthSizeClass.Medium -> AsteroidsContentType.ListOnly

        WindowWidthSizeClass.Expanded -> AsteroidsContentType.ListAndDetail
        else -> AsteroidsContentType.ListOnly
    }

    Scaffold (
        modifier,
        topBar = {
            AsteroidTopAppBar(
                title = stringResource(id = AsteroidsScreenDestination.titleRes),
                canNavigateBack = false
            )
        }
    ) { innerPadding ->
        Column {
            if (contentType == AsteroidsContentType.ListAndDetail) {
                AsteroidsList(
                    pictureOfTheDay = uiState.pictureOfTheDay,
                    onItemClick = onAsteroidItemPressed,
                    modifier = modifier.fillMaxSize(),
                    contentPadding = innerPadding,
                    asteroids = uiState.asteroids
                )
            } else {
                AsteroidsListAndDetails(uiState = uiState, onAsteroidItemPressed = onAsteroidItemPressed) {

                }
            }
        }
    }
}

@Composable
fun AsteroidsListAndDetails(
    modifier: Modifier = Modifier,
    uiState: AsteroidsUiState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onAsteroidItemPressed: (String) -> Unit,
    onBackPressed: () -> Unit,
) {
    Row(
        modifier = modifier,
    ){
        AsteroidsList(
            pictureOfTheDay = uiState.pictureOfTheDay,
            asteroids = uiState.asteroids,
            onItemClick = onAsteroidItemPressed,
            contentPadding = PaddingValues(
                top = contentPadding.calculateTopPadding(),
            ),
            modifier = Modifier
                .weight(2f)
                .padding(horizontal = dimensionResource(R.dimen.padding_medium))
        )

        AsteroidDetailScreen(
            navigateBack = onBackPressed,
            contentPadding =
            PaddingValues(
                top = contentPadding.calculateTopPadding(),
            ),
            modifier = Modifier
                .weight(3f)
                .padding(end = 16.dp)
        )
    }
}

@Composable
private fun PictureOfTheDayCard(
    imgUrl: String?,
    title: String,
    modifier: Modifier = Modifier
) {
    Log.i(TAG, "PictureOfTheDayCard | imgUrl: $imgUrl")
    Box(
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium)
            .height(dimensionResource(id = R.dimen.pictureOfTheDay_card_height))
    ) {
        if (LocalInspectionMode.current) {
            Image(
                painter = painterResource(id = R.drawable.picture_of_the_day_sample),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize())
        } else {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(imgUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(id = R.string.picture_of_the_day),
                alignment = Alignment.Center,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth())
        }
        Text(
            text = title,
            color = Color.White.copy(alpha = 0.7f),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.Black.copy(alpha = 0.4f)) // Transparent black background for text
                .padding(
                    horizontal = dimensionResource(id = R.dimen.padding_large),
                    vertical = dimensionResource(id = R.dimen.padding_small)
                ),
            style = MaterialTheme.typography.bodyLarge // Customize your typography style
        )
    }
}

@Composable
private fun AsteroidsList(
    pictureOfTheDay: PictureOfTheDay?,
    asteroids: List<Asteroid>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        contentPadding = contentPadding,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
        modifier = modifier
    ) {
        item { PictureOfTheDayCard(imgUrl = pictureOfTheDay?.url, title = pictureOfTheDay?.title ?: "") }
        items(asteroids, key = { asteroid -> asteroid.id }) {
            asteroid ->
            AsteroidsItem(
                item = asteroid,
                modifier = Modifier
                    .padding(
                        vertical = dimensionResource(id = R.dimen.padding_small)
                    )
                    .clickable(
                        onClickLabel = stringResource(id = R.string.action_view_asteroid)
                    ) { onItemClick(asteroid.id) }
                    .clickable { onItemClick(asteroid.id) }
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
        modifier = modifier.padding(
            horizontal = dimensionResource(id = R.dimen.padding_medium)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AsteroidItemImage(
                isHazardous = item.isHazardous,
                modifier = Modifier.size(dimensionResource(id = R.dimen.image_height))
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
fun KeyValueText(
    key: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row (
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
    ){
        Text(
            text = "$key:",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.card_text_vertical_space))
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_small)))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.card_text_vertical_space))
        )
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
            contentScale = ContentScale.Fit
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PictureOfTheDayCardPreview() {
    PictureOfTheDayCard(imgUrl = samplePictureOfTheDay.url, title = samplePictureOfTheDay.title)
}

@Preview(showBackground = true)
@Composable
private fun AsteroidsListPreview() {
    AsteroidsList(pictureOfTheDay = samplePictureOfTheDay, asteroids = sampleAsteroids, onItemClick = {})
}

@Preview(showBackground = true)
@Composable
private fun AsteroidItemImagePreview() {
    AsteroidItemImage(isHazardous = false)
}

@Preview(showBackground = true)
@Composable
private fun AsteroidsListItemPreview() {
    AsteroidsItem(item = sampleAsteroids[0])
}

