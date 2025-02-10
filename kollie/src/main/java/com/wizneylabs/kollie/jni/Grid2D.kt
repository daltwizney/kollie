package com.wizneylabs.kollie.jni

class Grid2D {

    private var _nativeHandle: Long;

    init {

        _nativeHandle = _create();
    }

    fun draw() {
        _draw(_nativeHandle);
    }

    fun destroy(freeGLResources: Boolean = true) {
        _destroy(_nativeHandle, freeGLResources);
    }

    private external fun _create(): Long;
    private external fun _destroy(ptr: Long, freeGLResources: Boolean);

    private external fun _draw(ptr: Long);
}