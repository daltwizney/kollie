package com.wizneylabs.kollie

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wizneylabs.kollie.pathfinder.Maze

class PathfinderAppViewModelFactory(
    private val width: Int,
    private val height: Int,
    private val horizontalWalks: Int = 10,
    private val verticalWalks: Int = 10
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PathfinderAppViewModel(
            width, height, horizontalWalks, verticalWalks
        ) as T;
    }
}

class PathfinderAppViewModel(
    width: Int,
    height: Int,
    horizontalWalks: Int = 10,
    verticalWalks: Int = 10

) : ViewModel() {

    val TAG = PathfinderAppViewModel::class.simpleName;

    val maze: Maze;

    val _width = width;
    val _height = height;

    val Width: Int
        get() = _width;

    val Height: Int
        get() = _height

    init {

        maze = Maze(_width, _height);
        maze.generateDrunkenCrawl(horizontalWalks, verticalWalks);
    }

    fun handleTap(offset: Offset) {

        Log.d(TAG, "TAP input received with offset: ${offset}");
    }

    fun handleDoubleTap(offset: Offset) {

        Log.d(TAG, "DOUBLE TAP input received with offset: ${offset}");
    }

    fun handlePress(offset: Offset) {

        Log.d(TAG, "PRESS input received with offset: ${offset}");
    }

    fun handleLongPress(offset: Offset) {

        Log.d(TAG, "LONG PRESS input received with offset: ${offset}");
    }

    fun handleDragStart(offset: Offset) {
        Log.d(TAG, "DRAG START input received with offset: ${offset}");
    }

    fun handleDrag(change: PointerInputChange, dragAmount: Offset) {
        Log.d(TAG, "DRAG input received with dragAmount = ${dragAmount}");
    }

    fun handleDragEnd() {
        Log.d(TAG, "DRAG END received");
    }

    fun handleDragCanceled() {
        Log.d(TAG, "DRAG CANCELLED received");
    }
}