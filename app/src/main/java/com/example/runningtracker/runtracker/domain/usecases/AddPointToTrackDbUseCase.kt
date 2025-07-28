package com.example.runningtracker.runtracker.domain.usecases

import com.example.runningtracker.runtracker.domain.repository.RunningTrackerDbRepository
import javax.inject.Inject

class AddPointToTrackDbUseCase @Inject constructor(
    private val repository: RunningTrackerDbRepository
) {
    suspend operator fun invoke(
        trackId: Int,
        numberPoint: Int,
        trackLatitude: Double,
        trackLongitude: Double
    ) {
        repository.addPointToTrackDb(
            trackId = trackId,
            numberPoint = numberPoint,
            trackLatitude = trackLatitude,
            trackLongitude = trackLongitude
        )
    }
}