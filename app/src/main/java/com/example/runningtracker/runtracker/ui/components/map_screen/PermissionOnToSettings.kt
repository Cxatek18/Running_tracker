package com.example.runningtracker.runtracker.ui.components.map_screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.example.runningtracker.theme.fz_16
import com.example.runningtracker.theme.fz_18
import com.example.runningtracker.theme.padding_20
import com.example.runningtracker.theme.padding_24

@Composable
fun PermissionOnToSettings(
    modifier: Modifier = Modifier,
    context: Context
) {
    Column(
        modifier = modifier
            .padding(top = padding_24)
            .fillMaxSize()
            .padding(horizontal = padding_20),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.text_title_permission_in_to_settings_screen),
            color = Color.Blue,
            fontSize = fz_18,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(top = padding_20))
        Button(onClick = {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.fromParts("package", context.packageName, null)
            context.startActivity(intent)
        }) {
            Text(
                text = stringResource(R.string.text_open_settings),
                color = Color.White,
                fontSize = fz_16,
                fontFamily = FontFamily.Monospace,
            )
        }
    }
}