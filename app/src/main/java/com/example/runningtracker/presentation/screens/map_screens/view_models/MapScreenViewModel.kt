package com.example.runningtracker.presentation.screens.map_screens.view_models

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningtracker.domain.module.running_tracker.PointTrack
import com.example.runningtracker.domain.module.running_tracker.RunTrackerModule
import com.example.runningtracker.domain.usecases.running_tracker.SetInfoInRunningUseCase
import com.example.runningtracker.domain.usecases.running_tracker.StartRunningUseCase
import com.example.runningtracker.domain.usecases.running_tracker.StopTrackUseCase
import com.example.runningtracker.domain.usecases.running_tracker.UpdateDistanceTrackUseCase
import com.example.runningtracker.domain.usecases.running_tracker.UpdateTimeTrackUseCase
import com.example.runningtracker.domain.usecases.running_tracker.UpdateWayTrackUseCase
import com.example.runningtracker.domain.usecases.running_tracker.locale_db.SaveRunTrackerToDbUseCase
import com.example.runningtracker.presentation.screens.map_screens.state.MapScreenState
import com.example.runningtracker.presentation.screens.map_screens.utils.calculateTotalDistance
import com.example.runningtracker.presentation.screens.map_screens.utils.timeIntegerToTimeHHMMSS
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Collections.emptyList
import javax.inject.Inject

@HiltViewModel
class MapScreenViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val startRunningUseCase: StartRunningUseCase,
    private val setInfoInRunningUseCase: SetInfoInRunningUseCase,
    private val stopTrackUseCase: StopTrackUseCase,
    private val updateDistanceTrackUseCase: UpdateDistanceTrackUseCase,
    private val updateTimeTrackUseCase: UpdateTimeTrackUseCase,
    private val updateWayTrackUseCase: UpdateWayTrackUseCase,

    // use cases running_tracker database
    private val saveRunTrackerToDbUseCase: SaveRunTrackerToDbUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<MapScreenState> = MutableStateFlow<MapScreenState>(
        MapScreenState.Initial
    )
    val state: StateFlow<MapScreenState> = _state.asStateFlow()

    private val _currentRunningTracker: MutableStateFlow<RunTrackerModule?> = MutableStateFlow(
        null
    )
    val currentRunningTracker: StateFlow<RunTrackerModule?> = _currentRunningTracker.asStateFlow()

    private var timerJob: Job? = null

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

    //region Map to MapScreen
    fun changeIsLoadingMap(
        value: Boolean
    ) {
        viewModelScope.launch {
            when (_state.value) {
                MapScreenState.Initial -> {}
                is MapScreenState.Success -> {
                    val currentState =
                        _state.value as MapScreenState.Success
                    if (currentState.isLoadingMap != value) {
                        val updateState = currentState.copy(
                            isLoadingMap = value
                        )
                        _state.value = updateState
                    }
                }
            }
        }
    }

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

    fun updateUserLocation(
        userLocationLatitude: Double,
        userLocationLongitude: Double
    ) {
        viewModelScope.launch(Dispatchers.IO) {
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
    //endregion

    //region RunTracker to MapScreen
    fun startRunning(
        startPointLocationLatitude: Double,
        startPointLocationLongitude: Double
    ) {
        viewModelScope.launch {
            startRunningUseCase(
                startPointLocationLatitude = startPointLocationLatitude,
                startPointLocationLongitude = startPointLocationLongitude,
            )
                .catch {
                    Log.d("DEBUG", "$it")
                }
                .collect { runTrackModule ->
                    _currentRunningTracker.value = runTrackModule
                }
        }
    }

    fun updateWayTrack(
        pointLocationLatitude: Double,
        pointLocationLongitude: Double
    ) {
        viewModelScope.launch {
            updateWayTrackUseCase(
                pointLocationLatitude = pointLocationLatitude,
                pointLocationLongitude = pointLocationLongitude
            )
            when (_state.value) {
                MapScreenState.Initial -> {}
                is MapScreenState.Success -> {
                    val currentState =
                        _state.value as MapScreenState.Success
                    _currentRunningTracker.value.let { runTrackerModule ->
                        val updateState = currentState.copy(
                            wayTrackState = runTrackerModule?.wayTrack ?: emptyList(),
                        )
                        _state.value = updateState
                    }
                }
            }
        }
    }

    fun updateTimeTrack() {
        if (timerJob?.isActive == true) return
        timerJob = viewModelScope.launch {
            var timeInt = 0
            while (isActive) {
                delay(1000L)
                val currentState =
                    _state.value as MapScreenState.Success
                timeInt += 1
                updateTimeTrackUseCase(
                    timeTrack = timeInt
                )
                _state.value = currentState.copy(
                    timeTrack = timeIntegerToTimeHHMMSS(
                        timeIsInt = timeInt
                    )
                )
            }
        }
    }

    @SuppressLint("DefaultLocale")
    fun updateDistanceTrack() {
        viewModelScope.launch {
            val currentState =
                _state.value as MapScreenState.Success
            val dist = calculateTotalDistance(
                currentState.wayTrackState
            )
            val updateState = currentState.copy(
                distanceTrack = String.format("%.2f", dist)
            )
            updateDistanceTrackUseCase(updateState.distanceTrack)
            _state.value = updateState
        }
    }

    fun stopTrack() {
        viewModelScope.launch {
            val currentState =
                _state.value as MapScreenState.Success
            val updateState = currentState.copy(
                timeTrack = ZERO_TIME_TRACK,
                distanceTrack = DISTANCE_TRACK
            )
            saveRunTrackerToDb(
                timeTrack = currentRunningTracker.value?.timeTrack ?: 0,
                distanceTrack = _currentRunningTracker.value?.distanceTrack ?: "",
                wayTrack = _currentRunningTracker.value?.wayTrack ?: listOf()
            )
            _state.value = updateState
            timerJob?.cancel()
            timerJob = null
            stopTrackUseCase()
        }
    }
    //endregion

    //region RunTracker to MapScreen work in database
    fun saveRunTrackerToDb(
        timeTrack: Int,
        distanceTrack: String,
        wayTrack: List<PointTrack>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            saveRunTrackerToDbUseCase(
                timeTrack = timeTrack,
                distanceTrack = distanceTrack,
                wayTrack = wayTrack
            )
        }
    }

    //endregion

    companion object {
        const val ZERO_DOUBLE = 0.0
        const val ZERO_TIME_TRACK = "00:00:00"
        const val RADIUS_EARTH = 6371000.0
        const val DISTANCE_TRACK = "0.00"
    }
}