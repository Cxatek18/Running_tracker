package com.example.runningtracker.runtracker.presentation.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@SuppressLint("MissingPermission")
fun locationUpdatesFlow(
    intervalMillis: Long = MapIntervals.INTERVAL_MILLIS.interval,
    fastestIntervalMillis: Long = MapIntervals.MILLIS_MIN_UPDATE_INTERVAL.interval,
    context: Context
): Flow<Location> = callbackFlow {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        intervalMillis
    ).setMinUpdateIntervalMillis(fastestIntervalMillis)
        .build()

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val latestLocation = locationResult.lastLocation
            if (latestLocation != null) {
                trySend(latestLocation).isSuccess
            }
        }
    }

    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.getMainLooper()
    )

    awaitClose {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}


fun SharedPreferences.intFlow(key: String, defaultValue: Int) = callbackFlow<Int> {
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, changedKey ->
        if (changedKey == key) {
            trySend(prefs.getInt(key, defaultValue))
        }
    }
    trySend(getInt(key, defaultValue))

    registerOnSharedPreferenceChangeListener(listener)

    awaitClose {
        unregisterOnSharedPreferenceChangeListener(listener)
    }
}