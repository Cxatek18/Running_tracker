package com.example.runningtracker.main.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.runningtracker.R
import com.example.runningtracker.detail.presentation.navigation.navigateToTrackDetail
import com.example.runningtracker.history.presentation.navigation.navigateToTracksHistoryScreen
import com.example.runningtracker.runtracker.presentation.navigation.MainGraph
import com.example.runningtracker.runtracker.presentation.navigation.mapGraph
import com.example.runningtracker.theme.fz_24
import com.example.runningtracker.theme.padding_0

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavHost(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets(padding_0)),
                title = {
                    Text(
                        text = stringResource(R.string.name_top_bar),
                        fontSize = fz_24,
                        color = Color.White
                    )
                },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.secondary,
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                    actionIconContentColor = MaterialTheme.colorScheme.secondary,
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigateToTracksHistoryScreen()
                    }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = stringResource(R.string.cd_text_icon_history),
                            tint = Color.White
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = MainGraph
        ) {
            mapGraph(
                modifier = Modifier
                    .padding(paddingValues = paddingValues),
                onClickNavigateToTrackDetail = {
                    navController.navigateToTrackDetail(it)
                }
            )
        }
    }
}