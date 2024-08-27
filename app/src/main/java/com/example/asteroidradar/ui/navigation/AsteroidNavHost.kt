package com.example.asteroidradar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.asteroidradar.ui.screens.asteroidDetails.AsteroidDetailScreen
import com.example.asteroidradar.ui.screens.asteroidDetails.AsteroidDetailsDestination
import com.example.asteroidradar.ui.screens.asteroids.AsteroidsScreenDestination
import com.example.asteroidradar.ui.screens.asteroids.AsteroidsScreen

@Composable
fun AsteroidNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AsteroidsScreenDestination.route,
        modifier = modifier
    ) {
        composable(route = AsteroidsScreenDestination.route) {
            AsteroidsScreen(
                navigateToAsteroidDetails = {
                    navController.navigate("${AsteroidDetailsDestination.route}/${it}")
                }
            )
        }
        composable(
            route = AsteroidDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(AsteroidDetailsDestination.ASTEROID_ID_ARG) {
                type = NavType.StringType
            })
        ) {
            AsteroidDetailScreen(
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}