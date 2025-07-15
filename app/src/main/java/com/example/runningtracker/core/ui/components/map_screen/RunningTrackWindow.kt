package com.example.runningtracker.core.ui.components.map_screen

import android.R.attr.fontFamily
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import com.example.runningtracker.R
import com.example.runningtracker.core.ui.theme.fz_18
import com.example.runningtracker.core.ui.theme.fz_20
import com.example.runningtracker.core.ui.theme.padding_12
import com.example.runningtracker.core.ui.theme.padding_20
import com.example.runningtracker.core.ui.theme.round_18
import com.example.runningtracker.presentation.screens.map_screens.state.MapScreenState

@Composable
fun RunningTrackWindow(
    modifier: Modifier = Modifier,
    state: MapScreenState.Success
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(
                    bottomStart = round_18,
                    bottomEnd = round_18
                )
            )
            .padding(vertical = padding_12),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.text_run_run),
            color = Color.Blue,
            fontSize = fz_18,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(top = padding_20))

        Text(
            text = String.format(
                stringResource(R.string.text_time_run),
                state.timeTrack
            ),
            color = Color.Black,
            fontSize = fz_20,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(top = padding_20))

        Text(
            text = String.format(
                stringResource(R.string.text_distance_run),
                state.distanceTrack
            ),
            color = Color.Black,
            fontSize = fz_20,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )
    }
}