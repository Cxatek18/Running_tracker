package com.example.runningtracker.runtracker.ui.components.map_screen

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
import com.example.runningtracker.theme.fz_18
import com.example.runningtracker.theme.fz_20
import com.example.runningtracker.theme.padding_12
import com.example.runningtracker.theme.padding_20
import com.example.runningtracker.theme.round_18

@Composable
fun RunningTrackWindow(
    modifier: Modifier = Modifier,
    timeTrack: String,
    distanceTrack: String
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
                timeTrack
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
                distanceTrack
            ),
            color = Color.Black,
            fontSize = fz_20,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )
    }
}