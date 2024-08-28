package com.example.asteroidradar.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.asteroidradar.AsteroidRadarApplication

private const val TAG = "UpdateAsteroidsWorker"

class UpdateAsteroidsWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    private val appContext = applicationContext as AsteroidRadarApplication
    private val databaseRepository = appContext.container.asteroidDatabaseRepository
    private val fetchAsteroidsUseCase = appContext.container.fetchAsteroidsUseCase

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