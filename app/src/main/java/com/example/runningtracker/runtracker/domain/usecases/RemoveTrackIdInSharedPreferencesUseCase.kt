package com.example.runningtracker.runtracker.domain.usecases

import com.example.runningtracker.runtracker.domain.repository.RunningTrackerDbRepository
import javax.inject.Inject

class RemoveTrackIdInSharedPreferencesUseCase @Inject constructor(
    private val repository: RunningTrackerDbRepository
) {
    suspend operator fun invoke(
        key: String
    ) {
        repository.removeTrackIdInSharedPreferences(
            key = key
        )
    }
}