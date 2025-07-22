package com.example.runningtracker.domain.usecases.running_tracker

import com.example.runningtracker.domain.repository.running_tracker.RunningTrackerBackgroundRepository
import javax.inject.Inject

class ChangeIsBackgroundWorkUseCase @Inject constructor(
    private val repository: RunningTrackerBackgroundRepository
) {
    suspend operator fun invoke(value: Boolean) {
        repository.changeIsBackgroundWork(value = value)
    }
}