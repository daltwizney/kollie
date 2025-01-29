package com.wizneylabs.kollie.core

import com.wizneylabs.kollie.pathfinder.GridRenderer

open class Scene {

    private val _entities = mutableListOf<Entity>();

    private var _nextAvailableEntityId = 0;

    /**
     *  Rendering - scene needs to keep track of different types of renderers so they can
     *  be drawn differently in the main scene rendering composable
     */

    private val _gridRenderers = mutableListOf<ComponentContainer>();

    open fun Update(t: Float, dt: Float) {

        this._entities.forEach({ e ->

            e.Update(t, dt);
        });
    }

    fun AddEntity(name: String) {

        this._entities.add(Entity(this, name, _nextAvailableEntityId++));
    }

    fun onComponentAdded(container: ComponentContainer) {

        if (container.component is GridRenderer) {
            _gridRenderers.add(container);
        }
    }

    fun onComponentRemoved(container: ComponentContainer) {

        // NOTE: component must not be destroyed yet here,
        // as we need to be able to remove it's reference
        // from our internal lists! removal and destruction
        // of components need to be separate parts of the
        // component lifecycle!

        if (container.component is GridRenderer)
        {
            // TODO: you probably need to add component IDs so you can
            // find the component by ID and remove it!
        }
    }
}