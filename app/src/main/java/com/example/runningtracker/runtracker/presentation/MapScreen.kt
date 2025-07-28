package com.example.runningtracker.runtracker.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.runningtracker.R
import com.example.runningtracker.runtracker.presentation.state.MapScreenState
import com.example.runningtracker.runtracker.presentation.view_models.MapScreenViewModel
import com.example.runningtracker.runtracker.services.RunningTrackerService
import com.example.runningtracker.runtracker.ui.components.map_screen.AllowNotifications
import com.example.runningtracker.runtracker.ui.components.map_screen.EnableGeoLocation
import com.example.runningtracker.runtracker.ui.components.map_screen.NeededLocationPermissions
import com.example.runningtracker.runtracker.ui.components.map_screen.NeededNotificationPermission
import com.example.runningtracker.runtracker.ui.components.map_screen.PermissionOnToSettings
import com.example.runningtracker.runtracker.ui.components.map_screen.RequestingPermissions
import com.example.runningtracker.runtracker.ui.components.map_screen.RunningTrackWindow
import com.example.runningtracker.runtracker.ui.widgets.map_screens.LocationLoadingBar
import com.example.runningtracker.theme.fz_16
import com.example.runningtracker.theme.padding_20
import com.example.runningtracker.utils.timeIntegerToTimeHHMMSS
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.common.util.CollectionUtils.listOf

@SuppressLint("MissingPermission", "InlinedApi")
@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    state: MapScreenState,
    userLocation: Location?,
    viewModel: MapScreenViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val locationPermissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    val permissionsState: MultiplePermissionsState =
        rememberMultiplePermissionsState(locationPermissions)

    val notificationPermission = rememberPermissionState(
        Manifest.permission.POST_NOTIFICATIONS
    )

    LaunchedEffect(Unit) {
        viewModel.setSuccessState()
        viewModel.startLocationUpdates()
        viewModel.observeRunTracker()
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
                            when (val status = notificationPermission.status) {
                                is PermissionStatus.Granted -> {

                                    if (userLocation == null) {
                                        LocationLoadingBar()
                                        viewModel.changeIsChangingCameraPosition(value = true)
                                    } else {
                                        MapUiElement(
                                            context = context,
                                            state = state,
                                            userLocation = userLocation
                                        )

                                        viewModel.changeIsChangingCameraPosition(value = false)
                                        val textInBtnOnTrack: String
                                        val isRunTrack: Boolean
                                        if (state.runTrackerModule != null) {
                                            textInBtnOnTrack =
                                                stringResource(R.string.text_btn_stop)
                                            isRunTrack = true
                                        } else {
                                            textInBtnOnTrack =
                                                stringResource(R.string.text_btn_start)
                                            isRunTrack = false
                                        }

                                        AnimatedVisibility(
                                            visible = isRunTrack,
                                            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                                            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                                        ) {
                                            RunningTrackWindow(
                                                timeTrack = timeIntegerToTimeHHMMSS(
                                                    state.runTrackerModule?.timeTrack ?: 0
                                                ),
                                                distanceTrack = state.runTrackerModule
                                                    ?.distanceTrack ?: "0,00 m"
                                            )
                                        }

                                        Button(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = padding_20)
                                                .align(Alignment.BottomCenter),
                                            onClick = {
                                                val serviceIntent = Intent(
                                                    context,
                                                    RunningTrackerService::class.java
                                                )
                                                if (!isRunTrack) {
                                                    ContextCompat.startForegroundService(
                                                        context,
                                                        serviceIntent
                                                    )
                                                    viewModel.stopLocationUpdates()
                                                } else {
                                                    context.stopService(serviceIntent)
                                                    viewModel.removeTrackIdInSharedPreferences(
                                                        "CURRENT_TRACK"
                                                    )
                                                    viewModel.stopRunTrack()
                                                    viewModel.startLocationUpdates()
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
                                }

                                is PermissionStatus.Denied -> {
                                    if (status.shouldShowRationale) {
                                        AllowNotifications(
                                            notificationPermission = permissionsState as
                                                    PermissionState
                                        )
                                    } else {
                                        NeededNotificationPermission(
                                            context = context
                                        )
                                    }
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