package com.example.runningtracker.presentation.screens.tracks_history.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningtracker.domain.usecases.running_tracker_history.locale_db.DeleteRunTrackerToDbUseCase
import com.example.runningtracker.domain.usecases.running_tracker_history.locale_db.GetListRunTrackerFromDbUseCase
import com.example.runningtracker.presentation.screens.tracks_history.state.TracksHistoryScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TracksHistoryScreenViewModel @Inject constructor(
    private val getListRunTrackerFromDbUseCase: GetListRunTrackerFromDbUseCase,
    private val deleteRunTrackerToDbUseCase: DeleteRunTrackerToDbUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<TracksHistoryScreenState>(
        TracksHistoryScreenState.Initial
    )
    val state: StateFlow<TracksHistoryScreenState> = _state.asStateFlow()

    fun getListRunTrackerFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            getListRunTrackerFromDbUseCase()
                .catch {
                    Log.d("DEBUG", "$it")
                }
                .collect { historyTrack ->
                    val updateState = TracksHistoryScreenState.Success(
                        tracksHistory = historyTrack
                    )
                    _state.value = updateState
                }
        }
    }

    fun deleteRunTrackerToDb(idRunTracker: Int) {
        viewModelScope.launch {
            deleteRunTrackerToDbUseCase(
                idRunTracker = idRunTracker
            )
            getListRunTrackerFromDb()
        }
    }
}