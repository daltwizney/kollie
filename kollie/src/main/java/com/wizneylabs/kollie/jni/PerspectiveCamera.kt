package com.wizneylabs.kollie.jni

class PerspectiveCamera(
    fov: Float = 45.0f,
    aspectRatio: Float = 1.777f,
    nearPlane: Float = 0.1f,
    farPlane: Float = 100.0f
) {

    val nativeHandle: Long
        get() = _nativeHandle;

    private val _nativeHandle: Long;

    init {

        _nativeHandle = _create(fov, aspectRatio, nearPlane, farPlane);
    }

    fun setAspectRatio(aspectRatio: Float) {

        _setAspectRatio(_nativeHandle, aspectRatio);
    }

    fun setPosition(x: Float, y: Float, z: Float) {

        _setPosition(_nativeHandle, x, y, z);
    }

    fun lookAt(x: Float, y: Float, z: Float) {

        _lookAt(_nativeHandle, x, y, z);
    }

    private external fun _create(fov: Float,
                                 aspectRatio: Float,
                                 nearPlane: Float,
                                 farPlane: Float): Long;

    private external fun _destroy(ptr: Long, freeGLResources: Boolean);
    private external fun _setAspectRatio(ptr: Long, aspectRatio: Float);
    private external fun _setPosition(ptr: Long, x: Float, y: Float, z: Float);
    private external fun _lookAt(ptr: Long, x: Float, y: Float, z: Float);
}