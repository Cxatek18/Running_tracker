package com.example.runningtracker.domain.usecases.running_tracker

import com.example.runningtracker.domain.repository.running_tracker.RunningTrackerRepository
import javax.inject.Inject

class StopTrackUseCase @Inject constructor(
    private val repository: RunningTrackerRepository
) {
    suspend operator fun invoke() {
        repository.stopTrack()
    }
}