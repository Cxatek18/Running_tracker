package com.example.runningtracker.runtracker.data.locale.module

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "point_track",
    foreignKeys = [
        ForeignKey(
            entity = RunTrackerDbModule::class,
            parentColumns = ["id"],
            childColumns = ["runId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["runId"])]
)
data class PointTrackDbModule(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val runId: Int,
    val numberPoint: Int,
    val startTrackLatitude: Double,
    val startTrackLongitude: Double,
)