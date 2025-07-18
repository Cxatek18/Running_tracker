package com.example.runningtracker.domain.repository.running_tracker.locale_db

import com.example.runningtracker.domain.module.running_tracker.PointTrack

interface RunningTrackerDbRepository {

    suspend fun saveRunTrackerToDb(
        timeTrack: Int,
        distanceTrack: String,
        wayTrack: List<PointTrack>
    )
}