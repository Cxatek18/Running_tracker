package com.example.runningtracker.runtracker.data.locale.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.runningtracker.runtracker.data.locale.dao.RunningTrackerDao
import com.example.runningtracker.runtracker.data.locale.module.PointTrackDbModule
import com.example.runningtracker.runtracker.data.locale.module.RunTrackerDbModule

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