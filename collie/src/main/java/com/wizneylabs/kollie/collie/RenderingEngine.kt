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

    /**
     * A native method that is implemented by the 'physics' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    external fun gridCollisionQuery(input: GridCollisionQueryInput)
        : GridCollisionQueryOutput;

    external fun nativeInit(surface: Surface);
    external fun nativeRender();
    external fun nativeDestroy();

    companion object {
        // Used to load the 'physics' library on application startup.
        init {
            System.loadLibrary("collie")
        }
    }
}