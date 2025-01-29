package com.wizneylabs.kollie.core

class ComponentContainer(entity: Entity,
                         component: Component,
                         id: Int) {

    val component: Component
        get() = _component;

    val awake: Boolean
        get() = _awake;

    val started: Boolean
        get() = _started;

    private val _entity: Entity = entity;
    private val _component: Component = component;
    private val _id: Int = id;

    private var _awake = false;
    private var _started = false;

    init {

    }

    val id: Int
        get() = _id;

    fun Awake() {

        if (!_awake && !_started) {
            component.Awake();
            _awake = true;
        }
    }

    fun Start() {

        if (_awake && !_started)
        {
            component.Start();
            _started = true;
        }
    }

    fun Update(t: Float, dt: Float) {

        component.Update(t, dt);
    }
}