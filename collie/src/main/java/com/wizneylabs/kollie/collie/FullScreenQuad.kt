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

    fun initialize() {
        _init(_nativeHandle);
    }

    fun draw() {

        _draw(_nativeHandle);
    }

    private external fun _create(): Long;

    // TODO: call this something other than _init() b.c. of the
    // meaning init() already has in kotlin... ultimately,
    // we want to buffer vertex data, so maybe name it accordingly
    private external fun _init(ptr: Long);
    
    private external fun _draw(ptr: Long);
}