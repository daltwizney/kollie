package com.wizneylabs.kollie

import android.content.res.Configuration
import android.graphics.RuntimeShader
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wizneylabs.kollie.ui.theme.KollieTheme

import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val viewModel: MainViewModel = viewModel();

            KollieTheme {
                KollieApp(viewModel)
            }
        }
    }
}

@Composable
fun KollieApp(viewModel: MainViewModel) {

    val configuration = LocalConfiguration.current;

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
    {
        MyAGSLCanvas();
    }
    else
    {
        MyCanvas();

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = viewModel.Count.value,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 42.sp
            )
            Button(onClick= { viewModel.increment() }) {
                Text("Increment")
            }
        }
    }
}

@Composable
fun MyAGSLCanvas() {

    val shader = remember {
        RuntimeShader("""
            uniform float2 resolution;
            uniform float time;
            
            half4 main(float2 coord) {
                float2 uv = coord/resolution.xy;
                float3 color = float3(
                    sin(uv.x * 6.28 + time) * 0.5 + 0.5,
                    sin(uv.y * 6.28 + time) * 0.5 + 0.5,
                    sin((uv.x + uv.y) * 6.28 + time) * 0.5 + 0.5
                );
                return half4(color, 1.0);
            }
        """.trimIndent())
    }

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
        shader.setFloatUniform(
            "resolution",
            size.width,
            size.height
        )
        shader.setFloatUniform("time", time)

        drawRect(
            brush = ShaderBrush(shader),
            size = size
        )
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
