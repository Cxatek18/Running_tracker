package com.example.runningtracker.domain.usecases.running_tracker.locale_db

import com.example.runningtracker.domain.module.running_tracker.RunTrackerModule
import com.example.runningtracker.domain.repository.running_tracker.locale_db.RunningTrackerDbRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListRunTrackerFromDbUseCase @Inject constructor(
    private val repository: RunningTrackerDbRepository
) {

    operator fun invoke(): Flow<List<RunTrackerModule>> {
        return repository.getListRunTrackerFromDb()
    }
}