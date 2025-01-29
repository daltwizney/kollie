package com.wizneylabs.kollie.core

open class Component(private val entity: Entity,
                     private val _id: Int) {

    init {

        throw RuntimeException("TODO: bad design - Component constructor can't have params!");
    }

    val id: Int
        get() = _id;

    // TODO: we'll need to create the components this way,
    // where the user instantiates a component object
    // and the component registers itself with the entity
    // object where it receives an ID from the entity...

    // could we use some sort of factory pattern here
    // instead of having the user create components
    // directly themselves?

    // we want to control component ID management per entity

    // unfortunately, kotlin, like java, doesn't support
    // generics very well and has issues due to type erasure...

    open fun Awake() {

    }

    open fun Start() {

    }

    open fun Update(t: Float, dt: Float) {

    }
}