package com.example.asteroidradar

import android.app.Application
import android.util.Log
import com.example.asteroidradar.data.AppContainer
import com.example.asteroidradar.data.DefaultAppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import org.json.JSONObject

const val TAG = "AsteroidRadarApplication"

class AsteroidRadarApplication: Application() {
    lateinit var container: AppContainer

    val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        Log.i(TAG, "onCreate")
        super.onCreate()
        container = DefaultAppContainer(applicationContext)
    }
}



