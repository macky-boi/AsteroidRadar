package com.example.asteroidradar

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.asteroidradar.data.local.asteroid.AsteroidDao
import com.example.asteroidradar.data.local.AsteroidRadarDatabase
import com.example.asteroidradar.utils.DateUtils
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.util.Date

class AsteroidEntityDaoInstrumentedTest {

    private lateinit var asteroidDao: AsteroidDao
    private lateinit var asteroidRadarDatabase: AsteroidRadarDatabase

    @Before
    fun createDB() {
        val context: Context = ApplicationProvider.getApplicationContext()

        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        asteroidRadarDatabase = Room.inMemoryDatabaseBuilder(context, AsteroidRadarDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        asteroidDao = asteroidRadarDatabase.asteroidDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        asteroidRadarDatabase.close()
    }

    @Test
    fun testInsertAsteroids() = runBlocking {
        asteroidDao.insertAsteroids(sampleAsteroidEntities)
        val allAsteroids = asteroidDao.getAllAsteroids().value!!
        assertEquals(allAsteroids[0], sampleAsteroidEntities[0])
    }

    @Test
    fun testGetAllAsteroids() = runBlocking {
        asteroidDao.insertAsteroids(sampleAsteroidEntities)
        val allAsteroids = asteroidDao.getAllAsteroids().value!!
        assertEquals(allAsteroids[0], sampleAsteroidEntities[0])
        assertEquals(allAsteroids[1], sampleAsteroidEntities[1])
        assertEquals(allAsteroids.size, sampleAsteroidEntities.size)
    }

    @Test
    fun testGetAsteroid() = runBlocking {
        asteroidDao.insertAsteroids(sampleAsteroidEntities)
        val asteroid = asteroidDao.getAsteroid("2024CD2").value!!
        assertEquals(asteroid, sampleAsteroidEntities[1])
    }

    @Test
    fun testDeleteAsteroidsFromThePast() = runBlocking {
        asteroidDao.insertAsteroids(sampleAsteroidEntities)

        val currentDate = Date()
        asteroidDao.deleteAsteroidsFrom(currentDate)

        val allAsteroids = asteroidDao.getAllAsteroids().value!!
        assertEquals(0, allAsteroids.size)
    }

    @Test
    fun testGetCount(): Unit = runBlocking {
        assertEquals(0, asteroidDao.getCount())
    }

    @Test
    fun testGetLatestDate(): Unit = runBlocking {
        asteroidDao.insertAsteroids(sampleAsteroidEntities)

        val latestDate = asteroidDao.getLatestDate()!!
        val latestDateString = DateUtils().dateToString(latestDate)

        assertEquals(sampleAsteroidEntities[0].date,  latestDateString)
    }

}