package com.example.runningtracker.detail.domain.usecases

import com.example.runningtracker.detail.domain.repository.RunningTrackerDetailDbRepository
import com.example.runningtracker.runtracker.domain.module.RunTrackerModule
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