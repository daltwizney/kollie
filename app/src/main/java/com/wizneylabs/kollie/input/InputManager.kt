package com.wizneylabs.kollie.input

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange

class InputManager {

    val TAG = InputManager::class.simpleName;

    fun handleTap(offset: Offset) {

        Log.d(TAG, "TAP input received with offset: ${offset}");
    }

    fun handleDoubleTap(offset: Offset) {

        Log.d(TAG, "DOUBLE TAP input received with offset: ${offset}");
    }

    fun handlePress(offset: Offset) {

        Log.d(TAG, "PRESS input received with offset: ${offset}");
    }

    fun handleLongPress(offset: Offset) {

        Log.d(TAG, "LONG PRESS input received with offset: ${offset}");
    }

    fun handleDragStart(offset: Offset) {
        Log.d(TAG, "DRAG START input received with offset: ${offset}");
    }

    fun handleDrag(change: PointerInputChange, dragAmount: Offset) {
        Log.d(TAG, "DRAG input received with dragAmount = ${dragAmount}");
    }

    fun handleDragEnd() {
        Log.d(TAG, "DRAG END received");
    }

    fun handleDragCanceled() {
        Log.d(TAG, "DRAG CANCELLED received");
    }
}