package com.wizneylabs.kollie.collie

class ShaderProgram {

    companion object {
        init{

            CollieLoader // ensure library is loaded!
        }
    }

    var vertexShaderSource: String = "";
    var fragmentShaderSource: String = "";

    private var _nativeHandle: Long;

    init {

        _nativeHandle = _create();
    }

    fun compile() { _compile(_nativeHandle) }

    fun canUse(): Boolean { return _canUse(_nativeHandle) }
    fun use() { _use(_nativeHandle) }

    fun destroy() {

        if (_nativeHandle > 0)
        {
            _destroy(_nativeHandle);
            _nativeHandle = -1;
        }
    }

    private external fun _create(): Long;
    private external fun _destroy(ptr: Long);

    private external fun _canUse(ptr: Long): Boolean;
    private external fun _use(ptr: Long);
    private external fun _compile(ptr: Long);
}