package com.example.runningtracker.presentation.screens.map_screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import com.example.runningtracker.R
import com.example.runningtracker.core.ui.theme.fz_16
import com.example.runningtracker.core.ui.theme.fz_18
import com.example.runningtracker.core.ui.theme.padding_20
import com.example.runningtracker.core.ui.theme.padding_24
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NeededLocationPermissions(
    modifier: Modifier = Modifier,
    multiplePermissionsState: MultiplePermissionsState
) {
    Column(
        modifier = modifier
            .padding(top = padding_24)
            .fillMaxSize()
            .padding(horizontal = padding_20),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.text_title_needed_location_permission),
            color = Color.Blue,
            fontSize = fz_18,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )
        Button(onClick = { multiplePermissionsState.launchMultiplePermissionRequest() }) {
            Text(
                text = stringResource(R.string.text_request_permissions),
                color = Color.White,
                fontSize = fz_16,
                fontFamily = FontFamily.Monospace,
            )
        }
    }
}