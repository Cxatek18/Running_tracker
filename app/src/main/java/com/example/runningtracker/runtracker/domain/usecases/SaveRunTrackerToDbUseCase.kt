package com.example.runningtracker.runtracker.domain.usecases

import com.example.runningtracker.runtracker.domain.repository.RunningTrackerDbRepository
import javax.inject.Inject

class SaveRunTrackerToDbUseCase @Inject constructor(
    private val repository: RunningTrackerDbRepository
) {
    suspend operator fun invoke(
        timeTrack: Int,
        distanceTrack: String,
        key: String,
    ) {
        repository.saveRunTrackerToDb(
            timeTrack = timeTrack,
            distanceTrack = distanceTrack,
            key = key
        )
    }
}