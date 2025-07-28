package com.example.runningtracker.history.ui.components.tracks_histor_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.runningtracker.theme.padding_12
import com.example.runningtracker.theme.padding_24

@Composable
fun NoElementInHistory(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = padding_24)
            .padding(top = padding_12),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.text_no_track_in_history),
            color = Color.Blue,
            fontSize = fz_18,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )
    }
}