package com.example.asteroidradar.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [AsteroidEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AsteroidDatabase : RoomDatabase() {

    abstract fun asteroidDao(): AsteroidDao

    companion object {
        @Volatile
        private var Instance: AsteroidDatabase? = null

        fun getDatabase(context: Context): AsteroidDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AsteroidDatabase::class.java, "asteroid_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}