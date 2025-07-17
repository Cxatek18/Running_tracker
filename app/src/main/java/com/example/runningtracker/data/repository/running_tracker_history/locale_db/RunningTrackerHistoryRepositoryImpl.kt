package com.example.runningtracker.data.repository.running_tracker_history.locale_db

import com.example.runningtracker.data.locale.database.running_tracker.RunningTrackerDatabase
import com.example.runningtracker.data.locale.mapper.running_tracker.toListRunTrackerModule
import com.example.runningtracker.domain.module.running_tracker.RunTrackerModule
import com.example.runningtracker.domain.repository.running_tracker_history.locale_db.RunningTrackerHistoryDbRepository
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
                .toListRunTrackerModule()
        )
    }

    override suspend fun deleteRunTrackerToDb(idRunTracker: Int) {
        runningTrackerDatabase.runningTrackerDao().deleteRunTracker(idRunTracker)
    }
}