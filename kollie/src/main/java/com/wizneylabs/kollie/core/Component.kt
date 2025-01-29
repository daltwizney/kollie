package com.wizneylabs.kollie.core

abstract class Component {

    open fun Awake() {}

    open fun Start() {}

    open fun Update(t: Float, dt: Float) {}
}