package com.example.runningtracker.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("DefaultLocale")
fun timeIntegerToTimeHHMMSS(timeIsInt: Int): String {
    val hours = timeIsInt / 3600
    val minutes = (timeIsInt % 3600) / 60
    val seconds = timeIsInt % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    val formatted = sdf.format(Date())
    return formatted
}