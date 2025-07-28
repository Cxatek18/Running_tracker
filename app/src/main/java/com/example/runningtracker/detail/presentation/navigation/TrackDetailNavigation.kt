package com.example.runningtracker.detail.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.runningtracker.detail.presentation.TrackDetailScreen
import com.example.runningtracker.detail.presentation.state.TrackDetailScreenState
import com.example.runningtracker.detail.presentation.view_models.TrackDetailScreenViewModel
import kotlinx.serialization.Serializable

@Serializable
data class TrackDetailDestination(
    val trackId: Int
)

fun NavHostController.navigateToTrackDetail(trackId: Int) {
    navigate(TrackDetailDestination(trackId = trackId))
}

fun NavGraphBuilder.trackDetailScreen(
    modifier: Modifier = Modifier
) {
    composable<TrackDetailDestination> { backStackEntry ->
        val args = backStackEntry.toRoute<TrackDetailDestination>()
        val viewModel: TrackDetailScreenViewModel = hiltViewModel()
        val state: TrackDetailScreenState by viewModel.state.collectAsStateWithLifecycle()
        TrackDetailScreen(
            modifier = modifier,
            viewModel = viewModel,
            state = state,
            idRunTrack = args.trackId
        )
    }
}