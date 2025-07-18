package com.example.runningtracker.data.repository.running_tracker.locale_db

import com.example.runningtracker.data.locale.database.running_tracker.RunningTrackerDatabase
import com.example.runningtracker.data.locale.module.running_tracker.PointTrackDbModule
import com.example.runningtracker.data.locale.module.running_tracker.RunTrackerDbModule
import com.example.runningtracker.data.utils.getCurrentDate
import com.example.runningtracker.domain.module.running_tracker.PointTrack
import com.example.runningtracker.domain.repository.running_tracker.locale_db.RunningTrackerDbRepository
import javax.inject.Inject

class RunningTrackerDbRepositoryImpl @Inject constructor(
    private val runningTrackerDatabase: RunningTrackerDatabase
) : RunningTrackerDbRepository {

    override suspend fun saveRunTrackerToDb(
        timeTrack: Int,
        distanceTrack: String,
        wayTrack: List<PointTrack>
    ) {
        val runDb = RunTrackerDbModule(
            id = 0,
            timeTrack = timeTrack,
            distanceTrack = distanceTrack,
            dateRunTrack = getCurrentDate()
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
}