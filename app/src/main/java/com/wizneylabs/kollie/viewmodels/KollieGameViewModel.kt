package com.wizneylabs.kollie.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wizneylabs.kollie.input.InputManager
import com.wizneylabs.kollie.pathfinder.Maze
import com.wizneylabs.kollie.utils.SlidingWindow

import com.wizneylabs.kollie.physics.NativeLib

class KollieGameViewModelFactory(
    private val screenWidth: Int,
    private val screenHeight: Int,
    private val cellSize: Int,
    private val horizontalWalks: Int = 10,
    private val verticalWalks: Int = 10
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return KollieGameViewModel(
            screenWidth, screenHeight, cellSize, horizontalWalks, verticalWalks
        ) as T;
    }
}

class KollieGameViewModel(

    screenWidth: Int,
    screenHeight: Int,
    cellSize: Int = 100,
    horizontalWalks: Int = 10,
    verticalWalks: Int = 10

) : ViewModel() {

    /**
     *  General game data
     */

    val TAG = KollieGameViewModel::class.simpleName;

    val input = InputManager();

    var frameCounter = mutableStateOf(0L);

    private var _lastFrameTime = 0.0f; // seconds

    var _time = 0.0f; // seconds
    var _deltaTime = 0.0f; // seconds

    var fps = mutableStateOf(0.0f);
    var frameTime = mutableStateOf(0.0f);

    private var _frameBuffer = SlidingWindow<Float>(120);

    private var _debug = true;

    /**
     *  Maze-specific data
     */

    val maze: Maze;

    // number of rows and columns for maze is based on screen 'landscape' orientation!
    val rows = screenHeight / cellSize;
    val columns = screenWidth / cellSize;

    val cellSize = cellSize;

    val screenHeight = screenHeight;
    val screenWidth = screenWidth;

    val horizontalWalks = horizontalWalks;
    val verticalWalks = verticalWalks;

    init {

        maze = Maze(columns, rows);
        maze.generateDrunkenCrawl(horizontalWalks, verticalWalks);

        val lib = NativeLib();
        Log.d("NativeTest", lib.stringFromJNI());

        this.input.onTap.add(this::handleTapInput);
    }

    fun handleTapInput(offset: Offset) {

//        Log.d(TAG + "TapInput", "tap offset: ${offset}");

        val row = (offset.y / cellSize).toInt();
        val column = (offset.x / cellSize).toInt();

        Log.d(TAG + "TapInput", "clicked cell: (${row}, ${column})");
    }

    fun updateGame(timeSeconds: Float) {

        _deltaTime = timeSeconds - _lastFrameTime;

        _time = timeSeconds;
        _lastFrameTime = timeSeconds;

        frameCounter.value++;

        if (_debug) {

            _frameBuffer.add(this._time);

            if (frameCounter.value % 60L == 0L) {

                fps.value = _frameBuffer.maxSize / (_frameBuffer.last() - _frameBuffer.first());
                frameTime.value = 1.0f/fps.value;
            }
        }
    }

    fun setDebugEnabled(enabled: Boolean) {

        if (enabled == _debug)
        {
            return;
        }

        _debug = enabled;
    }
}