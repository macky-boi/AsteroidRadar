package com.example.asteroidradar.ui.screens

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.asteroidradar.R
import com.example.asteroidradar.data.local.asteroid.Asteroid
import com.example.asteroidradar.sampleAsteroids



@Composable
fun AsteroidDetail(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
    asteroid: Asteroid,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {

    BackHandler {
        navigateBack()
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsteroidDetailImage(
            isHazardous = asteroid.isHazardous,
            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium))
        )
        AsteroidDetailCardDescription(asteroid = asteroid,
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium))
        )
    }
}

@Composable
private fun AsteroidDetailCardDescription(
    modifier: Modifier = Modifier,
    asteroid: Asteroid
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
        ) {
            AsteroidDetailsRow(
                labelResId = R.string.name_label,
                detail = asteroid.name
            )
            AsteroidDetailsRow(
                labelResId = R.string.is_hazardous_label,
                detail = asteroid.isHazardous.toString()
            )
            AsteroidDetailsRow(
                labelResId = R.string.close_approachDate_label,
                detail = asteroid.closeApproachDate
            )
            AsteroidDetailsRow(
                labelResId = R.string.miss_distance_astronomical_label,
                detail = asteroid.missDistanceAstronomical
            )
            AsteroidDetailsRow(
                labelResId = R.string.relative_velocity_label,
                detail = asteroid.relativeVelocityKilometersPerSecond,
            )
            AsteroidDetailsRow(
                labelResId = R.string.absolute_magnitude_label,
                detail = asteroid.absoluteMagnitude.toString()
            )
        }
    }
}

@Composable
private fun AsteroidDetailImage(isHazardous: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(dimensionResource(id = R.dimen.image_size_large))
            .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
            .clip(CircleShape)
    ) {
        Image(
            painter = painterResource(
                if (isHazardous)
                    R.mipmap.hazardous_comet_img_foreground else  R.mipmap.safe_comet_img_foreground
            ),
            modifier = Modifier.size(dimensionResource(id = R.dimen.image_size_large)),
            contentDescription = null,
            alignment = Alignment.Center,
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
private fun AsteroidDetailsRow(
    @StringRes labelResId: Int,
    detail: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(
                horizontal = dimensionResource(id = R.dimen.padding_small)
            )
    ) {
        Text(text = stringResource(id = labelResId), style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = detail, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
}

@Preview
@Composable
fun AsteroidDetailBodyPreview() {
    AsteroidDetail(
        asteroid = sampleAsteroids[0]
    )
}