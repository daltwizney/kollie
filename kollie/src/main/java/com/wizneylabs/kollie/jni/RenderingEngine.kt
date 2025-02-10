package com.wizneylabs.kollie

class RenderingEngine {

    companion object {

        private var _loaded = false;

        fun initialize() {

            if (!_loaded)
            {
                System.loadLibrary("kollie");

                _init();

                _loaded = true;
            }
        }

        external fun resize(width: Int, height: Int);

        external fun clearColorBuffer();

        external fun isContextValid(): Boolean;

        private external fun _init();
    }
}