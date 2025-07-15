package com.example.runningtracker.domain.usecases.running_tracker

import com.example.runningtracker.domain.module.running_tracker.RunTrackerModule
import com.example.runningtracker.domain.repository.running_tracker.RunningTrackerRepository
import javax.inject.Inject

class SetInfoInRunningUseCase @Inject constructor(
    private val repository: RunningTrackerRepository
) {
    suspend operator fun invoke(runTrackerModule: RunTrackerModule) {
        repository.setInfoInRunning(
            runTrackerModule = runTrackerModule
        )
    }
}