package com.example.runningtracker.presentation.screens.track_detail.state

import com.example.runningtracker.domain.module.running_tracker.RunTrackerModule

sealed interface TrackDetailScreenState {

    data object Initial : TrackDetailScreenState

    data class Success(
        val runTrackerModule: RunTrackerModule? = null
    ) : TrackDetailScreenState
}