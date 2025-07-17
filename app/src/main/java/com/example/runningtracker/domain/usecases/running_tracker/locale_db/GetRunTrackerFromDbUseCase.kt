package com.example.runningtracker.domain.usecases.running_tracker.locale_db

import com.example.runningtracker.domain.module.running_tracker.RunTrackerModule
import com.example.runningtracker.domain.repository.running_tracker.locale_db.RunningTrackerDbRepository
import javax.inject.Inject

class GetRunTrackerFromDbUseCase @Inject constructor(
    private val repository: RunningTrackerDbRepository
) {
    suspend operator fun invoke(idRunTracker: Int): RunTrackerModule? {
        return repository.getRunTrackerFromDb(
            idRunTracker = idRunTracker
        )
    }
}