package com.example.runningtracker.domain.usecases.running_tracker_history.locale_db

import com.example.runningtracker.domain.repository.running_tracker_history.locale_db.RunningTrackerHistoryDbRepository
import javax.inject.Inject

class DeleteRunTrackerToDbUseCase @Inject constructor(
    private val repository: RunningTrackerHistoryDbRepository
) {
    suspend operator fun invoke(idRunTracker: Int) {
        repository.deleteRunTrackerToDb(
            idRunTracker = idRunTracker
        )
    }
}