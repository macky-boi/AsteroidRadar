package com.example.asteroidradar.ui.screens.asteroidDetails

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.asteroidradar.R
import com.example.asteroidradar.ui.AsteroidTopAppBar
import com.example.asteroidradar.ui.navigation.NavigationDestination
import com.example.asteroidradar.ui.sampleAsteroids


object AsteroidDetailsDestination: NavigationDestination {
    override val route = "asteroid_details"
    override val titleRes: Int = R.string.asteroid_detail_title
    const val ASTEROID_ID_ARG = "asteroidId"
    val routeWithArgs = "$route/{$ASTEROID_ID_ARG}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroidDetailScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    viewModel: AsteroidDetailsViewModel = viewModel(factory = AsteroidDetailsViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    Log.i("AsteroidDetailScreen", "uiState: $uiState")

    Scaffold(
        topBar = {
            AsteroidTopAppBar(
                title = stringResource(id = AsteroidDetailsDestination.titleRes),
                canNavigateBack = true
            )
        }
    ) { innerPadding ->
        AsteroidDetailsBody(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState
        )
    }
}

@Composable
private fun AsteroidDetailImage(isHazardous: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(dimensionResource(id = R.dimen.image_height))
            .background(MaterialTheme.colorScheme.secondaryContainer, shape = CircleShape)
            .clip(CircleShape)
    ) {
        Image(
            painter = painterResource(
                if (isHazardous)
                    R.mipmap.hazardous_comet_img_foreground else  R.mipmap.safe_comet_img_foreground
            ),
            contentDescription = null,
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop
        )
    }
}


@Composable
fun AsteroidDetailsBody(
    uiState: AsteroidDetailsUiState,
    modifier: Modifier = Modifier,
) {
    val asteroid = uiState.asteroid

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_medium)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsteroidDetailImage(
            isHazardous = asteroid.isHazardous,
            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium))
        )
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
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
        Text(text = stringResource(id = labelResId))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = detail, fontWeight = FontWeight.Bold)
    }
}

@Preview
@Composable
fun AsteroidDetailBodyPreview() {
    AsteroidDetailsBody(
        uiState = AsteroidDetailsUiState(sampleAsteroids[0])
    )
}