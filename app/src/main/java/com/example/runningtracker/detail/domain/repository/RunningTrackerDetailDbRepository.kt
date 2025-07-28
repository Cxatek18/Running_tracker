package com.example.runningtracker.detail.domain.repository

import com.example.runningtracker.runtracker.domain.module.RunTrackerModule

interface RunningTrackerDetailDbRepository {

    suspend fun getRunTrackerFromDb(idRunTracker: Int): RunTrackerModule?
}