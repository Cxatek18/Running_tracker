package com.example.runningtracker.presentation.screens.map_screens.utils

enum class MapIntervals(val interval: Long) {
    MILLIS_MIN_UPDATE_INTERVAL(interval = 1_000L),
    INTERVAL_MILLIS(interval = 2_000L)
}