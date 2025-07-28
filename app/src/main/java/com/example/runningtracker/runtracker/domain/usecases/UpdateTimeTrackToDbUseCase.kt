package com.example.runningtracker.runtracker.domain.usecases

import com.example.runningtracker.runtracker.domain.repository.RunningTrackerDbRepository
import javax.inject.Inject

class UpdateTimeTrackToDbUseCase @Inject constructor(
    private val repository: RunningTrackerDbRepository
) {
    suspend operator fun invoke(
        trackId: Int,
        time: Int
    ) {
        repository.updateTimeTrackToDb(
            trackId = trackId,
            time = time
        )
    }
}