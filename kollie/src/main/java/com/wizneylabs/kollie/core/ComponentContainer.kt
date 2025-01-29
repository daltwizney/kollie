package com.wizneylabs.kollie.core

class ComponentContainer(entity: Entity,
                         component: Component,
                         id: Int) {

    val component: Component
        get() = _component;

    val started: Boolean
        get() = _started;

    private val _entity: Entity = entity;
    private val _component: Component = component;
    private val _id: Int = id;

    private var _started = false;

    init {

        component.Awake();
    }

    val id: Int
        get() = _id;

    fun Start() {

        if (!_started)
        {
            component.Start();
            _started = true;
        }
    }

    fun Update(t: Float, dt: Float) {

        component.Update(t, dt);
    }
}