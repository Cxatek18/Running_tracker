package com.example.runningtracker.domain.usecases.running_tracker

import com.example.runningtracker.domain.module.running_tracker.RunTrackerModule
import com.example.runningtracker.domain.repository.running_tracker.RunningTrackerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StartRunningUseCase @Inject constructor(
    private val repository: RunningTrackerRepository
) {
    operator fun invoke(
        startPointLocationLatitude: Double,
        startPointLocationLongitude: Double
    ): Flow<RunTrackerModule?> {
        return repository.startRunning(
            startPointLocationLatitude = startPointLocationLatitude,
            startPointLocationLongitude = startPointLocationLongitude
        )
    }
}