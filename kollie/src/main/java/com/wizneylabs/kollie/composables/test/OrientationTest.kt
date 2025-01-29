package com.wizneylabs.kollie.composables.test

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel

@Composable
fun OrientationHandler(context: Context, configuration: Configuration, viewModel: ViewModel) {

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
    {
        MyAGSLCanvas(context);
    }
    else
    {
        MyCanvas();

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
//            Text(text = viewModel.Count.value,
//                modifier = Modifier
//                    .fillMaxWidth(),
//                textAlign = TextAlign.Center,
//                fontSize = 42.sp
//            )
//            Button(onClick= { viewModel.increment() }) {
//                Text("Increment")
//            }
        }
    }
}

@Composable
fun MyCanvas() {
    Canvas(modifier = Modifier
        .padding(vertical = 20.dp)
        .fillMaxSize()) {

        val multiColorGradient = Brush.verticalGradient(
            0f to Color.Red,
            0.3f to Color.Yellow,
            0.6f to Color.Green,
            1f to Color.Blue
        )

        drawRect(brush = multiColorGradient);
    }
}

@Composable
fun MyCanvas2() {
    Canvas(modifier = Modifier
        .padding(vertical = 20.dp)
        .fillMaxSize()) {

        drawRect(color = Color.Blue);
    }
}

@Preview("Canvas Previews",
    device = Devices.PIXEL_5,
)
@Composable
fun CanvasPreview() {
        MyCanvas()
}
