package com.example.runningtracker.history.data.repository

import com.example.runningtracker.history.domain.repository.RunningTrackerHistoryDbRepository
import com.example.runningtracker.runtracker.data.locale.database.RunningTrackerDatabase
import com.example.runningtracker.runtracker.data.locale.mapper.toListRunTrackerModule
import com.example.runningtracker.runtracker.domain.module.RunTrackerModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RunningTrackerHistoryDbRepositoryImpl @Inject constructor(
    private val runningTrackerDatabase: RunningTrackerDatabase
) : RunningTrackerHistoryDbRepository {

    override fun getListRunTrackerFromDb(): Flow<List<RunTrackerModule>> = flow {
        emit(
            runningTrackerDatabase
                .runningTrackerDao()
                .getListRunTracker()
                .sortedByDescending { it.runTracker.id }
                .toListRunTrackerModule()
        )
    }

    override suspend fun deleteRunTrackerToDb(idRunTracker: Int) {
        runningTrackerDatabase.runningTrackerDao().deleteRunTracker(idRunTracker)
    }
}