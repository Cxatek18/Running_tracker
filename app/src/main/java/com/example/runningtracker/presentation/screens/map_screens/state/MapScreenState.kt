package com.example.runningtracker.presentation.screens.map_screens.state

import com.example.runningtracker.domain.module.running_tracker.PointTrack

sealed interface MapScreenState {

    data object Initial : MapScreenState

    data class Success(
        //Params settings
        val isGeoGrant: Boolean = false,
        val isLoadingMap: Boolean = false,

        //Params map
        val isChangeCameraPositionMap: Boolean = false,
        val userLocationLatitude: Double = 0.0,
        val userLocationLongitude: Double = 0.0,
        val zoom: Float = 17.0f,
        val azimuthMap: Float = 0.0f,
        val tiltMap: Float = 0.0f,

        // Params run tracker
        val wayTrackState: List<PointTrack> = listOf(),
        val timeTrack: String = "00:00:00",
        val distanceTrack: String = "0.00"
    ) : MapScreenState
}