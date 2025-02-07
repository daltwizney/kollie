package com.wizneylabs.kollie

class RenderingEngine {

    companion object {
        init {
            KollieLoader // ensure library is loaded!
        }

        external fun resize(width: Int, height: Int);

        external fun clearColorBuffer();

        external fun isContextValid(): Boolean;
    }
}