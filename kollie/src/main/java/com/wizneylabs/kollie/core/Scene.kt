package com.wizneylabs.kollie.core

import android.util.Log
import com.wizneylabs.kollie.pathfinder.GridRenderer

open class Scene() {

    // TODO: we don't want this to be nullable!  create a SceneContainer
    // so user code doesn't know about implementation details of engine!
    val Game: Game?
        get() = _game;

    private var _game: Game? = null;

    private val _entities = mutableListOf<Entity>();

    private val _usedEntityIDs = hashSetOf<Int>();

    /**
     *  Rendering - scene needs to keep track of different types of renderers so they can
     *  be drawn differently in the main scene rendering composable
     */

    val GridRenderers: MutableList<ComponentContainer>
        get() = _gridRenderers;

    private val _gridRenderers = mutableListOf<ComponentContainer>();

    fun Initialize(game: Game) {

        if (_game != null)
        {
            throw RuntimeException("scene is already initialized!");
        }

        _game = game;
    }

    open fun Start() {

    }

    fun Tick(t: Float, dt: Float) {

        _entities.forEach({ entity ->
            entity.Update(t, dt);
        })
    }

    fun AddEntity(name: String): Entity {

        val entity = Entity(this, name, GetNextAvailableEntityId());

        this._entities.add(entity);

        return entity;
    }

    fun GetNextAvailableEntityId(): Int {

        if (_usedEntityIDs.size == Int.MAX_VALUE)
        {
            return -1;
        }

        // only try up to Int.MAX_VALUE times
        for (i in 0..Int.MAX_VALUE)
        {
            val id = (0..Int.MAX_VALUE).random();

            if (!_usedEntityIDs.contains(id))
            {
                _usedEntityIDs.add(id);
                return id;
            }
        }

        return -1;
    }

    fun <T: Component> FindComponentByType(typeName: String?): T? {

        if (typeName == null)
        {
            return null;
        }

        _entities.forEach({ entity ->

            val componentContainers = entity.Components.values;

            componentContainers.forEach({ container ->

                if (container.component::class.simpleName == typeName)
                {
                    @Suppress("UNCHECKED_CAST")
                    return container.component as T;
                }
            });
        });

        return null;
    }

    fun ReleaseComponentId(id: Int) {

        _usedEntityIDs.remove(id);
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