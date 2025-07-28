package com.example.runningtracker.detail.presentation.state

import com.example.runningtracker.runtracker.domain.module.RunTrackerModule

sealed interface TrackDetailScreenState {

    data object Initial : TrackDetailScreenState

    data class Success(
        val runTrackerModule: RunTrackerModule? = null
    ) : TrackDetailScreenState
}