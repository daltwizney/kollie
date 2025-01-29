package com.wizneylabs.kollie.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wizneylabs.kollie.core.Scene
import com.wizneylabs.kollie.pathfinder.GridRenderer
import com.wizneylabs.kollie.viewmodels.KollieGameViewModel

@Composable
fun KollieCanvas(viewModel: KollieGameViewModel) {

    LaunchedEffect(Unit) {

        while (true) {

            withFrameNanos { timeNanos ->

                // Your update code here
                viewModel.updateGame(timeNanos / 1_000_000_000f);
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Canvas(modifier = Modifier
            .padding(vertical = 20.dp)
            .fillMaxSize()
            .align(Alignment.Center)
            .pointerInput(Unit) {
                detectTapGestures (
                    onTap = { offset -> viewModel.Game.Input.handleTap(offset )},
                    onDoubleTap = { offset  -> viewModel.Game.Input.handleDoubleTap(offset) },
                    onPress = { offset -> viewModel.Game.Input.handlePress(offset) },
                    onLongPress = { offset -> viewModel.Game.Input.handleLongPress(offset) }
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset -> viewModel.Game.Input.handleDragStart(offset) },
                    onDrag = { change, dragAmount -> viewModel.Game.Input.handleDrag(change, dragAmount) },
                    onDragEnd = { viewModel.Game.Input.handleDragEnd() },
                    onDragCancel = { viewModel.Game.Input.handleDragCanceled() }
                )
            }
        ) {
            val frameCount = viewModel.frameCounter.value;

            val game = viewModel.Game;

            val gridRenderers = game.CurrentScene.GridRenderers;

            // TODO: need to sort gridRenderers into a priority queue, based on depth

            gridRenderers.forEach({ gridRendererContainer ->

                val gridRenderer = gridRendererContainer.component as GridRenderer;

                val rows = gridRenderer.rows;
                val columns = gridRenderer.columns;
                val cellSize = gridRenderer.cellSize;

                if (gridRenderer.IsInitialized)
                {
                    for (i in 0..rows - 1)
                    {
                        for (j in 0..columns - 1)
                        {
                            val cellColor = gridRenderer.getColor(i, j);

                            drawRect(color = cellColor,
                                topLeft = Offset(
                                    j * 1.0f * cellSize,
                                    i * 1.0f * cellSize),
                                size = Size(cellSize.toFloat(), cellSize.toFloat())
                            );
                        }
                    }
                }
            });
        }

        DebugUI(viewModel);
    }
}