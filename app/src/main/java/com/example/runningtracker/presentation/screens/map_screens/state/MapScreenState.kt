package com.example.runningtracker.presentation.screens.map_screens.state

sealed interface MapScreenState {

    data object Initial : MapScreenState

    data class Success(
        val isGeoGrant: Boolean = false,
        val userLocationLatitude: Double = 0.0,
        val userLocationLongitude: Double = 0.0,
        val zoomMap: Float = 17.0f,
        val azimuthMap: Float = 0.0f,
        val tiltMap: Float = 0.0f
    ) : MapScreenState
}