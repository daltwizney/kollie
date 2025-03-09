package com.wizneylabs.kollie.jni

import com.wizneylabs.kollie.ShaderProgram

class UvSphere {

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

    fun draw(shader: ShaderProgram, camera: PerspectiveCamera) {

        _draw(_nativeHandle, shader.nativeHandle, camera.nativeHandle);
    }

    private external fun _create(): Long;
    private external fun _destroy(ptr: Long, freeGLResources: Boolean);

    private external fun _draw(ptr: Long, shaderProgramPtr: Long, cameraPtr: Long);
}