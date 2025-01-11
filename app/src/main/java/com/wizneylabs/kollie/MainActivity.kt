package com.wizneylabs.kollie

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wizneylabs.kollie.ui.theme.KollieTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KollieTheme {
                KollieApp()
            }
        }
    }
}

@Composable
fun KollieApp() {

    val configuration = LocalConfiguration.current;

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
    {
        MyCanvas();
    }
    else
    {
        MyCanvas2();
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
    KollieTheme {
        MyCanvas()
    }
}
