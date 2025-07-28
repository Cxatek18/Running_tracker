package com.example.runningtracker.runtracker.presentation.utils

import com.example.runningtracker.runtracker.domain.module.PointTrack
import com.example.runningtracker.runtracker.presentation.view_models.MapScreenViewModel
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

fun calcDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2).pow(2) +
            cos(Math.toRadians(lat1)) *
            cos(Math.toRadians(lat2)) *
            sin(dLon / 2).pow(2)
    val c = 2 * asin(sqrt(a))
    return MapScreenViewModel.Companion.RADIUS_EARTH * c
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