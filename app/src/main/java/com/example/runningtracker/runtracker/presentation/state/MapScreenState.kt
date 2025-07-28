package com.example.runningtracker.runtracker.presentation.state

import com.example.runningtracker.runtracker.domain.module.RunTrackerModule

sealed interface MapScreenState {

    data object Initial : MapScreenState

    data class Success(
        //Params settings
        val isGeoGrant: Boolean = false,
        val isLoadingMap: Boolean = false,

        //Params map
        val isChangeCameraPositionMap: Boolean = false,
        val zoom: Float = 17.0f,
        val azimuthMap: Float = 0.0f,
        val tiltMap: Float = 0.0f,

        val runTrackerModule: RunTrackerModule? = null,
        val isTrackOn: Boolean = false
    ) : MapScreenState
}