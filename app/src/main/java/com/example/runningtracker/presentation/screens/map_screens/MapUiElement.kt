package com.example.runningtracker.presentation.screens.map_screens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.LifecycleOwner
import com.example.runningtracker.R
import com.example.runningtracker.presentation.screens.map_screens.state.MapScreenState
import com.example.runningtracker.presentation.screens.map_screens.view_models.MapScreenViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import ru.sulgik.mapkit.compose.Placemark
import ru.sulgik.mapkit.compose.YandexMap
import ru.sulgik.mapkit.compose.bindToLifecycleOwner
import ru.sulgik.mapkit.compose.rememberAndInitializeMapKit
import ru.sulgik.mapkit.compose.rememberCameraPositionState
import ru.sulgik.mapkit.compose.rememberPlacemarkState
import ru.sulgik.mapkit.geometry.Point
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
    val millisMinUpdateInterval = 1_000L
    val intervalMillis = 5_000L

    rememberAndInitializeMapKit().bindToLifecycleOwner()

    val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            viewModel.changeUserLocation(
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
                    viewModel.changeUserLocation(
                        userLocationLatitude = location.latitude,
                        userLocationLongitude = location.longitude
                    )
                }
            }
        }
    }

    DisposableEffect(
        lifecycleOwner,
        permissionsState.allPermissionsGranted,
        true
    ) {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, intervalMillis
        )
            .setMinUpdateIntervalMillis(millisMinUpdateInterval)
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


    val cameraPositionState = rememberCameraPositionState()
    val placeMarkState = rememberPlacemarkState(
        Point(state.userLocationLatitude, state.userLocationLongitude)
    )

    LaunchedEffect(
        state.userLocationLatitude,
        state.userLocationLongitude
    ) {
        cameraPositionState.position = CameraPosition(
            Point(state.userLocationLatitude, state.userLocationLongitude),
            state.zoomMap,
            state.azimuthMap,
            state.tiltMap
        )
        placeMarkState.geometry =
            Point(state.userLocationLatitude, state.userLocationLongitude)
    }

    YandexMap(
        cameraPositionState = cameraPositionState,
        modifier = Modifier.fillMaxSize()
    ) {
        Placemark(
            state = placeMarkState,
            icon = ImageProvider.fromResource(
                context,
                R.drawable.img_place_from
            ),
        )
    }
}