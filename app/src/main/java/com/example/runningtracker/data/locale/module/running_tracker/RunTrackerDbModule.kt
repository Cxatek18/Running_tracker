package com.example.runningtracker.data.locale.module.running_tracker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_tracker")
data class RunTrackerDbModule(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val timeTrack: Int,
    val distanceTrack: String
)