package com.example.runningtracker.core.ui.widgets.map_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.example.runningtracker.core.ui.theme.padding_12
import com.example.runningtracker.core.ui.theme.round_18

@Composable
fun LocationLoadingBar(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = padding_12)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(
                    size = round_18
                )
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(all = padding_12),
            text = stringResource(R.string.text_loading_location),
            color = Color.Blue,
            fontSize = fz_18,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )
    }
}