package com.wizneylabs.kollie.collie

class FullScreenQuad {

    companion object {
        init {
            CollieLoader
        }
    }

    private var _nativeHandle: Long;

    init {
        _nativeHandle = _create();
    }

    fun destroy() {

        if (_nativeHandle > 0)
        {
            _destroy(_nativeHandle);
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
    private external fun _destroy(ptr: Long);

    private external fun _initBuffers(ptr: Long);

    private external fun _draw(ptr: Long);
}