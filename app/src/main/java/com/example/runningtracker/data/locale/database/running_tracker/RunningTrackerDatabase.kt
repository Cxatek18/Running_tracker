package com.example.runningtracker.data.locale.database.running_tracker

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.runningtracker.data.locale.dao.running_tracker.RunningTrackerDao
import com.example.runningtracker.data.locale.module.running_tracker.PointTrackDbModule
import com.example.runningtracker.data.locale.module.running_tracker.RunTrackerDbModule

@Database(
    entities = [
        RunTrackerDbModule::class,
        PointTrackDbModule::class
    ],
    version = 1,
    exportSchema = false
)
abstract class RunningTrackerDatabase : RoomDatabase() {
    abstract fun runningTrackerDao(): RunningTrackerDao
}