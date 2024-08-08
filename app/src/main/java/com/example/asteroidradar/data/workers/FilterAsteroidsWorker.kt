package com.example.asteroidradar.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class FilterAsteroidsWorker(
    ctx: Context,
    params: WorkerParameters,
): CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return Result.success()
    }
}