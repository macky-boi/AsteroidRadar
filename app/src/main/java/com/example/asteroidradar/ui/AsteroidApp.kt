package com.example.asteroidradar.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.asteroidradar.R
import com.example.asteroidradar.ui.screens.AsteroidDetail
import com.example.asteroidradar.ui.screens.AsteroidsList
import com.example.asteroidradar.ui.screens.AsteroidsListAndDetails
import com.example.asteroidradar.utils.AsteroidsContentType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroidApp(
    viewModel: AsteroidAppViewModel = viewModel(factory = AsteroidAppViewModel.Factory),
    windowSize: WindowWidthSizeClass,
) {
    val uiState by viewModel.uiState.collectAsState()

    val contentType = when (windowSize) {
        WindowWidthSizeClass.Compact,
        WindowWidthSizeClass.Medium -> AsteroidsContentType.ListOnly

        WindowWidthSizeClass.Expanded -> AsteroidsContentType.ListAndDetail
        else -> AsteroidsContentType.ListOnly
    }

    viewModel.updateContentType(contentType)

    Scaffold (
        topBar = {
            if (contentType != AsteroidsContentType.ListAndDetail)
                AsteroidTopAppBar(
                    canNavigateBack = uiState.isShowingDetailPage,
                    title = if (uiState.isShowingListPage)
                        stringResource(id = R.string.app_name)
                    else 
                        stringResource(id = R.string.asteroid_detail_title),
                    contentType = contentType,
                    navigateUp = { viewModel.navigateToListPage() }
                )
        }    ) { innerPadding ->
        Column {
            if (contentType == AsteroidsContentType.ListAndDetail) {
                AsteroidsListAndDetails(
                    asteroids = uiState.asteroids,
                    currentAsteroid = uiState.currentAsteroid,
                    pictureOfTheDay = uiState.pictureOfTheDay,
                    onChangeCurrentAsteroid = { asteroid -> viewModel.updateCurrentAsteroid(asteroid) },
                    contentPadding = innerPadding
                )
            } else {
                Log.i("AsteroidApp", "not ListAndDetail")
                if (uiState.isShowingListPage) {
                    Log.i("AsteroidApp", "isShowingListPage")
                    AsteroidsList(
                        asteroids = uiState.asteroids,
                        pictureOfTheDay = uiState.pictureOfTheDay,
                        currentAsteroid = uiState.currentAsteroid,
                        onAsteroidClicked = {
                            viewModel.updateCurrentAsteroid(it)
                            viewModel.navigateToDetailPage()},
                        contentPadding = innerPadding
                    )
                } else {
                    Log.i("AsteroidApp", "NotShowingListPage")
                    AsteroidDetail(
                        asteroid = uiState.currentAsteroid,
                        navigateBack = { viewModel.navigateToListPage() },
                        contentPadding = PaddingValues(
                            top = innerPadding.calculateTopPadding(),
                            start = dimensionResource(id = R.dimen.padding_medium),
                            end = dimensionResource(id = R.dimen.padding_medium)
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroidTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    contentType: AsteroidsContentType,
    canNavigateBack: Boolean = false,
    navigateUp: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.displayLarge
            )},
        navigationIcon = if (canNavigateBack) {
            {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back button"
                    )
                }
            }
        } else {
            { Box {} }
        },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}