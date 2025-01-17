package com.wizneylabs.kollie

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class GameState(
    val deltaTime: Float = 0f,
    val lastFrameTimeMillis: Long = System.currentTimeMillis()
)

@Composable
fun KollieCanvas(viewModel: MainViewModel) {

    var time by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos { frameTimeNanos ->
                time = (frameTimeNanos / 1_000_000_000f)
            }
        }
    }

    Canvas(modifier = Modifier
        .padding(vertical = 20.dp)
        .fillMaxSize()
    ) {

        drawCircle(color = Color.Blue,
            center = Offset(150f, 150f),
            radius = 50f
        )
    }
}