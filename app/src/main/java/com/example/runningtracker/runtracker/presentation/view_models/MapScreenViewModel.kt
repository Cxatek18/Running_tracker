package com.example.runningtracker.runtracker.presentation.view_models

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningtracker.runtracker.domain.usecases.ObserveRunTrackerUseCase
import com.example.runningtracker.runtracker.domain.usecases.RemoveTrackIdInSharedPreferencesUseCase
import com.example.runningtracker.runtracker.presentation.state.MapScreenState
import com.example.runningtracker.runtracker.presentation.utils.intFlow
import com.example.runningtracker.runtracker.presentation.utils.locationUpdatesFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapScreenViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val observeRunTrackerUseCase: ObserveRunTrackerUseCase,
    private val sharedPreferences: SharedPreferences,
    private val removeTrackIdInSharedPreferencesUseCase: RemoveTrackIdInSharedPreferencesUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<MapScreenState> = MutableStateFlow<MapScreenState>(
        MapScreenState.Initial
    )
    val state: StateFlow<MapScreenState> = _state.asStateFlow()

    private val _userLocation = MutableStateFlow<Location?>(null)
    val userLocation: StateFlow<Location?> = _userLocation.asStateFlow()

    private val currentTrackIdFlow = sharedPreferences.intFlow(
        KEY_CURRENT_TRACK_ID, DEFAULT_ID_RUN_TRACK_IN_SHARED_PREFERENCES
    )

    private var locationJob: Job? = null
    private var observeJob: Job? = null

    //region MapScreen
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
                    val locationManager =
                        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val isGpsEnabled =
                        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                            LocationManager.NETWORK_PROVIDER
                        )
                    val currentState =
                        _state.value as MapScreenState.Success
                    val updateState = currentState.copy(
                        isGeoGrant = isGpsEnabled
                    )
                    _state.value = updateState
                }
            }
        }
    }
    //endregion

    //region Run tracker
    fun changeIsChangingCameraPosition(
        value: Boolean
    ) {
        when (_state.value) {
            MapScreenState.Initial -> {}
            is MapScreenState.Success -> {
                val currentState =
                    _state.value as MapScreenState.Success
                if (currentState.isChangeCameraPositionMap != value) {
                    val updateState = currentState.copy(
                        isChangeCameraPositionMap = value
                    )
                    _state.value = updateState
                }
            }
        }
    }

    fun startLocationUpdates() {
        if (locationJob?.isActive == true) return
        locationJob = viewModelScope.launch {
            locationUpdatesFlow(context = context)
                .collectLatest { location ->
                    _userLocation.value = location
                }
        }
    }

    fun stopLocationUpdates() {
        locationJob?.cancel()
    }

    fun observeRunTracker() {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            currentTrackIdFlow
                .flatMapLatest { id ->
                    if (id != DEFAULT_ID_RUN_TRACK_IN_SHARED_PREFERENCES) {
                        observeRunTrackerUseCase(id)
                            .catch { e ->
                                Log.e(TAG_DEBUG, "$TEXT_ERROR_OBSERVING_RUN_TRACK $e")
                                flowOf(null)
                            }
                    } else {
                        flowOf(null)
                    }
                }
                .collect { runTrackerModule ->
                    _state.update { currentState ->
                        if (currentState is MapScreenState.Success) {
                            currentState.copy(
                                runTrackerModule = runTrackerModule,
                                isTrackOn = (runTrackerModule != null)
                            )
                        } else currentState
                    }
                }
        }
    }

    fun removeTrackIdInSharedPreferences(key: String) {
        viewModelScope.launch {
            removeTrackIdInSharedPreferencesUseCase(key)
        }
    }

    fun stopRunTrack() {
        viewModelScope.launch {
            _state.update { currentState ->
                if (currentState is MapScreenState.Success) {
                    currentState.copy(runTrackerModule = null, isTrackOn = false)
                } else currentState
            }
        }
    }
    //endregion

    companion object {
        const val RADIUS_EARTH = 6371000.0
        const val DEFAULT_ID_RUN_TRACK_IN_SHARED_PREFERENCES = -999
        const val KEY_CURRENT_TRACK_ID = "CURRENT_TRACK"

        private const val TAG_DEBUG = "DEBUG"
        private const val TEXT_ERROR_OBSERVING_RUN_TRACK = "Error observing run tracker:"
    }
}