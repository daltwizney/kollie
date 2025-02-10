package com.wizneylabs.kollie

class FullScreenQuad {

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

    fun resize(screenWidth: Int, screenHeight: Int) {

        _resize(_nativeHandle, screenWidth, screenHeight);
    }

    fun draw() {

        _draw(_nativeHandle);
    }

    private external fun _create(): Long;
    private external fun _destroy(ptr: Long, freeGLResources: Boolean);

    private external fun _resize(ptr: Long, screenWidth: Int, screenHeight: Int);

    private external fun _draw(ptr: Long);
}