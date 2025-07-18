package com.example.runningtracker.domain.repository.running_tracker_detail.locale_db

import com.example.runningtracker.domain.module.running_tracker.RunTrackerModule

interface RunningTrackerDetailDbRepository {

    suspend fun getRunTrackerFromDb(idRunTracker: Int): RunTrackerModule?
}