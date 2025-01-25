package com.wizneylabs.kollie.core

class Entity(val scene: Scene,
             private var _name: String,
             val _id: Int) {

    private val _components = mutableListOf<Component>();

    fun Update(t: Float, dt: Float) {

        this._components.forEach({ c ->
            c.Update(t, dt);
        });
    }

    inline fun <reified T: Component> AddComponent(): T {

        // TODO: pass a unique component ID!
        val component: Component = T::class.java.getDeclaredConstructor().newInstance(this, 0);

        this.AddExistingComponent(component);

        return component as T;
    }

    fun AddExistingComponent(component: Component) {

        // TODO: check if component ID is already reserved, if so throw an error!

        _components.add(component);

        this.scene.onComponentAdded(component);
    }

    fun RemoveComponent(component: Component) {

        // TODO: need component IDs so we can find
        // the right one to remove!

        // component IDs can probably just be in
        // integer and don't have to be unique
        // across entities, so long as entities
        // all have unique IDs in each scene
    }
}