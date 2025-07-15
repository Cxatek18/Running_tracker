package com.example.runningtracker.presentation.screens.map_screens.utils

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun timeIntegerToTimeHHMMSS(timeIsInt: Int): String {
    val hours = timeIsInt / 3600
    val minutes = (timeIsInt % 3600) / 60
    val seconds = timeIsInt % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}