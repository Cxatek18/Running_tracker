package com.example.runningtracker.core.ui.components.tracks_histor_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.runningtracker.core.ui.theme.size_40
import com.example.runningtracker.core.ui.theme.size_border_card_2

@Composable
fun LoadingElement(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(size_40),
            color = MaterialTheme.colorScheme.secondary,
            strokeWidth = size_border_card_2,
            trackColor = Color(0xFF5E4B8B),
        )
    }
}