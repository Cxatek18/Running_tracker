package com.example.runningtracker.history.domain.usecases

import com.example.runningtracker.history.domain.repository.RunningTrackerHistoryDbRepository
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