package com.example.runningtracker.presentation.screens.map_screens

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.runningtracker.R
import com.example.runningtracker.core.ui.components.map_screen.EnableGeoLocation
import com.example.runningtracker.core.ui.components.map_screen.NeededLocationPermissions
import com.example.runningtracker.core.ui.components.map_screen.PermissionOnToSettings
import com.example.runningtracker.core.ui.components.map_screen.RequestingPermissions
import com.example.runningtracker.core.ui.components.map_screen.RunningTrackWindow
import com.example.runningtracker.core.ui.theme.fz_16
import com.example.runningtracker.core.ui.theme.padding_20
import com.example.runningtracker.core.ui.widgets.map_screens.LocationLoadingBar
import com.example.runningtracker.domain.module.running_tracker.RunTrackerModule
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
    currentRunningTracker: RunTrackerModule?,
    viewModel: MapScreenViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

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
                        if (state.isGeoGrant) {
                            MapUiElement(
                                context = context,
                                viewModel = viewModel,
                                state = state,
                                lifecycleOwner = lifecycleOwner,
                                permissionsState = permissionsState
                            )

                            if (!state.isLoadingMap) {
                                LocationLoadingBar()
                            } else {
                                val textInBtnOnTrack: String
                                val isRunTrack: Boolean
                                if (currentRunningTracker != null) {
                                    textInBtnOnTrack = stringResource(R.string.text_btn_stop)
                                    isRunTrack = false
                                } else {
                                    textInBtnOnTrack = stringResource(R.string.text_btn_start)
                                    isRunTrack = true
                                }

                                AnimatedVisibility(
                                    visible = !isRunTrack,
                                    exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                                    enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                                ) {
                                    RunningTrackWindow(state = state)
                                }

                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = padding_20)
                                        .align(Alignment.BottomCenter),
                                    onClick = {
                                        if (isRunTrack) {
                                            viewModel.updateTimeTrack()
                                            viewModel.startRunning(
                                                startPointLocationLatitude = state.userLocationLatitude,
                                                startPointLocationLongitude = state.userLocationLongitude
                                            )
                                        } else {
                                            viewModel.stopTrack()
                                        }
                                    },
                                ) {
                                    Text(
                                        text = textInBtnOnTrack,
                                        color = Color.White,
                                        fontSize = fz_16,
                                        fontFamily = FontFamily.Monospace,
                                    )
                                }
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
                        RequestingPermissions()
                    }
                }
            }
        }
    }
}