package com.example.runningtracker.domain.repository.running_tracker

import com.example.runningtracker.domain.module.running_tracker.RunTrackerModule
import kotlinx.coroutines.flow.Flow

interface RunningTrackerRepository {

    fun startRunning(
        startPointLocationLatitude: Double,
        startPointLocationLongitude: Double
    ): Flow<RunTrackerModule?>

    suspend fun setInfoInRunning(
        runTrackerModule: RunTrackerModule
    )

    suspend fun updateDistanceTrack(
        distanceTrack: String
    )

    suspend fun updateWayTrack(
        pointLocationLatitude: Double,
        pointLocationLongitude: Double
    )

    suspend fun updateTimeTrack(
        timeTrack: Int
    )

    suspend fun stopTrack()
}