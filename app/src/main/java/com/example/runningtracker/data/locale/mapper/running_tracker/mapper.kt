package com.example.runningtracker.data.locale.mapper.running_tracker

import com.example.runningtracker.data.locale.module.running_tracker.PointTrackDbModule
import com.example.runningtracker.data.locale.module.running_tracker.RunTrackerWithPointsDbModule
import com.example.runningtracker.domain.module.running_tracker.PointTrack
import com.example.runningtracker.domain.module.running_tracker.RunTrackerModule

fun List<RunTrackerWithPointsDbModule>.toListRunTrackerModule(): List<RunTrackerModule> {
    return this.map {
        it.toRunTrackerModule()
    }
}

fun RunTrackerWithPointsDbModule.toRunTrackerModule(): RunTrackerModule {
    return RunTrackerModule(
        startPointTrack = this.wayTrack[0].toPointTrack(),
        timeTrack = this.runTracker.timeTrack,
        wayTrack = this.wayTrack.toListPintTrack(),
        distanceTrack = this.runTracker.distanceTrack,
        dateRunTrack = this.runTracker.dateRunTrack,
        idRunTrackerInDb = this.runTracker.id
    )
}

fun List<PointTrackDbModule>.toListPintTrack(): List<PointTrack> {
    return this.map {
        it.toPointTrack()
    }
}

fun PointTrackDbModule.toPointTrack(): PointTrack {
    return PointTrack(
        numberPoint = this.numberPoint,
        startTrackLatitude = this.startTrackLatitude,
        startTrackLongitude = this.startTrackLongitude
    )
}