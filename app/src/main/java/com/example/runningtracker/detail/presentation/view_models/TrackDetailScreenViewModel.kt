package com.example.runningtracker.detail.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningtracker.detail.domain.usecases.GetRunTrackerFromDbUseCase
import com.example.runningtracker.detail.presentation.state.TrackDetailScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackDetailScreenViewModel @Inject constructor(
    private val getRunTrackerFromDbUseCase: GetRunTrackerFromDbUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<TrackDetailScreenState>(
        TrackDetailScreenState.Initial
    )
    val state: StateFlow<TrackDetailScreenState> = _state.asStateFlow()

    fun getRunTrackerFromDb(idRunTracker: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val runTrackerModule = getRunTrackerFromDbUseCase(
                idRunTracker = idRunTracker
            )
            val updateState = TrackDetailScreenState.Success(
                runTrackerModule = runTrackerModule
            )
            _state.value = updateState
        }
    }
}