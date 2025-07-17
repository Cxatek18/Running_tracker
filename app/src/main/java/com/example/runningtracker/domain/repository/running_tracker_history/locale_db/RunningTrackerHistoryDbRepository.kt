package com.example.runningtracker.domain.repository.running_tracker_history.locale_db

import com.example.runningtracker.domain.module.running_tracker.RunTrackerModule
import kotlinx.coroutines.flow.Flow

interface RunningTrackerHistoryDbRepository {

    fun getListRunTrackerFromDb(): Flow<List<RunTrackerModule>>

    suspend fun deleteRunTrackerToDb(idRunTracker: Int)
}