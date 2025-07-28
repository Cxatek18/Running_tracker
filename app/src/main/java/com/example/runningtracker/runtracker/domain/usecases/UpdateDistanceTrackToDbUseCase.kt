package com.example.runningtracker.runtracker.domain.usecases

import com.example.runningtracker.runtracker.domain.repository.RunningTrackerDbRepository
import javax.inject.Inject

class UpdateDistanceTrackToDbUseCase @Inject constructor(
    private val repository: RunningTrackerDbRepository
) {
    suspend operator fun invoke(
        trackId: Int,
        distanceTrack: String
    ) {
        repository.updateDistanceTrackToDb(
            trackId = trackId,
            distanceTrack = distanceTrack
        )
    }
}