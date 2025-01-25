package com.wizneylabs.kollie

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wizneylabs.kollie.input.InputManager
import com.wizneylabs.kollie.pathfinder.Maze
import com.wizneylabs.kollie.utils.SlidingWindow

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

    var frameCounter = mutableStateOf(0L);

    var timeSeconds = 0.0f;

    var fps = mutableStateOf(0.0f);
    var frameTime = mutableStateOf(0.0f);

    private var _frameBuffer = SlidingWindow<Float>(120);

    private var _debug = true;

    fun updateGame(timeSeconds: Float) {

        this.timeSeconds = timeSeconds;
        frameCounter.value++;

        if (_debug) {

            _frameBuffer.add(this.timeSeconds);

            if (frameCounter.value % 60L == 0L) {

                fps.value = _frameBuffer.maxSize / (_frameBuffer.last() - _frameBuffer.first());
                frameTime.value = 1.0f/fps.value;
            }

            Log.d(TAG, "fps = ${fps.value}");
        }
    }

    fun setDebugEnabled(enabled: Boolean) {

        if (enabled == _debug)
        {
            return;
        }

        _debug = enabled;
    }

    init {

        maze = Maze(_width, _height);
        maze.generateDrunkenCrawl(horizontalWalks, verticalWalks);
    }

}