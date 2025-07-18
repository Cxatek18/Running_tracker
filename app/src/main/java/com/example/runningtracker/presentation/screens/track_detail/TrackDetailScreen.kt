package com.example.runningtracker.presentation.screens.track_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.runningtracker.core.ui.components.map_screen.RunningTrackWindow
import com.example.runningtracker.core.ui.components.map_ui_element.MapYandexElement
import com.example.runningtracker.core.ui.components.tracks_histor_screen.LoadingElement
import com.example.runningtracker.presentation.screens.track_detail.state.TrackDetailScreenState
import com.example.runningtracker.presentation.screens.track_detail.view_models.TrackDetailScreenViewModel
import com.example.runningtracker.presentation.utils.timeIntegerToTimeHHMMSS
import ru.sulgik.mapkit.compose.bindToLifecycleOwner
import ru.sulgik.mapkit.compose.rememberAndInitializeMapKit
import ru.sulgik.mapkit.compose.rememberCameraPositionState
import ru.sulgik.mapkit.compose.rememberPlacemarkState
import ru.sulgik.mapkit.geometry.Point
import ru.sulgik.mapkit.map.CameraPosition


@Composable
fun TrackDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: TrackDetailScreenViewModel,
    state: TrackDetailScreenState,
    idRunTrack: Int
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getRunTrackerFromDb(idRunTrack)
    }

    when (state) {
        TrackDetailScreenState.Initial -> {
            LoadingElement(
                modifier = modifier
            )
        }

        is TrackDetailScreenState.Success -> {
            Box(
                modifier = modifier.fillMaxSize()
            ) {
                val cameraPositionState = rememberCameraPositionState()
                val placeMarkState = rememberPlacemarkState(
                    Point(
                        latitude = state.runTrackerModule?.let {
                            it.wayTrack[0].startTrackLatitude
                        } ?: 0.0,
                        longitude = state.runTrackerModule?.let {
                            it.wayTrack[0].startTrackLongitude
                        } ?: 0.0
                    )
                )

                rememberAndInitializeMapKit().bindToLifecycleOwner()

                cameraPositionState.position = CameraPosition(
                    target = Point(
                        latitude = state.runTrackerModule?.let {
                            it.wayTrack[0].startTrackLatitude
                        } ?: 0.0,
                        longitude = state.runTrackerModule?.let {
                            it.wayTrack[0].startTrackLongitude
                        } ?: 0.0
                    ),
                    zoom = 17.0f,
                    azimuth = 0.0f,
                    tilt = 0.0f
                )

                placeMarkState.geometry =
                    Point(
                        latitude = state.runTrackerModule?.let {
                            it.wayTrack[0].startTrackLatitude
                        } ?: 0.0,
                        longitude = state.runTrackerModule?.let {
                            it.wayTrack[0].startTrackLongitude
                        } ?: 0.0
                    )

                MapYandexElement(
                    modifier = modifier,
                    context = context,
                    cameraPositionState = cameraPositionState,
                    placeMarkState = placeMarkState,
                    wayTrackState = state.runTrackerModule?.wayTrack ?: emptyList()
                )


                RunningTrackWindow(
                    timeTrack = timeIntegerToTimeHHMMSS(
                        timeIsInt = state.runTrackerModule?.timeTrack ?: 0
                    ),
                    distanceTrack = state.runTrackerModule?.distanceTrack ?: "0.00"
                )
            }
        }
    }
}