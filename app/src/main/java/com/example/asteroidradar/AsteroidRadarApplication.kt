package com.example.asteroidradar

import android.app.Application
import android.util.Log
import com.example.asteroidradar.data.AppContainer
import com.example.asteroidradar.data.DefaultAppContainer

const val TAG = "AsteroidRadarApplication"

class AsteroidRadarApplication: Application()
{
    lateinit var container: AppContainer

    override fun onCreate() {
        Log.i(TAG, "onCreate")
        super.onCreate()

        container = DefaultAppContainer(this)

        container.workManagerRepository.periodicallyUpdateAsteroids()
    }
}




