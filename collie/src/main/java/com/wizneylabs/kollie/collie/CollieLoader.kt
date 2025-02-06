package com.wizneylabs.kollie.collie

class CollieLoader {

    companion object {

        init {

            System.loadLibrary("collie");
        }
    }
}