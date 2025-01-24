package com.wizneylabs.kollie

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
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
fun MazeRenderer(viewModel: PathfinderAppViewModel,
                 cellSizePx: Int) {

    Log.d("MazeRenderer", "drawing maze!");

    val context = LocalContext.current;
    val configuration = LocalConfiguration.current;

    val rows = viewModel.maze.Height;
    val columns = viewModel.maze.Width;

    var isBlack = true;

    Canvas(modifier = Modifier
        .padding(vertical = 20.dp)
        .fillMaxSize()
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
}