//package com.example.asteroidradar.ui.screens
//
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.calculateEndPadding
//import androidx.compose.foundation.layout.calculateStartPadding
//import androidx.compose.foundation.layout.padding
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.dimensionResource
//import androidx.compose.ui.unit.LayoutDirection
//import androidx.compose.ui.unit.dp
//import com.example.asteroidradar.data.local.asteroid.Asteroid
//import com.example.asteroidradar.data.local.pictureOfTheDay.PictureOfTheDay
//import com.example.asteroidradar.R
//import com.example.asteroidradar.ui.screens.AsteroidDetail
//import com.example.asteroidradar.ui.screens.AsteroidsList
//
//
//@Composable
//fun AsteroidsListAndDetails(
//    asteroids: List<Asteroid>,
//    pictureOfTheDay: PictureOfTheDay?,
//    modifier: Modifier = Modifier,
//    currentAsteroid: Asteroid,
//    contentPadding: PaddingValues = PaddingValues(0.dp),
//    onChangeCurrentAsteroid: (Asteroid) -> Unit,
//) {
//    Row(
//        modifier = modifier.padding(
//            top = contentPadding.calculateTopPadding(),
//            start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
//            end = contentPadding.calculateEndPadding(LayoutDirection.Ltr),
//            bottom = contentPadding.calculateBottomPadding()
//        )
//    ){
//        AsteroidsList(
//            modifier = Modifier.weight(2.3f),
//            asteroids = asteroids,
//            pictureOfTheDay = pictureOfTheDay,
//            currentAsteroid = currentAsteroid,
//            onAsteroidClicked = onChangeCurrentAsteroid
//        )
//
//        AsteroidDetail(
//            modifier = Modifier
//                .weight(3f),
//            asteroid = currentAsteroid
//        )
//    }
//}