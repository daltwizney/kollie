package com.wizneylabs.kollie.core

import com.wizneylabs.kollie.physics.PhysicsEngine
import com.wizneylabs.kollie.input.InputManager

class Game(screenWidth: Int, screenHeight: Int, initialScene: Scene? = null) {

    val CurrentScene: Scene
        get() = _currentScene;

    val Input: InputManager
        get() = _input;

    val Physics: PhysicsEngine
        get() = _physics;

    val ScreenWidth: Int
        get() = _screenWidth;

    val ScreenHeight: Int
        get() = _screenHeight

    private val _screenWidth: Int = screenWidth;
    private val _screenHeight: Int = screenHeight;

    private val _input: InputManager = InputManager();
    private val _physics: PhysicsEngine = PhysicsEngine();
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