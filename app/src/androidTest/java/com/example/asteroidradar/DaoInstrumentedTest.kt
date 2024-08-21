package com.example.asteroidradar

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.asteroidradar.data.local.AsteroidEntity
import com.example.asteroidradar.data.local.AsteroidDao
import com.example.asteroidradar.data.local.AsteroidDatabase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DaoInstrumentedTest {

    private lateinit var asteroidDao: AsteroidDao
    private lateinit var asteroidDatabase: AsteroidDatabase

    private val asteroidEntities = listOf(
        AsteroidEntity(
            id = "2024AB1",
            name = "AsteroidEntity 2024AB1",
            date = "2024-08-08",
            isHazardous = true,
            absoluteMagnitude = 17.5,
            closeApproachDate = "2024-08-01",
            missDistanceAstronomical = "0.3978248012",
            relativeVelocityKilometersPerSecond = "13.2766885381"
        ),
        AsteroidEntity(
            id = "2024CD2",
            name = "AsteroidEntity 2024CD2",
            date = "2024-08-01",
            isHazardous = false,
            absoluteMagnitude = 22.1,
            closeApproachDate = "2024-08-05",
            missDistanceAstronomical = "1.2345678901",
            relativeVelocityKilometersPerSecond = "5.6789012345"
        ),
        AsteroidEntity(
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

    @Before
    fun createDB() {
        val context: Context = ApplicationProvider.getApplicationContext()

        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        asteroidDatabase = Room.inMemoryDatabaseBuilder(context, AsteroidDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        asteroidDao = asteroidDatabase.asteroidDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        asteroidDatabase.close()
    }

    @Test
    fun testInsertAsteroids() = runBlocking {
        asteroidDao.insertAsteroids(asteroidEntities)
        val allAsteroids = asteroidDao.getAllAsteroids().first()
        assertEquals(allAsteroids[0], asteroidEntities[0])
    }

    @Test
    fun testGetAllAsteroids() = runBlocking {
        asteroidDao.insertAsteroids(asteroidEntities)
        val allAsteroids = asteroidDao.getAllAsteroids().first()
        assertEquals(allAsteroids[0], asteroidEntities[0])
        assertEquals(allAsteroids[1], asteroidEntities[1])
        assertEquals(allAsteroids.size, asteroidEntities.size)
    }

    @Test
    fun testGetAsteroid() = runBlocking {
        asteroidDao.insertAsteroids(asteroidEntities)
        val asteroid = asteroidDao.getAsteroid("2024CD2").first()
        assertEquals(asteroid, asteroidEntities[1])
    }

    @Test
    fun testDeleteAsteroidsFromThePast() = runBlocking {
        asteroidDao.insertAsteroids(asteroidEntities)

        val currentDate = Date()
        asteroidDao.deleteAsteroidsFrom(currentDate)

        val allAsteroids = asteroidDao.getAllAsteroids().first()
        assertEquals(1, allAsteroids.size)
    }

    @Test
    fun testGetCount(): Unit = runBlocking {
        assertEquals(0, asteroidDao.getCount())
    }

    @Test
    fun testGetLatestDate(): Unit = runBlocking {
        asteroidDao.insertAsteroids(asteroidEntities)

        val latestDate = asteroidDao.getLatestDate()!!
        val latestDateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(latestDate)

        assertEquals(asteroidEntities[0].date,  latestDateString)
    }

}