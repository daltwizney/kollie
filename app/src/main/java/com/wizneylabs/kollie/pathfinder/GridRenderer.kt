package com.wizneylabs.kollie.pathfinder

import com.wizneylabs.kollie.core.Component
import com.wizneylabs.kollie.core.Entity

// TODO: this isn't good - we don't want the component to know about the entity in its
// constructor...

class GridRenderer(private val entity: Entity):
    Component(entity, 0) {

    var rows: Int = 0;
    var columns: Int = 0;

    override fun Awake() {

    }

    override fun Start() {

    }

    override fun Update(t: Float, dt: Float) {

    }
}