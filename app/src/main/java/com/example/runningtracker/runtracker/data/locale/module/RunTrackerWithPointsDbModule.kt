package com.example.runningtracker.runtracker.data.locale.module

import androidx.room.Embedded
import androidx.room.Relation

data class RunTrackerWithPointsDbModule(
    @Embedded val runTracker: RunTrackerDbModule,

    @Relation(
        parentColumn = "id",
        entityColumn = "runId"
    )
    val wayTrack: List<PointTrackDbModule>
)