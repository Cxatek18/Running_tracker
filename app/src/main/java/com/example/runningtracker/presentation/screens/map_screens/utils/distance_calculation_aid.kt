package com.example.runningtracker.presentation.screens.map_screens.utils

import com.example.runningtracker.domain.module.running_tracker.PointTrack
import com.example.runningtracker.presentation.screens.map_screens.view_models.MapScreenViewModel.Companion.RADIUS_EARTH
import kotlin.math.pow

fun calcDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = kotlin.math.sin(dLat / 2).pow(2) +
            kotlin.math.cos(Math.toRadians(lat1)) *
            kotlin.math.cos(Math.toRadians(lat2)) *
            kotlin.math.sin(dLon / 2).pow(2)
    val c = 2 * kotlin.math.asin(kotlin.math.sqrt(a))
    return RADIUS_EARTH * c
}

fun calculateTotalDistance(track: List<PointTrack>): Double {
    var distance = 0.0
    for (i in 1 until track.size) {
        val prev = track[i - 1]
        val curr = track[i]
        distance += calcDistance(
            prev.startTrackLatitude,
            prev.startTrackLongitude,
            curr.startTrackLatitude,
            curr.startTrackLongitude
        )
    }
    return distance
}