package com.example.runningtracker.runtracker.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.runningtracker.runtracker.data.locale.database.RunningTrackerDatabase
import com.example.runningtracker.runtracker.data.locale.mapper.toRunTrackerModule
import com.example.runningtracker.runtracker.data.locale.module.PointTrackDbModule
import com.example.runningtracker.runtracker.data.locale.module.RunTrackerDbModule
import com.example.runningtracker.runtracker.domain.module.RunTrackerModule
import com.example.runningtracker.runtracker.domain.repository.RunningTrackerDbRepository
import com.example.runningtracker.utils.getCurrentDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RunningTrackerDbRepositoryImpl @Inject constructor(
    private val runningTrackerDatabase: RunningTrackerDatabase,
    private val sharedPreferences: SharedPreferences
) : RunningTrackerDbRepository {

    override suspend fun saveRunTrackerToDb(
        timeTrack: Int,
        distanceTrack: String,
        key: String
    ) {
        val runDb = RunTrackerDbModule(
            id = 0,
            timeTrack = timeTrack,
            distanceTrack = distanceTrack,
            dateRunTrack = getCurrentDate()
        )

        val runId = runningTrackerDatabase.runningTrackerDao().saveRunTracker(runDb).toInt()
        sharedPreferences.edit { putInt(key, runId) }
    }

    override suspend fun updateTimeTrackToDb(trackId: Int, time: Int) {
        withContext(Dispatchers.IO) {
            runningTrackerDatabase.runningTrackerDao().updateTimeTrack(
                trackId = trackId,
                time = time
            )
        }
    }

    override suspend fun updateDistanceTrackToDb(
        trackId: Int,
        distanceTrack: String
    ) {
        withContext(Dispatchers.IO) {
            runningTrackerDatabase.runningTrackerDao().updateDistanceTrack(
                trackId = trackId,
                distance = distanceTrack
            )
        }
    }

    override suspend fun addPointToTrackDb(
        trackId: Int,
        numberPoint: Int,
        trackLatitude: Double,
        trackLongitude: Double
    ) {
        val point = PointTrackDbModule(
            id = 0,
            runId = trackId,
            numberPoint = numberPoint,
            startTrackLatitude = trackLatitude,
            startTrackLongitude = trackLongitude
        )
        withContext(Dispatchers.IO) {
            runningTrackerDatabase.runningTrackerDao().insertPointTrack(point)
        }
    }

    override suspend fun getTrackIdInSharedPreferences(
        key: String
    ): Flow<Int?> = flow {
        val idCurrentRunTracker = sharedPreferences.getInt(key, -999)
        emit(idCurrentRunTracker)
    }

    override suspend fun removeTrackIdInSharedPreferences(key: String) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit { remove(key) }
        }
    }

    override fun observeRunTracker(idRunTracker: Int): Flow<RunTrackerModule?> {
        return runningTrackerDatabase.runningTrackerDao()
            .observeRunTracker(idRunTracker)
            .map { dbModel ->
                dbModel?.toRunTrackerModule()
            }
    }
}