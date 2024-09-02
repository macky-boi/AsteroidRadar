package com.example.asteroidradar.ui.screens

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.asteroidradar.R
import com.example.asteroidradar.data.local.asteroid.Asteroid
import com.example.asteroidradar.data.local.pictureOfTheDay.PictureOfTheDay
import com.example.asteroidradar.sampleAsteroids
import com.example.asteroidradar.samplePictureOfTheDay

private const val TAG = "AsteroidsScreen"



@Composable
fun AsteroidsList(
    modifier: Modifier = Modifier,
    asteroids: List<Asteroid>,
    currentAsteroid: Asteroid,
    onAsteroidClicked: (Asteroid) -> Unit,
    pictureOfTheDay: PictureOfTheDay?,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
    ) {
        item {
            PictureOfTheDayCard(
             imgUrl = pictureOfTheDay?.url,
             title = pictureOfTheDay?.title ?: ""
            )
        }
        items(
            asteroids,
            key = { asteroid -> asteroid.id }
        ) { asteroid ->
            AsteroidsItem(
                asteroid = asteroid,
                isCurrentAsteroid = currentAsteroid == asteroid,
                modifier = Modifier
                    .padding(
                        vertical = dimensionResource(id = R.dimen.padding_small)
                    ),
                onClick = onAsteroidClicked
            )
        }
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AsteroidsItem(
    modifier: Modifier = Modifier,
    asteroid: Asteroid,
    isCurrentAsteroid: Boolean = false,
    onClick: (Asteroid) -> Unit = {}
) {
    Card(
        elevation = CardDefaults.cardElevation(),
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
    ) {
        Row(
            modifier = Modifier
                .clickable(
                    onClickLabel = stringResource(id = R.string.action_view_asteroid)
                ) { onClick(asteroid) }
                .background(
                    color = if (isCurrentAsteroid)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        Color.Unspecified
                )
                .semantics(mergeDescendants = true) {
                    contentDescription = "Asteroid."
                    selected = isCurrentAsteroid
                }
                .fillMaxWidth()
        ) {
            AsteroidItemImage(
                isHazardous = asteroid.isHazardous,
                modifier = Modifier.size(dimensionResource(id = R.dimen.image_height))
            )
            Column(
                modifier = Modifier
                    .padding(
                        vertical = dimensionResource(id = R.dimen.padding_medium),
                        horizontal = dimensionResource(id = R.dimen.padding_medium)
                    )
            ) {
                KeyValueText(key = "name", value = asteroid.name)
                KeyValueText(key = "distance", value = asteroid.missDistanceAstronomical)
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun KeyValueText(
    key: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row (
        verticalAlignment = Alignment.Bottom,
        modifier = modifier.semantics { contentDescription = "" }
    ){
        Text(
            text = "$key:",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.card_text_vertical_space))
                .semantics {
                }
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
//    AsteroidsList(onAsteroidClicked = {})
}

@Preview(showBackground = true)
@Composable
private fun AsteroidItemImagePreview() {
    AsteroidItemImage(isHazardous = false)
}

@Preview(showBackground = true)
@Composable
private fun AsteroidsListItemPreview() {
    AsteroidsItem(asteroid = sampleAsteroids[0])
}

