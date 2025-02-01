package com.wizneylabs.kollie.collie

import android.view.Surface

class GridCollisionQueryInput() {

    var cellSize: Int = -1;

    var pointX: Int = -1;
    var pointY: Int = -1;
}

class GridCollisionQueryOutput() {

    var row: Int = -1;
    var column: Int = -1;
}

class RenderingEngine {

    external fun stringFromJNI(): String

    external fun init();
    external fun draw();
    external fun resize(width: Int, height: Int);
    external fun destroy();

    companion object {

        // Used to load the 'collie' library on application startup.
        init {
            System.loadLibrary("collie")
        }
    }
}