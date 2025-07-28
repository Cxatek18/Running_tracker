package com.example.runningtracker.runtracker.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.runningtracker.runtracker.presentation.state.MapScreenState
import com.example.runningtracker.runtracker.ui.components.map_ui_element.MapYandexElement
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import ru.sulgik.mapkit.compose.bindToLifecycleOwner
import ru.sulgik.mapkit.compose.rememberAndInitializeMapKit
import ru.sulgik.mapkit.compose.rememberCameraPositionState
import ru.sulgik.mapkit.compose.rememberPlacemarkState
import ru.sulgik.mapkit.geometry.Point
import ru.sulgik.mapkit.map.CameraPosition

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun MapUiElement(
    modifier: Modifier = Modifier,
    context: Context,
    state: MapScreenState.Success,
    userLocation: Location?
) {
    val cameraPositionState = rememberCameraPositionState()
    val placeMarkState = rememberPlacemarkState(
        Point(
            latitude = userLocation?.latitude ?: 0.0,
            longitude = userLocation?.longitude ?: 0.0
        )
    )

    rememberAndInitializeMapKit().bindToLifecycleOwner()

    if (state.isChangeCameraPositionMap) {
        cameraPositionState.position = CameraPosition(
            target = Point(
                latitude = userLocation?.latitude ?: 0.0,
                longitude = userLocation?.longitude ?: 0.0
            ),
            zoom = state.zoom,
            azimuth = state.azimuthMap,
            tilt = state.tiltMap
        )
    }

    if (state.runTrackerModule != null) {
        if (state.runTrackerModule.wayTrack.isNotEmpty()) {
            placeMarkState.geometry =
                Point(
                    state.runTrackerModule.wayTrack.last().startTrackLatitude,
                    state.runTrackerModule.wayTrack.last().startTrackLongitude
                )
        } else {
            placeMarkState.geometry =
                Point(
                    userLocation?.latitude ?: 0.0,
                    userLocation?.longitude ?: 0.0
                )
        }
    } else {
        placeMarkState.geometry =
            Point(
                userLocation?.latitude ?: 0.0,
                userLocation?.longitude ?: 0.0
            )
    }


    MapYandexElement(
        modifier = modifier,
        context = context,
        cameraPositionState = cameraPositionState,
        placeMarkState = placeMarkState,
        wayTrackState = state.runTrackerModule?.wayTrack ?: listOf()
    )
}