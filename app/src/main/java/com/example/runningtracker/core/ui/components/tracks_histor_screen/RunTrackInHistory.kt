package com.example.runningtracker.core.ui.components.tracks_histor_screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.runningtracker.R
import com.example.runningtracker.core.ui.theme.fz_16
import com.example.runningtracker.core.ui.theme.padding_12
import com.example.runningtracker.core.ui.theme.padding_8
import com.example.runningtracker.core.ui.theme.round_18

@Composable
fun RunTrackInHistory(
    modifier: Modifier = Modifier,
    timeTrack: String,
    distanceTrack: String,
    onCLickDeleteTrack: () -> Unit,
    dateRunTrack: String = "",
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.Black,
                shape = RoundedCornerShape(
                    size = round_18
                )
            )
            .padding(
                vertical = padding_8,
                horizontal = padding_12
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_run),
                contentDescription = stringResource(R.string.text_cd_ic_run),
                tint = Color.White
            )

            Column {
                Text(
                    modifier = Modifier,
                    text = String.format(stringResource(R.string.text_time), timeTrack),
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.W700,
                    fontSize = fz_16,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    modifier = Modifier,
                    text = String.format(stringResource(R.string.text_distance), distanceTrack),
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.W700,
                    fontSize = fz_16,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Text(
            modifier = Modifier
                .padding(top = padding_8)
                .fillMaxWidth(),
            text = String.format(stringResource(R.string.text_date), dateRunTrack),
            color = Color.White,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.W700,
            fontSize = fz_16,
            overflow = TextOverflow.Ellipsis
        )

        IconButton(
            modifier = modifier
                .padding(top = padding_8)
                .align(Alignment.End),
            onClick = {
                onCLickDeleteTrack()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.text_cd_ic_delete),
                tint = Color.Red
            )
        }
    }
}