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

    private val samplePictureOfTheDay = PictureOfTheDay(
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