package com.example.asteroidradar.data.local.asteroid

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.Date

@Dao
interface AsteroidDao {
    @Query(value = "SELECT * from asteroid WHERE id = :id")
    fun getAsteroid(id: String): LiveData<AsteroidEntity>

    @Query("SELECT * from asteroid")
    fun getAllAsteroids(): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsteroids(asteroidEntityEntities: List<AsteroidEntity>)

    @Query("DELETE from asteroid WHERE date < :dateToday")
    suspend fun deleteAsteroidsFrom(dateToday: Date)

    @Query("SELECT MAX(date) FROM asteroid")
    suspend fun getLatestDate(): Date?

    @Query("SELECT COUNT(*) FROM asteroid")
    suspend fun getCount(): Int
}