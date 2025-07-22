package com.example.runningtracker.domain.usecases.running_tracker

import com.example.runningtracker.domain.module.running_tracker.RunningTrackerBackgroundModule
import com.example.runningtracker.domain.repository.running_tracker.RunningTrackerBackgroundRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRunningTrackerBackgroundModuleUseCase @Inject constructor(
    private val repository: RunningTrackerBackgroundRepository
) {
    operator fun invoke(): Flow<RunningTrackerBackgroundModule> {
        return repository.getRunningTrackerBackgroundModule()
    }
}