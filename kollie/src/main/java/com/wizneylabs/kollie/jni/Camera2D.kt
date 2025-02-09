package com.wizneylabs.kollie.jni

class Camera2D {

    val nativeHandle: Long
        get() = _nativeHandle

    private var _nativeHandle: Long;

    init {

        _nativeHandle = _create();
    }

    fun destroy(freeGLResources: Boolean = true) {

        if (_nativeHandle > 0)
        {
            _destroy(_nativeHandle, freeGLResources);
            _nativeHandle = -1;
        }
    }

    fun setPosition(x: Float, y: Float, z: Float) {
        _setPosition(_nativeHandle, x, y, z);
    }

    fun lookAt(x: Float, y: Float, z: Float) {
        _lookAt(_nativeHandle, x, y, z);
    }

    private external fun _create(): Long;
    private external fun _destroy(ptr: Long, freeGLResources: Boolean);
    private external fun _setPosition(ptr: Long, x: Float, y: Float, z: Float);
    private external fun _lookAt(ptr: Long, x: Float, y: Float, z: Float);
}