package com.wizneylabs.kollie.core

import com.wizneylabs.kollie.input.InputManager

class Game(initialScene: Scene? = null) {

    val CurrentScene: Scene
        get() = _currentScene;

    val Input: InputManager
        get() = _input;

    private val _input: InputManager = InputManager();

    private var _currentScene: Scene;

    init {

        _currentScene = initialScene ?: Scene();
    }

    fun Update(t: Float, dt: Float) {

        _currentScene.Update(t, dt);
    }
}