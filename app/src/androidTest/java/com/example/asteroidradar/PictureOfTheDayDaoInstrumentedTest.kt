package com.example.asteroidradar

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.asteroidradar.data.local.asteroid.AsteroidDao
import com.example.asteroidradar.data.local.AsteroidRadarDatabase
import com.example.asteroidradar.data.local.Converters
import com.example.asteroidradar.data.local.pictureOfTheDay.PictureOfTheDay
import com.example.asteroidradar.data.local.pictureOfTheDay.PictureOfTheDayDao
import com.example.asteroidradar.utils.DateUtils
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.util.Date

class PictureOfTheDayDaoInstrumentedTest {
    private lateinit var pictureOfTheDayDao: PictureOfTheDayDao
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
        pictureOfTheDayDao = asteroidRadarDatabase.pictureOfTheDayDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        asteroidRadarDatabase.close()
    }

    @Test
    fun testGetCount() = runBlocking {
        assertEquals(0, pictureOfTheDayDao.getCount())
        pictureOfTheDayDao.insertPicture(samplePictureOfTheDay)
        assertEquals(1, pictureOfTheDayDao.getCount())
    }

    @Test
    fun testClearPictures() = runBlocking {
        pictureOfTheDayDao.insertPicture(samplePictureOfTheDay)
        val count = pictureOfTheDayDao.getCount()
        assertEquals(1, count)
    }

    @Test
    fun testDeletePicturesFrom() = runBlocking {
        val pastPicture = PictureOfTheDay(
            date = "2024-07-28",
            explanation = "",
            title = "",
            url = ""
        )

        pictureOfTheDayDao.insertPicture(pastPicture)

        val currentDate = Date()
        val currentDateString = DateUtils().dateToString(currentDate)

        val presentPicture = PictureOfTheDay(
            date = currentDateString,
            explanation = "",
            title = "",
            url = ""
        )

        pictureOfTheDayDao.insertPicture(presentPicture)

        pictureOfTheDayDao.deletePictureFrom(currentDate)

        assertEquals(1, pictureOfTheDayDao.getCount())
    }


}