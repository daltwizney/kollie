package com.wizneylabs.kollie.core

import com.wizneylabs.kollie.RenderingEngine
import com.wizneylabs.kollie.input.InputManager

class App(screenWidth: Int, screenHeight: Int, initialScene: Scene? = null) {

    val CurrentScene: Scene
        get() = _currentScene;

    val Input: InputManager
        get() = _input;

    val Renderer: RenderingEngine
        get() = _renderer;

    val ScreenWidth: Int
        get() = _screenWidth;

    val ScreenHeight: Int
        get() = _screenHeight

    private val _screenWidth: Int = screenWidth;
    private val _screenHeight: Int = screenHeight;

    private val _input: InputManager = InputManager();
    private val _renderer: RenderingEngine = RenderingEngine();
    private var _currentScene: Scene;

    init {

        _currentScene = initialScene ?: Scene();

        _currentScene.Initialize(this);

        _currentScene.Start();
    }

    fun Tick(t: Float, dt: Float) {

        _currentScene.Tick(t, dt);
    }
}