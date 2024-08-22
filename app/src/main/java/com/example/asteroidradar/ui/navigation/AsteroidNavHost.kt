package com.example.asteroidradar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.asteroidradar.ui.asteroids.AsteroidScreenDestination
import com.example.asteroidradar.ui.asteroids.AsteroidsScreen

@Composable
fun AsteroidNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AsteroidScreenDestination.route,
        modifier = modifier
    ) {
        composable(route = AsteroidScreenDestination.route) {
            AsteroidsScreen()
        }
    }
}