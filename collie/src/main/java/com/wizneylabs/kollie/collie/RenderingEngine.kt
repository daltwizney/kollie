package com.wizneylabs.kollie.collie

class RenderingEngine {

    companion object {
        init {
            CollieLoader // ensure library is loaded!
        }
    }

    external fun stringFromJNI(): String

    external fun init();

    external fun drawFullScreenQuad(shaderProgramID: Long);
    external fun drawGrid();

    external fun resize(width: Int, height: Int);
    external fun destroy();
}