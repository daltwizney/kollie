package com.wizneylabs.freestyle

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wizneylabs.freestyle.ui.theme.KollieTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KollieTheme {

                FreestyleCanvas();
            }
        }
    }
}

@Composable
fun FreestyleCanvas() {

    val canvasViewModel: FreestyleCanvasViewModel = viewModel();

    LaunchedEffect(Unit) {

        while (true) {
            withFrameNanos {
                canvasViewModel.update(it)
            };
        }
    }

    val circleData = canvasViewModel.circleData.collectAsState().value;

    Canvas(modifier = Modifier.fillMaxSize()) {

        Log.d("radius", "canvas refreshing!");

        drawCircle(
            color = circleData.color,
            radius = circleData.radius,
            center = Offset(
                x = circleData.transform.position.x,
                y = circleData.transform.position.y
//                x = size.width / 2,
//                y = size.height / 2
            )
        )
    }
}