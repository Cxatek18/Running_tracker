package com.example.runningtracker.domain.repository.running_tracker

import com.example.runningtracker.domain.module.running_tracker.RunningTrackerBackgroundModule
import kotlinx.coroutines.flow.Flow

interface RunningTrackerBackgroundRepository {

    fun getRunningTrackerBackgroundModule(): Flow<RunningTrackerBackgroundModule>

    suspend fun changeIsBackgroundWork(value: Boolean)
}