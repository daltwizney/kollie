package com.wizneylabs.kollie

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun KollieCanvas(viewModel: MainViewModel) {

    Canvas(modifier = Modifier
        .padding(vertical = 20.dp)
        .fillMaxSize()
    ) {
        Log.d("MainViewModel", "re-rendering!");

        @Suppress("UNUSED_VARIABLE")
        val invalidator = viewModel.frameCounter.value;

        drawCircle(color = Color.Blue,
            center = Offset(150f, 150f),
            radius = 50f
        )
    }
}
