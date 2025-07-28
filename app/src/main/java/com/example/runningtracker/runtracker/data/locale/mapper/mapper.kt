package com.example.runningtracker.runtracker.data.locale.mapper

import com.example.runningtracker.runtracker.data.locale.module.PointTrackDbModule
import com.example.runningtracker.runtracker.data.locale.module.RunTrackerWithPointsDbModule
import com.example.runningtracker.runtracker.domain.module.PointTrack
import com.example.runningtracker.runtracker.domain.module.RunTrackerModule

fun List<RunTrackerWithPointsDbModule>.toListRunTrackerModule(): List<RunTrackerModule> {
    return this.map {
        it.toRunTrackerModule()
    }
}

fun RunTrackerWithPointsDbModule.toRunTrackerModule(): RunTrackerModule {
    val startPoint = this.wayTrack.firstOrNull()?.toPointTrack() ?: defaultPoint()
    return RunTrackerModule(
        startPointTrack = startPoint,
        timeTrack = this.runTracker.timeTrack,
        wayTrack = this.wayTrack.toListPintTrack(),
        distanceTrack = this.runTracker.distanceTrack,
        dateRunTrack = this.runTracker.dateRunTrack,
        idRunTrackerInDb = this.runTracker.id
    )
}

fun defaultPoint(): PointTrack {
    return PointTrack(
        numberPoint = 0,
        startTrackLatitude = 0.0,
        startTrackLongitude = 0.0
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