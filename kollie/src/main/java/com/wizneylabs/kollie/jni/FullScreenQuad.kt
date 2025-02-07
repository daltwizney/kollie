package com.wizneylabs.kollie

class FullScreenQuad {

    companion object {
        init {
            KollieLoader
        }
    }

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

    fun initBuffers() {
        _initBuffers(_nativeHandle);
    }

    fun draw() {

        _draw(_nativeHandle);
    }

    private external fun _create(): Long;
    private external fun _destroy(ptr: Long, freeGLResources: Boolean);

    private external fun _initBuffers(ptr: Long);

    private external fun _draw(ptr: Long);
}