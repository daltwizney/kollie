package com.wizneylabs.kollie.core

class ComponentContainer(val entity: Entity,
                         val component: Component,
                              private val _id: Int) {

    init {

    }

    val id: Int
        get() = _id;

    fun Awake() {

    }

    fun Start() {

    }

    fun Update(t: Float, dt: Float) {

    }
}