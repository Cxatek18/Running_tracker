package com.example.runningtracker.runtracker.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.runningtracker.R
import com.example.runningtracker.detail.domain.usecases.GetRunTrackerFromDbUseCase
import com.example.runningtracker.main.MainActivity
import com.example.runningtracker.runtracker.domain.module.PointTrack
import com.example.runningtracker.runtracker.domain.module.RunTrackerModule
import com.example.runningtracker.runtracker.domain.repository.RunningTrackerDbRepository
import com.example.runningtracker.runtracker.presentation.utils.calculateTotalDistance
import com.example.runningtracker.runtracker.presentation.utils.locationUpdatesFlow
import com.example.runningtracker.utils.timeIntegerToTimeHHMMSS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class RunningTrackerService : Service() {

    @Inject
    lateinit var repo: RunningTrackerDbRepository

    @Inject
    lateinit var getRunTrackerFromDbUseCase: GetRunTrackerFromDbUseCase

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var trackId: Int = -1
    private var timerJob: Job? = null
    private var locationJob: Job? = null
    private var elapsedTime = 0
    private val points: MutableList<PointTrack> = mutableListOf<PointTrack>()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent?.action == getString(R.string.text_intent_action_stop_tracking)) {
            stopTracking()
            stopSelf()
            return START_NOT_STICKY
        } else {
            startTracking()
        }
        return START_NOT_STICKY
    }

    @SuppressLint("DefaultLocale")
    private fun startTracking() {
        CoroutineScope(Dispatchers.IO).launch {
            repo.saveRunTrackerToDb(
                0,
                "0.0",
                getString(R.string.text_key_current_track_id)
            )
            trackId = repo.getTrackIdInSharedPreferences(
                getString(R.string.text_key_current_track_id)
            ).first() ?: -1

            val initialRun = getRunTrackerFromDbUseCase(trackId)
            withContext(Dispatchers.Main) {
                initialRun?.let {
                    startForeground(1, buildNotification(initialRun))
                }
            }

            timerJob = launch {
                while (isActive) {
                    delay(1000)
                    elapsedTime++
                    repo.updateTimeTrackToDb(trackId, elapsedTime)

                    val run = getRunTrackerFromDbUseCase(trackId)
                    withContext(Dispatchers.Main) {
                        run?.let {
                            updateNotification(run)
                        }
                    }
                }
            }

            locationJob = launch(Dispatchers.Main) {
                locationUpdatesFlow(context = applicationContext).collect { location ->
                    val point = PointTrack(
                        numberPoint = points.size + 1,
                        startTrackLatitude = location.latitude,
                        startTrackLongitude = location.longitude
                    )
                    points.add(
                        point
                    )
                    val dist = calculateTotalDistance(points)
                    repo.updateDistanceTrackToDb(trackId, String.format("%.2f", dist))
                    repo.addPointToTrackDb(
                        trackId,
                        point.numberPoint,
                        point.startTrackLatitude,
                        point.startTrackLongitude
                    )

                    val run = getRunTrackerFromDbUseCase(trackId)
                    run?.let {
                        updateNotification(run)
                    }
                }
            }
        }
    }

    private fun stopTracking() {
        CoroutineScope(Dispatchers.Main).launch {
            repo.removeTrackIdInSharedPreferences(
                getString(R.string.text_key_current_track_id)
            )
        }
        timerJob?.cancel()
        locationJob?.cancel()
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        stopTracking()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null


    @SuppressLint("ObsoleteSdkInt")
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.text_tracking_channel),
                getString(R.string.text_location_tracking),
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(runTracker: RunTrackerModule): Notification {
        val stopIntent = Intent(this, RunningTrackerService::class.java).apply {
            action = getString(R.string.text_intent_action_stop_tracking)
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
        val contentText = String.format(
            getString(R.string.text_content_service),
            formattedTime,
            runTracker.distanceTrack
        )

        val builder = NotificationCompat.Builder(
            this,
            getString(R.string.text_tracking_channel)
        )
            .setContentTitle(getString(R.string.text_title_service))
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_run)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .addAction(
                R.drawable.ic_run,
                getString(R.string.text_stop_service),
                stopPendingIntent
            )
            .addAction(
                R.drawable.ic_run,
                getString(R.string.text_open_app),
                openAppPendingIntent
            )

        return builder.build()
    }

    private fun updateNotification(runTracker: RunTrackerModule) {
        val notification = buildNotification(runTracker)
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(1, notification)
    }
}