package com.example.asteroidradar.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.asteroidradar.data.local.asteroid.Asteroid
import com.example.asteroidradar.data.local.asteroid.AsteroidDao
import com.example.asteroidradar.data.local.pictureOfTheDay.PictureOfTheDay
import com.example.asteroidradar.data.local.pictureOfTheDay.PictureOfTheDayDao

@Database(entities = [Asteroid::class, PictureOfTheDay::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AsteroidRadarDatabase : RoomDatabase() {

    abstract fun asteroidDao(): AsteroidDao
    abstract fun pictureOfTheDayDao(): PictureOfTheDayDao

    companion object {
        @Volatile
        private var Instance: AsteroidRadarDatabase? = null

        fun getDatabase(context: Context): AsteroidRadarDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AsteroidRadarDatabase::class.java, "asteroid_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}