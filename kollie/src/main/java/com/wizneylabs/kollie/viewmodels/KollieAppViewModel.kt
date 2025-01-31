package com.wizneylabs.kollie.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wizneylabs.kollie.utils.SlidingWindow

import com.wizneylabs.kollie.core.App
import com.wizneylabs.kollie.core.Scene

class KollieAppViewModelFactory(
    private val initialScene: Scene,
    private val screenWidth: Int,
    private val screenHeight: Int,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return KollieAppViewModel(initialScene,
            screenWidth, screenHeight
        ) as T;
    }
}

class KollieAppViewModel(

    initialScene: Scene,
    screenWidth: Int,
    screenHeight: Int,

) : ViewModel() {

    /**
     *  General game data
     */

    val App: App
        get() = _app;

    val TAG = KollieAppViewModel::class.simpleName;

    var frameCounter = mutableStateOf(0L);

    private var _lastFrameTime = 0.0f; // seconds

    var _time = 0.0f; // seconds
    var _deltaTime = 0.0f; // seconds

    var fps = mutableStateOf(0.0f);
    var frameTime = mutableStateOf(0.0f);

    private var _frameBuffer = SlidingWindow<Float>(120);

    private var _debug = true;

    private val _app = App(screenWidth, screenHeight, initialScene);

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

        _app.Tick(_time, _deltaTime);
    }

    fun setDebugEnabled(enabled: Boolean) {

        if (enabled == _debug)
        {
            return;
        }

        _debug = enabled;
    }
}