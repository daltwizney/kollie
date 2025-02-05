package com.wizneylabs.kollie.collie

class GridCollisionQueryInput() {

    var cellSize: Int = -1;

    var pointX: Int = -1;
    var pointY: Int = -1;
}

class GridCollisionQueryOutput() {

    var row: Int = -1;
    var column: Int = -1;
}

object KollieLoader {

    init {

        System.loadLibrary("collie");
    }
}

class ShaderProgram {

    companion object {
        init{
            KollieLoader // ensure library is loaded!
        }
    }
}

class RenderingEngine {

    companion object {
        init {
            KollieLoader // ensure library is loaded!
        }
    }

    external fun stringFromJNI(): String

    external fun init();

    external fun drawFullScreenQuad(shaderProgramID: Long);
    external fun drawGrid();

    external fun resize(width: Int, height: Int);
    external fun destroy();

    external fun compileShader(vertexShaderSrc: String, fragmentShaderSrc: String): Long;
}