package com.example.runningtracker.domain.usecases.running_tracker

import com.example.runningtracker.domain.repository.running_tracker.RunningTrackerRepository
import javax.inject.Inject

class UpdateTimeTrackUseCase @Inject constructor(
    private val repository: RunningTrackerRepository
) {
    suspend operator fun invoke(timeTrack: Int) {
        repository.updateTimeTrack(
            timeTrack = timeTrack
        )
    }
}