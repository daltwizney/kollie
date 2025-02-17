package com.wizneylabs.kollie

import com.wizneylabs.kollie.jni.Camera2D
import com.wizneylabs.kollie.jni.PerspectiveCamera

class ShaderProgram {

    val nativeHandle: Long
        get() = _nativeHandle

    var vertexShaderSource: String = "";
    var fragmentShaderSource: String = "";

    private var _nativeHandle: Long = 0;

    init {

        _nativeHandle = _create();
    }

    fun compile() { _compile(_nativeHandle, vertexShaderSource, fragmentShaderSource) }

    fun canUse(): Boolean { return _canUse(_nativeHandle) }
    fun use() { _use(_nativeHandle) }

    fun destroy(freeGLResources: Boolean = true) {

        if (_nativeHandle > 0)
        {
            _destroy(_nativeHandle, freeGLResources);
            _nativeHandle = -1;
        }
    }

    fun setUniform2f(name: String, x: Float, y: Float) {
        _setUniform2f(_nativeHandle, name, x, y);
    }

    fun updateViewMatrix(camera: PerspectiveCamera) {

        _updateViewMatrix(_nativeHandle, camera.nativeHandle);
    }

    fun updateProjectionMatrix(camera: PerspectiveCamera) {

        _updateProjectionMatrix(_nativeHandle, camera.nativeHandle);
    }

    fun updateViewMatrix2D(camera: Camera2D) {

        _updateViewMatrix2D(_nativeHandle, camera.nativeHandle);
    }

    fun updateProjectionMatrix2D(camera: Camera2D,
                                 screenWidth: Int,
                                 screenHeight: Int) {

        _updateProjectionMatrix2D(_nativeHandle, camera.nativeHandle);
    }

    private external fun _create(): Long;
    private external fun _destroy(ptr: Long, freeGLResources: Boolean);

    private external fun _canUse(ptr: Long): Boolean;
    private external fun _use(ptr: Long);
    private external fun _compile(ptr: Long,
                                  vertexShaderSource: String,
                                  fragmentShaderSource: String);

    // TODO: need APIs for passing model matrix

    private external fun _updateViewMatrix(ptr: Long, cameraPtr: Long);
    private external fun _updateProjectionMatrix(ptr: Long, cameraPtr: Long);

    private external fun _updateViewMatrix2D(ptr: Long, camera2DPtr: Long);
    private external fun _updateProjectionMatrix2D(ptr: Long, camera2DPtr: Long);

    private external fun _setUniform2f(ptr: Long, name: String, x: Float, y: Float);
}