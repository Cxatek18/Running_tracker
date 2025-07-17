package com.example.runningtracker.domain.usecases.running_tracker.locale_db

import com.example.runningtracker.domain.module.running_tracker.PointTrack
import com.example.runningtracker.domain.repository.running_tracker.locale_db.RunningTrackerDbRepository
import javax.inject.Inject

class SaveRunTrackerToDbUseCase @Inject constructor(
    private val repository: RunningTrackerDbRepository
) {
    suspend operator fun invoke(
        timeTrack: Int,
        distanceTrack: String,
        wayTrack: List<PointTrack>
    ) {
        repository.saveRunTrackerToDb(
            timeTrack = timeTrack,
            distanceTrack = distanceTrack,
            wayTrack = wayTrack
        )
    }
}