package com.wizneylabs.kollie

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wizneylabs.kollie.input.InputManager
import com.wizneylabs.kollie.pathfinder.Maze
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    var frameCounter = mutableStateOf(0);

    suspend fun updateGame() {

        delay(16);

        frameCounter.value++;
    }

    private var _gameLoopJob: Job? = null;

    init {

        maze = Maze(_width, _height);
        maze.generateDrunkenCrawl(horizontalWalks, verticalWalks);

        this.startGameLoop();
    }

    fun startGameLoop() {

        if (_gameLoopJob == null)
        {
            _gameLoopJob = viewModelScope.launch {

                // TODO: this is wrong - you want this to be lifecycle aware!

                while (true) {

                    updateGame();
                }
            }
        }
    }

    fun stopGameLoop() {

        if (_gameLoopJob != null)
        {
            _gameLoopJob?.cancel();
            _gameLoopJob = null;
        }
    }

    fun handleLifecycleEvent(event: Lifecycle.Event) {

        // this is how the viewmodel can tell what the activity lifecycle
        // currently is...

        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                // Initialize resources
            }
            Lifecycle.Event.ON_START -> {
                // Become visible
            }
            Lifecycle.Event.ON_RESUME -> {
                // Gain focus, start animations/updates
                this.startGameLoop();
            }
            Lifecycle.Event.ON_PAUSE -> {
                // Lose focus, pause animations/updates
                this.stopGameLoop();
            }
            Lifecycle.Event.ON_STOP -> {
                // No longer visible
            }
            Lifecycle.Event.ON_DESTROY -> {
                // Clean up resources
            }
            else -> {}
        }
    }
}