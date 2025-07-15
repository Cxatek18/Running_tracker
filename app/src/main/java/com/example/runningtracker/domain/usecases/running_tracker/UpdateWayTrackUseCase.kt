package com.example.runningtracker.domain.usecases.running_tracker

import com.example.runningtracker.domain.repository.running_tracker.RunningTrackerRepository
import javax.inject.Inject

class UpdateWayTrackUseCase @Inject constructor(
    private val repository: RunningTrackerRepository
) {
    suspend operator fun invoke(
        pointLocationLatitude: Double,
        pointLocationLongitude: Double
    ) {
        repository.updateWayTrack(
            pointLocationLatitude = pointLocationLatitude,
            pointLocationLongitude = pointLocationLongitude
        )
    }
}