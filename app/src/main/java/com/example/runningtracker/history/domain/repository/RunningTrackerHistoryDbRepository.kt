package com.example.runningtracker.history.domain.repository

import com.example.runningtracker.runtracker.domain.module.RunTrackerModule
import kotlinx.coroutines.flow.Flow

interface RunningTrackerHistoryDbRepository {

    fun getListRunTrackerFromDb(): Flow<List<RunTrackerModule>>

    suspend fun deleteRunTrackerToDb(idRunTracker: Int)
}