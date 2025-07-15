package com.example.runningtracker.presentation.screens.map_screens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LifecycleOwner
import com.example.runningtracker.R
import com.example.runningtracker.presentation.screens.map_screens.state.MapScreenState
import com.example.runningtracker.presentation.screens.map_screens.utils.MapIntervals
import com.example.runningtracker.presentation.screens.map_screens.view_models.MapScreenViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import ru.sulgik.mapkit.compose.Placemark
import ru.sulgik.mapkit.compose.Polyline
import ru.sulgik.mapkit.compose.YandexMap
import ru.sulgik.mapkit.compose.bindToLifecycleOwner
import ru.sulgik.mapkit.compose.rememberAndInitializeMapKit
import ru.sulgik.mapkit.compose.rememberCameraPositionState
import ru.sulgik.mapkit.compose.rememberPlacemarkState
import ru.sulgik.mapkit.compose.rememberPolylineState
import ru.sulgik.mapkit.geometry.Point
import ru.sulgik.mapkit.geometry.Polyline
import ru.sulgik.mapkit.map.CameraPosition
import ru.sulgik.mapkit.map.ImageProvider
import ru.sulgik.mapkit.map.fromResource

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun MapUiElement(
    modifier: Modifier = Modifier,
    context: Context,
    viewModel: MapScreenViewModel,
    state: MapScreenState.Success,
    lifecycleOwner: LifecycleOwner,
    permissionsState: MultiplePermissionsState,
) {
    val cameraPositionState = rememberCameraPositionState()
    val placeMarkState = rememberPlacemarkState(
        Point(
            latitude = state.userLocationLatitude,
            longitude = state.userLocationLongitude
        )
    )

    rememberAndInitializeMapKit().bindToLifecycleOwner()

    val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            viewModel.updateUserLocation(
                userLocationLatitude = location.latitude,
                userLocationLongitude = location.longitude
            )
        }
    }

    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                if (location != null) {
                    viewModel.updateUserLocation(
                        userLocationLatitude = location.latitude,
                        userLocationLongitude = location.longitude
                    )
                    viewModel.updateWayTrack(
                        pointLocationLatitude = location.latitude,
                        pointLocationLongitude = location.longitude
                    )
                    viewModel.updateDistanceTrack()
                }
            }
        }
    }

    DisposableEffect(
        key1 = lifecycleOwner,
        key2 = permissionsState.allPermissionsGranted,
        key3 = true
    ) {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            MapIntervals.INTERVAL_MILLIS.interval
        )
            .setMinUpdateIntervalMillis(
                MapIntervals.MILLIS_MIN_UPDATE_INTERVAL.interval
            )
            .build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    if (
        state.userLocationLatitude != MapScreenViewModel.ZERO_DOUBLE &&
        state.userLocationLongitude != MapScreenViewModel.ZERO_DOUBLE
    ) {
        viewModel.changeIsChangingCameraPosition(true)
        viewModel.changeIsLoadingMap(true)
    } else {
        viewModel.changeIsChangingCameraPosition(false)
        viewModel.changeIsLoadingMap(false)
    }

    if (!state.isChangeCameraPositionMap) {
        cameraPositionState.position = CameraPosition(
            Point(
                state.userLocationLatitude,
                state.userLocationLongitude
            ),
            state.zoom,
            state.azimuthMap,
            state.tiltMap
        )
    }

    placeMarkState.geometry =
        Point(
            state.userLocationLatitude,
            state.userLocationLongitude
        )

    state.wayTrackState

    YandexMap(
        cameraPositionState = cameraPositionState,
        modifier = modifier.fillMaxSize()
    ) {
        Placemark(
            state = placeMarkState,
            icon = ImageProvider.fromResource(
                context,
                R.drawable.img_place_from
            ),
        )

        val currentPolyline = remember(state.wayTrackState) {
            if (state.wayTrackState.size > 1) {
                Polyline(state.wayTrackState.map {
                    Point(it.startTrackLatitude, it.startTrackLongitude)
                })
            } else null
        }

        currentPolyline?.let { polyline ->
            key(polyline) {
                Polyline(
                    state = rememberPolylineState(geometry = polyline),
                    strokeColor = Color.Green,
                    strokeWidth = 3f
                )
            }
        }
    }
}