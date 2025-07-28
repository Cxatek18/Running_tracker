package com.example.runningtracker.history.presentation.state

import com.example.runningtracker.runtracker.domain.module.RunTrackerModule

sealed interface TracksHistoryScreenState {

    data object Initial : TracksHistoryScreenState

    data class Success(
        val tracksHistory: List<RunTrackerModule> = listOf()
    ) : TracksHistoryScreenState
}