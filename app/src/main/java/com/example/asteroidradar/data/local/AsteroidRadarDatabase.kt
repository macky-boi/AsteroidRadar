package com.example.asteroidradar.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.asteroidradar.data.local.asteroid.AsteroidEntity
import com.example.asteroidradar.data.local.asteroid.AsteroidDao


@Database(entities = [AsteroidEntity::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AsteroidRadarDatabase : RoomDatabase() {

    abstract fun asteroidDao(): AsteroidDao

    companion object {
        @Volatile
        private var Instance: AsteroidRadarDatabase? = null

        fun getDatabase(context: Context): AsteroidRadarDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AsteroidRadarDatabase::class.java, "asteroid_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}