package com.example.asteroidradar.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface AsteroidDao {
    @Query(value = "SELECT * from asteroid WHERE id = :id")
    fun getAsteroid(id: String): Flow<Asteroid>

    @Query("SELECT * from asteroid")
    fun getAllAsteroids(): Flow<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsteroids(asteroids: List<Asteroid>)

    @Query("DELETE from asteroid WHERE date < :dateToday")
    suspend fun deleteAsteroidsFromThePast(dateToday: Date)
}