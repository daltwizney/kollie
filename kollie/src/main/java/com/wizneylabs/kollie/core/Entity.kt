package com.wizneylabs.kollie.core

/**
 *  Entity names and component IDs are unique within each scene.
 *  Component IDs consist of a user supplied ID, prefixed with the entity name.
 *
 *  If user doesn't supply an ID, component ID is 'EntityName' + 'Typename'
 */
class Entity(val scene: Scene,
             private var _name: String,
             val _id: Int) {

    val Name: String
        get() = _name;

    private val _components = mutableMapOf<String, Component>();

    fun Update(t: Float, dt: Float) {

        this._components.values.forEach({ c ->

            if (!c.awake)
            {
                c.Awake();
            }
            else if (!c.started)
            {
                c.Start();
            }
            else
            {
                c.Update(t, dt);
            }
        });
    }

    fun <T: Component> AddComponent(componentFactory: (Entity) -> T, id: String? = null): T {

        // TODO: verify component ID is unique!

        val component = componentFactory(this);

        var componentID = id ?: component::class.simpleName;

        if (componentID == null)
        {
            throw RuntimeException("failed to get component type name!");
        }

        componentID = this._name + componentID;

        _components[componentID] = component;

        this.scene.onComponentAdded(componentID, component);

        return component;
    }

    fun <T: Component> GetComponent(id: String?): T? {

        if (id == null || !_components.contains(this._name + id))
        {
            return null;
        }

        @Suppress("UNCHECKED_CAST")
        return _components[this._name + id] as T;
    }

    fun RemoveComponent(id: String) {

    }

    fun <T: Component> FindComponentByType(typeName: String): T? {

        _components.forEach { component ->

            if (component::class.simpleName == typeName)
            {
                @Suppress("UNCHECKED_CAST")
                return component as T;
            }
        };

        return null;
    }
}