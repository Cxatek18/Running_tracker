package com.example.runningtracker.data.repository.running_tracker

import android.util.Log
import com.example.runningtracker.domain.module.running_tracker.PointTrack
import com.example.runningtracker.domain.module.running_tracker.RunTrackerModule
import com.example.runningtracker.domain.repository.running_tracker.RunningTrackerRepository
import com.google.android.gms.common.util.CollectionUtils.listOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class RunningTrackerRepositoryImpl @Inject constructor() : RunningTrackerRepository {

    private val currentRunTracker = MutableStateFlow<RunTrackerModule?>(
        null
    )

    override fun startRunning(
        startPointLocationLatitude: Double,
        startPointLocationLongitude: Double
    ): Flow<RunTrackerModule?> = flow {
        if (currentRunTracker.value == null) {
            val startPointTrack = PointTrack(
                numberPoint = 1,
                startTrackLatitude = startPointLocationLatitude,
                startTrackLongitude = startPointLocationLongitude
            )
            val runningTrack = RunTrackerModule(
                startPointTrack = startPointTrack,
                distanceTrack = START_DISTANCE_TRACK,
                timeTrack = ZERO,
                wayTrack = listOf(
                    startPointTrack
                )
            )
            currentRunTracker.value = runningTrack
        }
        currentRunTracker.collect { emit(it) }
    }

    override suspend fun setInfoInRunning(runTrackerModule: RunTrackerModule) {
        currentRunTracker.value = runTrackerModule
    }

    override suspend fun updateDistanceTrack(distanceTrack: String) {
        currentRunTracker.update {
            it?.copy(
                distanceTrack = distanceTrack
            )
        }
    }

    override suspend fun updateWayTrack(
        pointLocationLatitude: Double,
        pointLocationLongitude: Double,
    ) {
        currentRunTracker.update { runTrackerModule ->
            runTrackerModule?.let {
                val wayTrack = runTrackerModule.wayTrack.toMutableList()
                val lastElement = wayTrack.last()
                wayTrack.add(
                    PointTrack(
                        numberPoint = lastElement.numberPoint + 1,
                        startTrackLatitude = pointLocationLatitude,
                        startTrackLongitude = pointLocationLongitude
                    )
                )
                runTrackerModule.copy(
                    wayTrack = wayTrack
                )
            }
        }
    }

    override suspend fun updateTimeTrack(
        timeTrack: Int
    ) {
        currentRunTracker.update {
            it?.copy(
                timeTrack = timeTrack
            )
        }
    }

    override suspend fun stopTrack() {
        Log.i("INFO", "save track to db")
        currentRunTracker.update {
            null
        }
    }

    companion object {

        private const val START_DISTANCE_TRACK: String = "0,00"
        private const val ZERO: Int = 0
    }
}