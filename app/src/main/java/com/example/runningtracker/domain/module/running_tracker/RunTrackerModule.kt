package com.example.runningtracker.domain.module.running_tracker

data class RunTrackerModule(
    val startPointTrack: PointTrack,
    val wayTrack: List<PointTrack>,
    val distanceTrack: String,
    val dateRunTrack: String = "",
    val idRunTrackerInDb: Int = -1,
    val timeTrack: Int = 0,
)