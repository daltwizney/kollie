package com.wizneylabs.kollie.core

class Entity(val scene: Scene,
             private var _name: String,
             val _id: Int) {

    val Name: String
        get() = _name;

    val Components: List<Component>
        get() {
            return _components.values.map { container -> container.component };
        }

    private val _components = mutableMapOf<String, ComponentContainer>();

    private val _usedComponentIDs = hashSetOf<Int>();

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

    fun <T: Component> AddComponent(componentFactory: () -> T, componentID: String? = null): T {

        val component = componentFactory();

        val container = ComponentContainer(this, component,
            _getNextAvailableComponentId());

        val componentTypeName = componentID ?: component::class.simpleName;

        if (componentTypeName == null)
        {
            throw RuntimeException("failed to get component type name!");
        }

        _components[componentTypeName] = container;

        this.scene.onComponentAdded(container);

        return component;
    }

    fun <T: Component> GetComponent(typeName: String?): T? {

        if (typeName == null || !_components.contains(typeName))
        {
            return null;
        }

        @Suppress("UNCHECKED_CAST")
        return _components[typeName]?.component as T;
    }

    private fun _getNextAvailableComponentId(): Int {

        if (_usedComponentIDs.size == Int.MAX_VALUE)
        {
            return -1;
        }

        // only try up to Int.MAX_VALUE times
        for (i in 0..Int.MAX_VALUE)
        {
            val id = (0..Int.MAX_VALUE).random();

            if (!_usedComponentIDs.contains(id))
            {
                _usedComponentIDs.add(id);
                return id;
            }
        }

        return -1;
    }

    fun ReleaseComponentId(id: Int) {

        _usedComponentIDs.remove(id);
    }

    fun RemoveComponent(component: ComponentContainer) {

        // TODO: need component IDs so we can find
        // the right one to remove!

        // component IDs can probably just be in
        // integer and don't have to be unique
        // across entities, so long as entities
        // all have unique IDs in each scene
    }
}