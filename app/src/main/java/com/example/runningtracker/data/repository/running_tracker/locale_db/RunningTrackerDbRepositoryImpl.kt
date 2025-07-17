package com.example.runningtracker.data.repository.running_tracker.locale_db

import com.example.runningtracker.data.locale.database.running_tracker.RunningTrackerDatabase
import com.example.runningtracker.data.locale.mapper.running_tracker.toListRunTrackerModule
import com.example.runningtracker.data.locale.mapper.running_tracker.toRunTrackerModule
import com.example.runningtracker.data.locale.module.running_tracker.PointTrackDbModule
import com.example.runningtracker.data.locale.module.running_tracker.RunTrackerDbModule
import com.example.runningtracker.domain.module.running_tracker.PointTrack
import com.example.runningtracker.domain.module.running_tracker.RunTrackerModule
import com.example.runningtracker.domain.repository.running_tracker.locale_db.RunningTrackerDbRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RunningTrackerDbRepositoryImpl @Inject constructor(
    private val runningTrackerDatabase: RunningTrackerDatabase
) : RunningTrackerDbRepository {

    override fun getListRunTrackerFromDb(): Flow<List<RunTrackerModule>> = flow {
        emit(
            runningTrackerDatabase
                .runningTrackerDao()
                .getListRunTracker()
                .toListRunTrackerModule()
        )
    }

    override suspend fun getRunTrackerFromDb(idRunTracker: Int): RunTrackerModule? {
        return runningTrackerDatabase.runningTrackerDao().getRunTracker(
            idRunTracker = idRunTracker
        )?.toRunTrackerModule()
    }

    override suspend fun saveRunTrackerToDb(
        timeTrack: Int,
        distanceTrack: String,
        wayTrack: List<PointTrack>
    ) {
        val runDb = RunTrackerDbModule(
            id = 0,
            timeTrack = timeTrack,
            distanceTrack = distanceTrack
        )

        val runId = runningTrackerDatabase.runningTrackerDao().saveRunTracker(runDb).toInt()

        val pointTrackDbList = wayTrack.mapIndexed { index, point ->
            PointTrackDbModule(
                id = 0,
                runId = runId,
                numberPoint = point.numberPoint,
                startTrackLatitude = point.startTrackLatitude,
                startTrackLongitude = point.startTrackLongitude
            )
        }
        runningTrackerDatabase.runningTrackerDao().insertPointTrackList(pointTrackDbList)
    }

    override suspend fun deleteRunTrackerToDb(idRunTracker: Int) {
        runningTrackerDatabase.runningTrackerDao().deleteRunTracker(idRunTracker)
    }
}