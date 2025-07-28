package com.example.runningtracker.runtracker.domain.usecases

import com.example.runningtracker.runtracker.domain.repository.RunningTrackerDbRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTrackIdInSharedPreferencesUseCase @Inject constructor(
    private val repository: RunningTrackerDbRepository
) {
    suspend operator fun invoke(
        key: String
    ): Flow<Int?> {
        return repository.getTrackIdInSharedPreferences(
            key = key
        )
    }
}