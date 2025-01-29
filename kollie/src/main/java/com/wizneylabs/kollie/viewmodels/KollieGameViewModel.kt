package com.wizneylabs.kollie.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wizneylabs.kollie.input.InputManager
import com.wizneylabs.kollie.pathfinder.Maze
import com.wizneylabs.kollie.physics.GridCollisionQueryInput
import com.wizneylabs.kollie.utils.SlidingWindow

import com.wizneylabs.kollie.core.Game
import com.wizneylabs.kollie.core.Scene
import com.wizneylabs.kollie.pathfinder.MazeRenderer

class KollieGameViewModelFactory(
    private val initialScene: Scene,
    private val screenWidth: Int,
    private val screenHeight: Int,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return KollieGameViewModel(initialScene,
            screenWidth, screenHeight
        ) as T;
    }
}

class KollieGameViewModel(

    initialScene: Scene,
    screenWidth: Int,
    screenHeight: Int,

) : ViewModel() {

    /**
     *  General game data
     */

    val Game: Game
        get() = _game;

    val TAG = KollieGameViewModel::class.simpleName;

    var frameCounter = mutableStateOf(0L);

    private var _lastFrameTime = 0.0f; // seconds

    var _time = 0.0f; // seconds
    var _deltaTime = 0.0f; // seconds

    var fps = mutableStateOf(0.0f);
    var frameTime = mutableStateOf(0.0f);

    private var _frameBuffer = SlidingWindow<Float>(120);

    private var _debug = true;

    private val _game = Game(screenWidth, screenHeight, initialScene);

    init {
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

        _game.Tick(_time, _deltaTime);
    }

    fun setDebugEnabled(enabled: Boolean) {

        if (enabled == _debug)
        {
            return;
        }

        _debug = enabled;
    }
}