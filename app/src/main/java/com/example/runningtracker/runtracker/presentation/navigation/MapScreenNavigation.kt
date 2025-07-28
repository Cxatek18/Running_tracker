package com.example.runningtracker.runtracker.presentation.navigation

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.runningtracker.detail.presentation.navigation.trackDetailScreen
import com.example.runningtracker.history.presentation.navigation.tracksHistoryScreen
import com.example.runningtracker.runtracker.presentation.MapScreen
import com.example.runningtracker.runtracker.presentation.state.MapScreenState
import com.example.runningtracker.runtracker.presentation.view_models.MapScreenViewModel
import kotlinx.serialization.Serializable

@Serializable
object MainGraph

@Serializable
object MapScreenDestination

@RequiresApi(Build.VERSION_CODES.Q)
fun NavGraphBuilder.mapGraph(
    modifier: Modifier = Modifier,
    onClickNavigateToTrackDetail: (Int) -> Unit
) {
    navigation<MainGraph>(startDestination = MapScreenDestination) {
        mapScreen(
            modifier = modifier
        )
        tracksHistoryScreen(
            modifier = modifier,
            onClickNavigateToTrackDetail = {
                onClickNavigateToTrackDetail(it)
            }
        )
        trackDetailScreen(
            modifier = modifier
        )
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun NavGraphBuilder.mapScreen(
    modifier: Modifier = Modifier,
) {
    composable<MapScreenDestination> {
        val viewModel: MapScreenViewModel = hiltViewModel()
        val state: MapScreenState by viewModel.state.collectAsStateWithLifecycle()
        val userLocation: Location? by viewModel.userLocation.collectAsStateWithLifecycle()
        MapScreen(
            modifier = modifier.background(color = MaterialTheme.colorScheme.background),
            viewModel = viewModel,
            state = state,
            userLocation = userLocation
        )
    }
}