package com.example.runningtracker.presentation.screens.map_screens.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningtracker.presentation.screens.map_screens.state.MapScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapScreenViewModel @Inject constructor() : ViewModel() {

    private val _state: MutableStateFlow<MapScreenState> = MutableStateFlow<MapScreenState>(
        MapScreenState.Initial
    )
    val state: StateFlow<MapScreenState> = _state.asStateFlow()

    fun setSuccessState() {
        viewModelScope.launch {
            when (_state.value) {
                MapScreenState.Initial -> {
                    _state.value = MapScreenState.Success()
                }

                is MapScreenState.Success -> {}
            }
        }
    }

    fun changeIsGeoGrant() {
        viewModelScope.launch {
            when (_state.value) {
                MapScreenState.Initial -> {}
                is MapScreenState.Success -> {
                    val currentState =
                        _state.value as MapScreenState.Success
                    val updateState = currentState.copy(
                        isGeoGrant = !currentState.isGeoGrant
                    )
                    _state.value = updateState
                }
            }
        }
    }

    fun changeUserLocation(
        userLocationLatitude: Double,
        userLocationLongitude: Double
    ) {
        viewModelScope.launch {
            when (_state.value) {
                MapScreenState.Initial -> {}
                is MapScreenState.Success -> {
                    val currentState =
                        _state.value as MapScreenState.Success
                    val updateState = currentState.copy(
                        userLocationLatitude = userLocationLatitude,
                        userLocationLongitude = userLocationLongitude
                    )
                    _state.value = updateState
                }
            }
        }
    }
}