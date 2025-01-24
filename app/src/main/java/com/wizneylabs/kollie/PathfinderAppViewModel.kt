package com.wizneylabs.kollie

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wizneylabs.kollie.input.InputManager
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

    val input = InputManager();

    val Width: Int
        get() = _width;

    val Height: Int
        get() = _height

    init {

        maze = Maze(_width, _height);
        maze.generateDrunkenCrawl(horizontalWalks, verticalWalks);
    }
}