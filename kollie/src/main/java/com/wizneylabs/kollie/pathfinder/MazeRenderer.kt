package com.wizneylabs.kollie.pathfinder

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.wizneylabs.kollie.core.Component

class MazeRenderer: Component() {

    val TAG = MazeRenderer::class.simpleName;

    var gridRenderer: GridRenderer? = null;
    var maze: Maze? = null;

    override fun Start() {
        super.Start();

        Log.d(TAG, "maze renderer initializing!")

        if (gridRenderer == null)
        {
            throw RuntimeException("grid renderer is null!");
        }

        if (maze == null)
        {
            throw RuntimeException("");
        }

        // set grid renderer cell colors based on maze!
        val rows = maze!!.Height;
        val columns = maze!!.Width;

        Log.d(TAG, "rows = ${rows}, columns = ${columns}");

        for (i in 0..rows - 1) {
            for (j in 0..columns - 1) {

                if (maze!!.getValue(i, j) == 1)
                {
                    gridRenderer!!.setColor(i, j, Color.Green);
                }
                else
                {
                    gridRenderer!!.setColor(i, j, Color.Red);
                }
            }
        }

        Log.d(TAG, "maze renderer initialized successfully!");
    }
}