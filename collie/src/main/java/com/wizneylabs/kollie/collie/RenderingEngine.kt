package com.wizneylabs.kollie.collie

class RenderingEngine {

    companion object {
        init {
            CollieLoader // ensure library is loaded!
        }

        external fun resize(width: Int, height: Int);

        external fun clearColorBuffer();

        external fun isContextValid(): Boolean;
    }
}