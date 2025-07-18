package com.example.runningtracker.data.repository.running_tracker_detail.locale_db

import com.example.runningtracker.data.locale.database.running_tracker.RunningTrackerDatabase
import com.example.runningtracker.data.locale.mapper.running_tracker.toRunTrackerModule
import com.example.runningtracker.domain.module.running_tracker.RunTrackerModule
import com.example.runningtracker.domain.repository.running_tracker_detail.locale_db.RunningTrackerDetailDbRepository
import javax.inject.Inject

class RunningTrackerDetailDbRepositoryImpl @Inject constructor(
    private val runningTrackerDatabase: RunningTrackerDatabase
) : RunningTrackerDetailDbRepository {

    override suspend fun getRunTrackerFromDb(idRunTracker: Int): RunTrackerModule? {
        return runningTrackerDatabase.runningTrackerDao().getRunTracker(
            idRunTracker = idRunTracker
        )?.toRunTrackerModule()
    }
}