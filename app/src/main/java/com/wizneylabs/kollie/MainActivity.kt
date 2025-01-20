package com.wizneylabs.kollie

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.RuntimeShader
import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wizneylabs.kollie.ui.theme.KollieTheme

import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

            KollieTheme {
                KollieApp()
            }
        }
    }
}

@Composable
fun KollieApp() {

    val context = LocalContext.current;
    val configuration = LocalConfiguration.current;

    val screenWidth = configuration.screenWidthDp.dp;
    val screenHeight = configuration.screenHeightDp.dp;

    val mainViewModel = viewModel<MainViewModel>();
    val pathfinderAppViewModel = viewModel<PathfinderAppViewModel>();

    MazeRenderer(pathfinderAppViewModel, screenWidth, screenHeight);
//    KollieCanvas(mainViewModel);
//    MyAGSLCanvas(context);
}

@Composable
fun MazeRenderer(viewModel: PathfinderAppViewModel,
                 screenWidth: Dp,
                 screenHeight: Dp) {

    val context = LocalContext.current;
    val configuration = LocalConfiguration.current;

    val screenWidthPx = with (LocalDensity.current) { screenWidth.toPx() };
    val screenHeightPx = with (LocalDensity.current) { screenHeight.toPx() };

    val cellSize: Int = 49;

    val rows: Int = (screenHeightPx / cellSize).toInt();
    val columns: Int = (screenWidthPx / cellSize).toInt();

    Log.d("MazeRenderer", "rows = ${rows}, cols = ${columns}");

    var isBlack = true;

    Canvas(modifier = Modifier
        .padding(vertical = 20.dp)
        .fillMaxSize()
    ) {
        for (i in 0..rows - 1)
        {
            for (j in 0..columns - 1)
            {
                val cellColor = if (isBlack) Color.Black else Color.Blue;

                drawRect(color = cellColor,
                    topLeft = Offset(
                        j * 1.0f * cellSize,
                        i * 1.0f * cellSize),
                    size = Size(cellSize.toFloat(), cellSize.toFloat())
                );

                isBlack = !isBlack;
            }

            isBlack = !isBlack;
        }
    }
}