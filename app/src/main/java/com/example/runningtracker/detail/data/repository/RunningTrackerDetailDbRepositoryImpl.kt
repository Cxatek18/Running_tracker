package com.example.runningtracker.detail.data.repository

import com.example.runningtracker.detail.domain.repository.RunningTrackerDetailDbRepository
import com.example.runningtracker.runtracker.data.locale.database.RunningTrackerDatabase
import com.example.runningtracker.runtracker.data.locale.mapper.toRunTrackerModule
import com.example.runningtracker.runtracker.domain.module.RunTrackerModule
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