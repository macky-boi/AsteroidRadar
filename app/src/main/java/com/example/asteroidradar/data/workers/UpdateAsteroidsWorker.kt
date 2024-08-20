package com.example.asteroidradar.data.workers

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.await
import com.example.asteroidradar.AsteroidRadarApplication
import com.example.asteroidradar.FETCH_ASTEROIDS_WORK_NAME
import com.example.asteroidradar.FILTER_ASTEROIDS_WORK_NAME
import com.example.asteroidradar.SAVE_ASTEROIDS_WORK_NAME
import com.example.asteroidradar.data.remote.AsteroidNetwork
import com.example.asteroidradar.data.remote.AsteroidsNetwork
import retrofit2.HttpException
import java.io.IOException

private const val TAG = "UpdateAsteroidsWorker"

class UpdateAsteroidsWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    private val appContext = applicationContext as AsteroidRadarApplication
    private val databaseRepository = appContext.container.asteroidDatabaseRepository
    private val fetchAsteroidsUseCase = appContext.container.fetchAsteroidsUseCase

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        Log.i("UpdateAsteroidsWorker", "doWork")

        databaseRepository.deleteAllAsteroidsFromThePast()

        lateinit var result: Result

        val fetchedDataResponse = fetchAsteroidsUseCase()
        fetchedDataResponse.onSuccess { asteroidsNetwork ->
            val asteroidsEntity = asteroidsNetwork.toEntity()
            asteroidsEntity.forEach { (_, asteroid) ->
                databaseRepository.insertAsteroids(asteroid)
            }
            result = Result.success()
        }.onFailure {
            result = Result.failure()
        }

        return result
    }


}