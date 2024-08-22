package com.example.asteroidradar.ui

import com.example.asteroidradar.data.local.Asteroid

val sampleAsteroids = listOf(
    Asteroid(
        id = "2024AB1",
        name = "AsteroidEntity 2024AB1",
        date = "2024-08-08",
        isHazardous = true,
        absoluteMagnitude = 17.5,
        closeApproachDate = "2024-08-01",
        missDistanceAstronomical = "0.3978248012",
        relativeVelocityKilometersPerSecond = "13.2766885381"
    ),
    Asteroid(
        id = "2024CD2",
        name = "AsteroidEntity 2024CD2",
        date = "2024-08-01",
        isHazardous = false,
        absoluteMagnitude = 22.1,
        closeApproachDate = "2024-08-05",
        missDistanceAstronomical = "1.2345678901",
        relativeVelocityKilometersPerSecond = "5.6789012345"
    ),
    Asteroid(
        id = "2024EF3",
        name = "AsteroidEntity 2024EF3",
        date = "2024-08-07",
        isHazardous = false,
        absoluteMagnitude = 19.8,
        closeApproachDate = "2024-08-10",
        missDistanceAstronomical = "0.4567890123",
        relativeVelocityKilometersPerSecond = "7.8901234567"
    )
)