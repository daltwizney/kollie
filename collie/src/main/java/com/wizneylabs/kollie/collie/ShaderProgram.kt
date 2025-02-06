package com.wizneylabs.kollie.collie

class ShaderProgram {

    companion object {
        init{

            CollieLoader // ensure library is loaded!
        }
    }

    var vertexShaderSource: String = "";
    var fragmentShaderSource: String = "";

    external fun compile();

    external fun canUse();
    external fun use();
}