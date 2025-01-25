package com.wizneylabs.kollie

import android.app.Application
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.wizneylabs.kollie.ui.theme.KollieTheme

import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

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

    val screenWidthPx = with (LocalDensity.current) { screenWidth.toPx() };
    val screenHeightPx = with (LocalDensity.current) { screenHeight.toPx() };

    val cellSize = 20.dp;
    val horizontalWalks = 3;
    val verticalWalks = 6;

    val cellSizePx = (with (LocalDensity.current) { cellSize.toPx() }).toInt();

    // number of rows and columns for maze is based on screen 'landscape' orientation!
    val rows = (screenHeightPx / cellSizePx).toInt();
    val columns = (screenWidthPx / cellSizePx).toInt();

    val mainViewModel = viewModel<MainViewModel>();

    Log.d("MazeRenderer", "rows = ${rows}, cols = ${columns}");

    val pathfinderAppViewModel = viewModel<PathfinderAppViewModel>(
        factory = PathfinderAppViewModelFactory(columns, rows,
            horizontalWalks, verticalWalks)
    );

    MazeRenderer(pathfinderAppViewModel, cellSizePx);
//    KollieCanvas(mainViewModel);
//    MyAGSLCanvas(context);
}

@Composable
fun DebugUI(viewModel: PathfinderAppViewModel) {

    Column(modifier = Modifier
        .offset(x = 0.dp, y = 20.dp)
        .background(Color.Black.copy(alpha = 0.7f))
        .padding(2.dp, 2.dp)
        .clearAndSetSemantics {  },
    ) {
        val fps = viewModel.fps.value;
        val frameTime = viewModel.frameTime.value * 1000;

        var fpsColor = Color.Red;

        if (fps > 58.0f)
        {
            fpsColor = Color.Green;
        }
        else if (fps > 50.0f)
        {
            fpsColor = Color.Yellow;
        }

        Text(
            text = "FPS: ${("%.1f").format(fps)}",
            modifier = Modifier,
            color = fpsColor,
        );

        Text(text = "dT: ${("%.1f").format(frameTime) }",
            modifier = Modifier,
            color = fpsColor);
    }
}

@Composable
fun MazeRenderer(viewModel: PathfinderAppViewModel,
                 cellSizePx: Int) {

    LaunchedEffect(Unit) {

        while (true) {

            withFrameNanos { timeNanos ->

                // Your update code here
                viewModel.updateGame(timeNanos / 1_000_000_000f);

//                Log.d("MazeRenderer", "frame count = ${viewModel.frameCounter.value}");
//                Log.d("MazeRenderer", "frame time = ${viewModel.frameTime}");
//                Log.d("MazeRenderer", "fps = ${viewModel.fps}");
            }
        }
    }

    Log.d("MazeRenderer", "RECOMPOSITION HAPPENED - drawing maze!");

    val context = LocalContext.current;
    val configuration = LocalConfiguration.current;

    val rows = viewModel.maze.Height;
    val columns = viewModel.maze.Width;

    var isBlack = true;

    Box(modifier = Modifier.fillMaxSize()) {

        Canvas(modifier = Modifier
            .padding(vertical = 20.dp)
            .fillMaxSize()
            .align(Alignment.Center)
            .pointerInput(Unit) {
                detectTapGestures (
                    onTap = { offset -> viewModel.input.handleTap(offset )},
                    onDoubleTap = { offset  -> viewModel.input.handleDoubleTap(offset) },
                    onPress = { offset -> viewModel.input.handlePress(offset) },
                    onLongPress = { offset -> viewModel.input.handleLongPress(offset) }
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset -> viewModel.input.handleDragStart(offset) },
                    onDrag = { change, dragAmount -> viewModel.input.handleDrag(change, dragAmount) },
                    onDragEnd = { viewModel.input.handleDragEnd() },
                    onDragCancel = { viewModel.input.handleDragCanceled() }
                )
            }
        ) {
            val frameCount = viewModel.frameCounter.value;

            for (i in 0..rows - 1)
            {
                for (j in 0..columns - 1)
                {
                    var cellColor = Color.Black;

                    if (viewModel.maze.getValue(i, j) == 1)
                    {
                        cellColor = Color.Blue;
                    }

                    drawRect(color = cellColor,
                        topLeft = Offset(
                            j * 1.0f * cellSizePx,
                            i * 1.0f * cellSizePx),
                        size = Size(cellSizePx.toFloat(), cellSizePx.toFloat())
                    );

                    isBlack = !isBlack;
                }

                isBlack = !isBlack;
            }
        }

        DebugUI(viewModel);
    }
}