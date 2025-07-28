package com.example.runningtracker.history.domain.usecases

import com.example.runningtracker.history.domain.repository.RunningTrackerHistoryDbRepository
import com.example.runningtracker.runtracker.domain.module.RunTrackerModule
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListRunTrackerFromDbUseCase @Inject constructor(
    private val repository: RunningTrackerHistoryDbRepository
) {

    operator fun invoke(): Flow<List<RunTrackerModule>> {
        return repository.getListRunTrackerFromDb()
    }
}