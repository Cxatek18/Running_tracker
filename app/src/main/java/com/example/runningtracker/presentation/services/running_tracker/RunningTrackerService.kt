package com.example.runningtracker.presentation.services.running_tracker

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import com.example.runningtracker.R
import com.example.runningtracker.domain.module.running_tracker.RunTrackerModule
import com.example.runningtracker.domain.usecases.running_tracker.GetCurrentRunTrackerUseCase
import com.example.runningtracker.domain.usecases.running_tracker.GetRunningTrackerBackgroundModuleUseCase
import com.example.runningtracker.domain.usecases.running_tracker.StartRunningUseCase
import com.example.runningtracker.domain.usecases.running_tracker.StopTrackUseCase
import com.example.runningtracker.domain.usecases.running_tracker.UpdateDistanceTrackUseCase
import com.example.runningtracker.domain.usecases.running_tracker.UpdateTimeTrackUseCase
import com.example.runningtracker.domain.usecases.running_tracker.UpdateWayTrackUseCase
import com.example.runningtracker.domain.usecases.running_tracker.locale_db.SaveRunTrackerToDbUseCase
import com.example.runningtracker.presentation.main.MainActivity
import com.example.runningtracker.presentation.screens.map_screens.utils.calculateTotalDistance
import com.example.runningtracker.presentation.utils.timeIntegerToTimeHHMMSS
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class RunningTrackerService : Service() {

    @Inject
    lateinit var startRunningUseCase: StartRunningUseCase

    @Inject
    lateinit var stopTrackUseCase: StopTrackUseCase

    @Inject
    lateinit var updateDistanceTrackUseCase: UpdateDistanceTrackUseCase

    @Inject
    lateinit var updateTimeTrackUseCase: UpdateTimeTrackUseCase

    @Inject
    lateinit var updateWayTrackUseCase: UpdateWayTrackUseCase

    @Inject
    lateinit var getCurrentRunTrackerUseCase: GetCurrentRunTrackerUseCase

    @Inject
    lateinit var getRunningTrackerBackgroundModuleUseCase: GetRunningTrackerBackgroundModuleUseCase

    @Inject
    lateinit var saveRunTrackerToDbUseCase: SaveRunTrackerToDbUseCase

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val trackScope = CoroutineScope(Dispatchers.IO)
    private val timeTrackScope = CoroutineScope(Dispatchers.IO)
    private val savedTrackScope = CoroutineScope(Dispatchers.Main)

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationCallback: LocationCallback? = null
    private var foregroundStarted = false

    private var runTracker: RunTrackerModule? = null
    private var isStartBackgroundWork: Boolean = false

    private var timerJob: Job? = null
    private var time: Int = 0

    override fun onBind(intent: Intent?): IBinder? = null

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        CoroutineScope(Dispatchers.Main).launch {
            getCurrentRunTrackerUseCase()
                .filterNotNull()
                .collectLatest { runTrackerModule ->
                    runTracker = runTrackerModule
                    if (!foregroundStarted) {
                        runTracker?.let { runTrackerModuleNotNull ->
                            startForeground(
                                FOREGROUND_CHANNEL_ID,
                                buildNotification(runTrackerModuleNotNull)
                            )
                            startRunningUseCase(
                                runTrackerModuleNotNull.wayTrack.last().startTrackLatitude,
                                runTrackerModuleNotNull.wayTrack.last().startTrackLongitude
                            ).collect { runTrackStarted ->
                                runTracker = runTrackStarted
                                time = runTrackStarted?.timeTrack ?: 0
                                runTrackStarted?.let { it -> updateNotification(it) }
                            }
                        }
                        foregroundStarted = true
                    }
                }
        }

        CoroutineScope(Dispatchers.Main).launch {
            getRunningTrackerBackgroundModuleUseCase()
                .distinctUntilChanged()
                .collect { state ->
                    isStartBackgroundWork = state.isBackground

                    if (state.isBackground) {
                        createLocationCallback()

                        val locationRequest = LocationRequest.Builder(
                            Priority.PRIORITY_HIGH_ACCURACY, 1000L
                        ).setMinUpdateIntervalMillis(1000L).build()

                        locationCallback?.let {
                            fusedLocationClient.requestLocationUpdates(
                                locationRequest,
                                it,
                                Looper.getMainLooper()
                            )
                        }

                    } else {
                        updateTimeTrack()
                        locationCallback?.let {
                            fusedLocationClient.removeLocationUpdates(it)
                        }
                    }
                }
        }
    }

    fun updateTimeTrack() {
        if (timerJob?.isActive == true) return
        timerJob = timeTrackScope.launch {
            while (isActive) {
                delay(1000L)
                time += 1
                updateTimeTrackUseCase(
                    timeTrack = time
                )
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == this.getString(R.string.text_intent_action_stop_tracking)) {
            stopSelf()
            return START_NOT_STICKY
        }
        return START_STICKY
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            @SuppressLint("DefaultLocale")
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation ?: return

                if (!isStartBackgroundWork) return

                trackScope.launch {
                    updateWayTrackUseCase(location.latitude, location.longitude)

                    val current = getCurrentRunTrackerUseCase().firstOrNull()
                    current?.let {
                        val dist = calculateTotalDistance(
                            it.wayTrack
                        )
                        updateDistanceTrackUseCase(String.format("%.2f", dist))
                    }
                }
            }
        }

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000L
        ).setMinUpdateIntervalMillis(1000L).build()

        locationCallback?.let {
            fusedLocationClient.requestLocationUpdates(
                request,
                it,
                Looper.getMainLooper()
            )
        }

    }

    private fun buildNotification(runTracker: RunTrackerModule): Notification {
        val stopIntent = Intent(this, RunningTrackerService::class.java).apply {
            action = this@RunningTrackerService.getString(R.string.text_intent_action_stop_tracking)
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            this,
            10,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val formattedTime = timeIntegerToTimeHHMMSS(runTracker.timeTrack)
        val builder = NotificationCompat.Builder(
            this,
            this.getString(R.string.text_tracking_channel)
        )
            .setContentTitle(this.getString(R.string.text_title_service))
            .setContentText(
                String.format(
                    this.getString(R.string.text_content_service),
                    formattedTime,
                    runTracker.distanceTrack
                )
            )
            .setSmallIcon(R.drawable.ic_run)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)


        builder.addAction(
            R.drawable.ic_run,
            this.getString(R.string.text_stop_service),
            stopPendingIntent
        )
        builder.addAction(
            R.drawable.ic_run,
            this.getString(R.string.text_open_app),
            openAppPendingIntent
        )

        return builder.build()
    }

    private fun updateNotification(runTracker: RunTrackerModule) {
        val notification = buildNotification(runTracker)
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(FOREGROUND_CHANNEL_ID, notification)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                this.getString(R.string.text_tracking_channel),
                this.getString(R.string.text_location_tracking),
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timerJob?.cancel()
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
        runBlocking {
            if (isStartBackgroundWork && runTracker != null) {
                try {
                    saveRunTrackerToDbUseCase(
                        timeTrack = runTracker!!.timeTrack,
                        distanceTrack = runTracker!!.distanceTrack,
                        wayTrack = runTracker!!.wayTrack
                    )
                    android.os.Process.killProcess(android.os.Process.myPid())
                } catch (e: Exception) {
                    Log.d(LOG_TAG_DEBUG, "$e")
                }
            }
        }
        savedTrackScope.cancel()
        serviceScope.cancel()
        trackScope.cancel()
    }

    companion object {
        private const val FOREGROUND_CHANNEL_ID = 1
        private const val LOG_TAG_DEBUG = "DEBUG"
    }
}