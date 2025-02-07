package com.wizneylabs.kollie

class ShaderProgram {

    companion object {
        init{

            KollieLoader // ensure library is loaded!
        }
    }

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

    private external fun _create(): Long;
    private external fun _destroy(ptr: Long, freeGLResources: Boolean);

    private external fun _canUse(ptr: Long): Boolean;
    private external fun _use(ptr: Long);
    private external fun _compile(ptr: Long,
                                  vertexShaderSource: String,
                                  fragmentShaderSource: String);

    private external fun _setUniform2f(ptr: Long, name: String, x: Float, y: Float);
}