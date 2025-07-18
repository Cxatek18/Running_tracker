package com.example.runningtracker.domain.usecases.running_tracker_detail.locale_db

import com.example.runningtracker.domain.module.running_tracker.RunTrackerModule
import com.example.runningtracker.domain.repository.running_tracker_detail.locale_db.RunningTrackerDetailDbRepository
import javax.inject.Inject

class GetRunTrackerFromDbUseCase @Inject constructor(
    private val repository: RunningTrackerDetailDbRepository
) {
    suspend operator fun invoke(idRunTracker: Int): RunTrackerModule? {
        return repository.getRunTrackerFromDb(
            idRunTracker = idRunTracker
        )
    }
}