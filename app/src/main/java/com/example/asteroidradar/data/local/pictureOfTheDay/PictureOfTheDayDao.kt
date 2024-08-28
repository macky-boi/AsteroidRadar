package com.example.asteroidradar.data.local.pictureOfTheDay

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface PictureOfTheDayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPicture(picture: PictureOfTheDay)

    @Query("SELECT * FROM pictureOfTheDay WHERE date = :date")
    fun getPictureByDate(date: Date): Flow<PictureOfTheDay>

    @Query("DELETE from pictureOfTheDay WHERE date < :dateToday")
    suspend fun deletePictureFrom(dateToday: Date)

    @Query("DELETE FROM pictureOfTheDay")
    suspend fun clearPictures()

    @Query("SELECT COUNT(*) FROM pictureOfTheDay")
    suspend fun getCount(): Int
}