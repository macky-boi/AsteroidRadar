package com.example.asteroidradar.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.asteroidradar.ui.screens.asteroidDetails.AsteroidDetailScreen
import com.example.asteroidradar.ui.screens.asteroidDetails.AsteroidDetailsDestination
import com.example.asteroidradar.ui.screens.asteroidDetails.AsteroidDetailsViewModel
import com.example.asteroidradar.ui.screens.asteroids.AsteroidsScreenDestination
import com.example.asteroidradar.ui.screens.asteroids.AsteroidsScreen
import com.example.asteroidradar.ui.screens.asteroids.AsteroidsViewModel

@Composable
fun AsteroidNavHost(
    windowSize: WindowWidthSizeClass,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AsteroidsScreenDestination.route,
        modifier = modifier
    ) {
        composable(route = AsteroidsScreenDestination.route) {
            val viewModel: AsteroidsViewModel = viewModel(factory = AsteroidsViewModel.Factory)
            AsteroidsScreen(
                windowSize = windowSize,
                viewModel = viewModel,
                onAsteroidItemPressed = { asteroidId ->
                    viewModel.setSelectedAsteroidId(asteroidId)
                    navController.navigate(AsteroidDetailsDestination.route)
                }
            )
        }
        composable(
            route = AsteroidDetailsDestination.route
        ) {
            AsteroidDetailScreen(
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}