package com.example.runningtracker.presentation.screens.map_screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.runningtracker.R
import com.example.runningtracker.core.ui.theme.fz_18
import com.example.runningtracker.core.ui.theme.padding_20
import com.example.runningtracker.core.ui.theme.padding_24
import com.example.runningtracker.core.ui.widgets.map_screens.LocationLoadingBar
import com.example.runningtracker.presentation.screens.map_screens.state.MapScreenState
import com.example.runningtracker.presentation.screens.map_screens.view_models.MapScreenViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.common.util.CollectionUtils.listOf

@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    state: MapScreenState,
    viewModel: MapScreenViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val zeroDouble = 0.0

    val locationPermissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    val permissionsState: MultiplePermissionsState =
        rememberMultiplePermissionsState(locationPermissions)

    LaunchedEffect(Unit) {
        viewModel.setSuccessState()
        if (!permissionsState.allPermissionsGranted) {
            permissionsState.launchMultiplePermissionRequest()
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.changeIsGeoGrant()
                permissionsState.launchMultiplePermissionRequest()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    when (state) {
        MapScreenState.Initial -> {}

        is MapScreenState.Success -> {
            Box(modifier = modifier.fillMaxSize()) {
                when {
                    permissionsState.allPermissionsGranted -> {
                        val locationManager =
                            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        val isGpsEnabled =
                            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                                LocationManager.NETWORK_PROVIDER
                            )
                        if (isGpsEnabled) {
                            MapUiElement(
                                context = context,
                                viewModel = viewModel,
                                state = state,
                                lifecycleOwner = lifecycleOwner,
                                permissionsState = permissionsState
                            )
                            if (
                                state.userLocationLatitude == zeroDouble && state.userLocationLongitude == zeroDouble
                            ) {
                                LocationLoadingBar()
                            }
                        } else {
                            EnableGeoLocation(
                                context = context
                            )
                        }
                    }

                    permissionsState.shouldShowRationale -> {
                        NeededLocationPermissions(
                            multiplePermissionsState = permissionsState
                        )
                    }

                    !permissionsState.allPermissionsGranted && !permissionsState.shouldShowRationale -> {
                        PermissionOnToSettings(
                            context = context
                        )
                    }

                    else -> {
                        Column(
                            modifier = Modifier
                                .padding(top = padding_24)
                                .fillMaxSize()
                                .padding(horizontal = padding_20),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.text_requesting_permissions),
                                color = Color.Blue,
                                fontSize = fz_18,
                                fontFamily = FontFamily.Monospace,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}