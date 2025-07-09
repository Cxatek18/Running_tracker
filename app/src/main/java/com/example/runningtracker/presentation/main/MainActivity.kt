package com.example.runningtracker.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.runningtracker.BuildConfig
import com.example.runningtracker.core.ui.theme.RunningTrackerTheme
import com.example.runningtracker.presentation.main.navigation.MainNavHost
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MapKitFactory.setApiKey(BuildConfig.YANDEX_MAPKIT_API_KEY)
        setContent {
            RunningTrackerTheme {
                MainNavHost()
            }
        }
    }
}