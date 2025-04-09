package com.example.asteroidradar

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.asteroidradar.data.local.AsteroidRadarDatabase
import com.example.asteroidradar.data.remote.NetworkModule
import com.example.asteroidradar.data.repository.AsteroidRadarRepository
import com.example.asteroidradar.data.repository.AsteroidRadarRepositoryImpl
import com.example.asteroidradar.data.repository.WorkManagerRepository
import com.example.asteroidradar.data.repository.WorkManagerRepositoryImpl

interface AppContainer {
    val workManagerRepository: WorkManagerRepository
    val asteroidRadarRepository: AsteroidRadarRepository
}

@RequiresApi(Build.VERSION_CODES.O)
class DefaultAppContainer(context: Context): AppContainer {

    private val networkModule = NetworkModule()

    private val database: AsteroidRadarDatabase by lazy {
        AsteroidRadarDatabase.getDatabase(context)
    }
    private val asteroidDao = database.asteroidDao()
    private val pictureOfTheDayDao = database.pictureOfTheDayDao()

    override val asteroidRadarRepository: AsteroidRadarRepository by lazy {
        AsteroidRadarRepositoryImpl(asteroidDao, pictureOfTheDayDao, networkModule.service)
    }


    override val workManagerRepository: WorkManagerRepository by lazy {
        WorkManagerRepositoryImpl(context)
    }

}