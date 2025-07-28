package com.example.runningtracker.runtracker.ui.components.map_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.runningtracker.theme.fz_18
import com.example.runningtracker.theme.padding_20
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AllowNotifications(
    modifier: Modifier = Modifier,
    notificationPermission: PermissionState
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = padding_20),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            notificationPermission.launchPermissionRequest()
        }) {
            Text(
                text = stringResource(
                    R.string.text_allow_notifications
                ),
                color = Color.Blue,
                fontSize = fz_18,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center
            )
        }
    }
}