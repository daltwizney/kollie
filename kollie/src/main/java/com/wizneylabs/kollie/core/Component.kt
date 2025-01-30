package com.wizneylabs.kollie.core

abstract class Component(val entity: Entity) {

    val awake: Boolean
        get() = _awake;

    val started: Boolean
        get() = _started;

    private var _awake = false;
    private var _started = false;

    open fun Awake() {

        if (!_awake && !_started) {
            _awake = true;
        }
    }

    open fun Start() {

        if (_awake && !_started)
        {
            _started = true;
        }
    }

    open fun Update(t: Float, dt: Float) {

    }
}