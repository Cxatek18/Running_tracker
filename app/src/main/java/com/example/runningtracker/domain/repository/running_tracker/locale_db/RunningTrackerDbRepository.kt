package com.example.runningtracker.domain.repository.running_tracker.locale_db

import com.example.runningtracker.domain.module.running_tracker.PointTrack
import com.example.runningtracker.domain.module.running_tracker.RunTrackerModule

interface RunningTrackerDbRepository {

    suspend fun getRunTrackerFromDb(idRunTracker: Int): RunTrackerModule?

    suspend fun saveRunTrackerToDb(
        timeTrack: Int,
        distanceTrack: String,
        wayTrack: List<PointTrack>
    )
}