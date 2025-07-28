package com.example.runningtracker.runtracker.domain.repository

import com.example.runningtracker.runtracker.domain.module.RunTrackerModule
import kotlinx.coroutines.flow.Flow

interface RunningTrackerDbRepository {

    suspend fun saveRunTrackerToDb(
        timeTrack: Int,
        distanceTrack: String,
        key: String
    )

    suspend fun updateTimeTrackToDb(
        trackId: Int,
        time: Int
    )

    suspend fun updateDistanceTrackToDb(
        trackId: Int,
        distanceTrack: String
    )

    suspend fun addPointToTrackDb(
        trackId: Int,
        numberPoint: Int,
        trackLatitude: Double,
        trackLongitude: Double
    )

    suspend fun getTrackIdInSharedPreferences(
        key: String
    ): Flow<Int?>

    suspend fun removeTrackIdInSharedPreferences(
        key: String
    )

    fun observeRunTracker(idRunTracker: Int): Flow<RunTrackerModule?>
}