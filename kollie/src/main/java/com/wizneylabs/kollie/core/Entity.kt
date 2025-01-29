package com.wizneylabs.kollie.core

class Entity(val scene: Scene,
             private var _name: String,
             val _id: Int) {

    val Name: String
        get() = _name;

    val Components: MutableList<ComponentContainer>
        get() = _components;

    private val _components = mutableListOf<ComponentContainer>();

    private val _usedComponentIDs = hashSetOf<Int>();

    fun Update(t: Float, dt: Float) {

        this._components.forEach({ c ->

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

    inline fun <reified T: Component> AddComponent(): T {

        val component: Component = T::class.java.getDeclaredConstructor().newInstance();

        val container = ComponentContainer(this, component,
            GetNextAvailableComponentId());

        this.AddExistingComponent(container);

        return component as T;
    }

    fun GetNextAvailableComponentId(): Int {

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

    fun AddExistingComponent(component: ComponentContainer) {

        // TODO: check if component ID is already reserved, if so throw an error!

        _components.add(component);

        this.scene.onComponentAdded(component);
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