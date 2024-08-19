package com.example.asteroidradar.data.repository

class AsteroidRepository(
    private val asteroidDatabaseRepository: AsteroidDatabaseRepository,
    private val asteroidNetworkRepository: AsteroidNetworkRepository,
    private val workManagerRepository: WorkManagerRepository
) {

    suspend fun getAsteroids(): List<Asteroid> {
        // Fetch from database first
        val cachedAsteroids = asteroidDatabaseRepository.getAsteroids()
        return if (cachedAsteroids.isNotEmpty()) {
            cachedAsteroids
        } else {
            // If the database is empty, fetch from the network
            val networkAsteroids = asteroidNetworkRepository.fetchAsteroids()
            asteroidDatabaseRepository.saveAsteroids(networkAsteroids)
            networkAsteroids
        }
    }

    fun schedulePeriodicUpdates() {
        workManagerRepository.periodicallyUpdateAsteroids()
    }
}