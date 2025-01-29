package com.wizneylabs.examples.components

import android.util.Log
import com.wizneylabs.kollie.core.Component

class BasicComponentExample: Component() {

    val TAG = BasicComponentExample::class.simpleName;

    var name = "unnamed";

    override fun Awake() {
        super.Awake();

        Log.d(TAG + "Awake", "basic component awakened!");
    }

    override fun Start() {
        super.Start();

        Log.d(TAG + "Start", "basic component started!");
    }

    override fun Update(t: Float, dt: Float) {
        super.Update(t, dt);

        Log.d(TAG + "Update", "update() called on basic component named $name");
    }
}