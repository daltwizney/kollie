package com.wizneylabs.kollie.core

class Game {

    val currentScene: Scene
        get() = _currentScene;

    private var _currentScene: Scene = Scene();

    fun Update(t: Float, dt: Float) {

        _currentScene.Update(t, dt);
    }
}