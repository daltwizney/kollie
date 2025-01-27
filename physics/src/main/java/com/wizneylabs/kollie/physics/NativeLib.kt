package com.wizneylabs.kollie.physics

class NativeLib {

    /**
     * A native method that is implemented by the 'physics' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'physics' library on application startup.
        init {
            System.loadLibrary("physics")
        }
    }
}