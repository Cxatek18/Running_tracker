package com.example.runningtracker.presentation.screens.tracks_history.state

import com.example.runningtracker.domain.module.running_tracker.RunTrackerModule

sealed interface TracksHistoryScreenState {

    data object Initial : TracksHistoryScreenState

    data class Success(
        val tracksHistory: List<RunTrackerModule> = listOf()
    ) : TracksHistoryScreenState
}