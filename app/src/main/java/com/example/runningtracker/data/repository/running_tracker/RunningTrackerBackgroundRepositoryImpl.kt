package com.example.runningtracker.data.repository.running_tracker

import com.example.runningtracker.domain.module.running_tracker.RunningTrackerBackgroundModule
import com.example.runningtracker.domain.repository.running_tracker.RunningTrackerBackgroundRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class RunningTrackerBackgroundRepositoryImpl @Inject constructor() :
    RunningTrackerBackgroundRepository {

    private val runningTrackerBackground = MutableStateFlow<RunningTrackerBackgroundModule>(
        RunningTrackerBackgroundModule()
    )

    override suspend fun changeIsBackgroundWork(value: Boolean) {
        runningTrackerBackground.update {
            it.copy(
                isBackground = value
            )
        }
    }

    override fun getRunningTrackerBackgroundModule(): Flow<RunningTrackerBackgroundModule> =
        runningTrackerBackground.asStateFlow()
}