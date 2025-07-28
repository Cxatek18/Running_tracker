package com.example.runningtracker.runtracker.domain.usecases

import com.example.runningtracker.runtracker.domain.module.RunTrackerModule
import com.example.runningtracker.runtracker.domain.repository.RunningTrackerDbRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRunTrackerUseCase @Inject constructor(
    private val repository: RunningTrackerDbRepository
) {
    operator fun invoke(idRunTracker: Int): Flow<RunTrackerModule?> {
        return repository.observeRunTracker(
            idRunTracker = idRunTracker
        )
    }
}