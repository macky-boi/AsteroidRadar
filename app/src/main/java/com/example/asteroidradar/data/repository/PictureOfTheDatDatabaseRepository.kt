package com.example.asteroidradar.data.repository

import com.example.asteroidradar.data.local.asteroid.Asteroid
import com.example.asteroidradar.data.local.asteroid.AsteroidDao
import com.example.asteroidradar.data.local.pictureOfTheDay.PictureOfTheDay
import com.example.asteroidradar.data.local.pictureOfTheDay.PictureOfTheDayDao
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface PictureOfTheDatabaseRepository  {
    suspend fun insertPicture(picture: PictureOfTheDay)
    fun getPictureByDate(date: Date): Flow<PictureOfTheDay>
    suspend fun deletePictureFrom(dateToday: Date)
    suspend fun clearPictures()
    suspend fun getCount(): Int
}

private const val TAG = "AsteroidDatabaseRepositoryImpl"

class PictureOfTheDatabaseRepositoryImpl  (
    private val pictureOfTheDayDao: PictureOfTheDayDao
): PictureOfTheDatabaseRepository {

    override suspend fun insertPicture(picture: PictureOfTheDay) =
        pictureOfTheDayDao.insertPicture(picture)

    override fun getPictureByDate(date: Date): Flow<PictureOfTheDay> =
        pictureOfTheDayDao.getPictureByDate(date)

    override suspend fun deletePictureFrom(dateToday: Date) =
        pictureOfTheDayDao.deletePictureFrom(dateToday)

    override suspend fun clearPictures() =
        pictureOfTheDayDao.clearPictures()

    override suspend fun getCount(): Int =
        pictureOfTheDayDao.getCount()
}