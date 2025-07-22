package com.example.runningtracker.presentation.screens.tracks_history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.runningtracker.core.ui.components.tracks_histor_screen.LoadingElement
import com.example.runningtracker.core.ui.components.tracks_histor_screen.NoElementInHistory
import com.example.runningtracker.core.ui.components.tracks_histor_screen.RunTrackInHistory
import com.example.runningtracker.core.ui.theme.padding_12
import com.example.runningtracker.core.ui.theme.padding_24
import com.example.runningtracker.presentation.screens.tracks_history.state.TracksHistoryScreenState
import com.example.runningtracker.presentation.screens.tracks_history.view_models.TracksHistoryScreenViewModel
import com.example.runningtracker.presentation.utils.timeIntegerToTimeHHMMSS

@Composable
fun TracksHistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: TracksHistoryScreenViewModel,
    state: TracksHistoryScreenState,
    onClickNavigateToTrackDetail: (Int) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getListRunTrackerFromDb()
    }

    when (state) {
        TracksHistoryScreenState.Initial -> {
            LoadingElement(
                modifier = modifier
            )
        }

        is TracksHistoryScreenState.Success -> {
            if (state.tracksHistory.isEmpty()) {
                NoElementInHistory(
                    modifier = modifier
                )
            }
            LazyColumn(
                modifier = modifier
                    .padding(top = padding_24)
                    .padding(horizontal = padding_12),
                verticalArrangement = Arrangement.spacedBy(space = padding_12)
            ) {
                items(state.tracksHistory) {
                    RunTrackInHistory(
                        modifier = Modifier.clickable {
                            onClickNavigateToTrackDetail(it.idRunTrackerInDb)
                        },
                        timeTrack = timeIntegerToTimeHHMMSS(it.timeTrack),
                        distanceTrack = it.distanceTrack,
                        dateRunTrack = it.dateRunTrack,
                        onCLickDeleteTrack = {
                            viewModel.deleteRunTrackerToDb(it.idRunTrackerInDb)
                        }
                    )
                }
            }
        }
    }
}