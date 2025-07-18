package com.example.runningtracker.presentation.screens.tracks_history.navigation

import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.runningtracker.presentation.screens.tracks_history.TracksHistoryScreen
import com.example.runningtracker.presentation.screens.tracks_history.state.TracksHistoryScreenState
import com.example.runningtracker.presentation.screens.tracks_history.view_models.TracksHistoryScreenViewModel
import kotlinx.serialization.Serializable


@Serializable
object TracksHistoryScreenDestination

fun NavHostController.navigateToTracksHistoryScreen() {
    navigate(TracksHistoryScreenDestination)
}

fun NavGraphBuilder.tracksHistoryScreen(
    modifier: Modifier = Modifier,
    onClickNavigateToTrackDetail: (Int) -> Unit
) {
    composable<TracksHistoryScreenDestination> {
        val viewModel: TracksHistoryScreenViewModel = hiltViewModel()
        val state: TracksHistoryScreenState by viewModel.state.collectAsStateWithLifecycle()
        TracksHistoryScreen(
            modifier = modifier,
            viewModel = viewModel,
            state = state,
            onClickNavigateToTrackDetail = {
                onClickNavigateToTrackDetail(it)
            }
        )
    }
}