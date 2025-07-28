package com.example.runningtracker.runtracker.ui.components.map_ui_element

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.runningtracker.R
import com.example.runningtracker.runtracker.domain.module.PointTrack
import ru.sulgik.mapkit.compose.CameraPositionState
import ru.sulgik.mapkit.compose.Placemark
import ru.sulgik.mapkit.compose.PlacemarkState
import ru.sulgik.mapkit.compose.Polyline
import ru.sulgik.mapkit.compose.YandexMap
import ru.sulgik.mapkit.compose.rememberPolylineState
import ru.sulgik.mapkit.geometry.Point
import ru.sulgik.mapkit.geometry.Polyline
import ru.sulgik.mapkit.map.ImageProvider
import ru.sulgik.mapkit.map.fromResource


@Composable
fun MapYandexElement(
    modifier: Modifier = Modifier,
    context: Context,
    cameraPositionState: CameraPositionState,
    placeMarkState: PlacemarkState,
    wayTrackState: List<PointTrack>
) {
    YandexMap(
        cameraPositionState = cameraPositionState,
        modifier = modifier.fillMaxSize()
    ) {
        Placemark(
            state = placeMarkState,
            icon = ImageProvider.fromResource(
                context,
                R.drawable.img_place_from
            ),
        )

        val currentPolyline = remember(wayTrackState) {
            if (wayTrackState.size > 1) {
                Polyline(wayTrackState.map {
                    Point(it.startTrackLatitude, it.startTrackLongitude)
                })
            } else null
        }

        currentPolyline?.let { polyline ->
            key(polyline) {
                Polyline(
                    state = rememberPolylineState(geometry = polyline),
                    strokeColor = Color.Green,
                    strokeWidth = 3f
                )
            }
        }
    }
}