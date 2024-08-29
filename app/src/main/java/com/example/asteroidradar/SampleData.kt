package com.example.asteroidradar

import com.example.asteroidradar.data.local.asteroid.Asteroid
import com.example.asteroidradar.data.local.pictureOfTheDay.PictureOfTheDay

val sampleAsteroids = listOf(
    Asteroid(
        id = "2024AB1",
        name = "10115 (1992 SK)",
        date = "2024-08-08",
        isHazardous = true,
        absoluteMagnitude = 17.5,
        closeApproachDate = "2024-08-01",
        missDistanceAstronomical = "0.3978248012",
        relativeVelocityKilometersPerSecond = "13.2766885381"
    ),
    Asteroid(
        id = "2024CD2",
        name = "2005 QQ87",
        date = "2024-08-01",
        isHazardous = false,
        absoluteMagnitude = 22.1,
        closeApproachDate = "2024-08-05",
        missDistanceAstronomical = "1.2345678901",
        relativeVelocityKilometersPerSecond = "5.6789012345"
    ),
    Asteroid(
        id = "2024EF3",
        name = "2009 EP2",
        date = "2024-08-07",
        isHazardous = false,
        absoluteMagnitude = 19.8,
        closeApproachDate = "2024-08-10",
        missDistanceAstronomical = "0.4567890123",
        relativeVelocityKilometersPerSecond = "7.8901234567"
    )
)

val samplePictureOfTheDay = PictureOfTheDay(
    date = "2024-08-28",
    explanation = "When can you see a black hole, a tulip, and a swan all at once? At night -- " +
            "if the timing is right, and if your telescope is pointed in the right direction.  " +
            "The complex and beautiful Tulip Nebula blossoms about 8,000 light-years away toward " +
            "the constellation of Cygnus the Swan.  Ultraviolet radiation from young energetic stars " +
            "at the edge of the Cygnus OB3 association, including O star HDE 227018, ionizes the" +
            " atoms and powers the emission from the Tulip Nebula.  Stewart Sharpless cataloged " +
            "this nearly 70 light-years across reddish glowing cloud of interstellar gas and dust" +
            " in 1959, as Sh2-101. Also in the featured field of view is the black hole Cygnus X-1," +
            " which to be a microquasar because it is one of strongest X-ray sources in planet Earth's sky." +
            " Blasted by powerful jets from a lurking black hole, its fainter bluish curved shock front is " +
            "only faintly visible beyond the cosmic Tulip's petals, near the right side of the frame.   " +
            "Back to School? Learn Science with NASA",
    url = "https://apod.nasa.gov/apod/image/2408/Tulip_Shastry_1080.jpg",
    title = "Tulip Nebula and Black Hole Cygnus X-1"
)