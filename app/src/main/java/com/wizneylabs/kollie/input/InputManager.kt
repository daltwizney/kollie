package com.wizneylabs.kollie.input

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange

class InputManager {

    val onTap = mutableListOf<(Offset) -> Unit>();

    /**
     *  TODO: for now, let's just expose some event-based input handling to kollie components,
     *  so we can build the pathfinder demo app w/o polling input support.
     *
     *  Add polling input support later, as per notes below:
     */

    /**
     *  TODO: we want to create a state machine here that gets updated every frame so we can support
     *  both polling and event-based input handling in kollie.
     *
     *  How Phaser handles input:
     *
     *  Phaser maintains an event queue between frames. When input events occur, they're added to
     *  this queue chronologically. During the next frame's update, Phaser processes the entire
     *  queue in order, so even brief interactions like quick clicks (mousedown + mouseup) get
     *  processed. This means your game logic can check for these events, even if they both
     *  occurred in the time between frames.
     *
     *  if both events happened between frames, Phaser will report both justDown and justUp as true
     *  in the same frame. This way your game code can detect and handle quick taps or clicks that
     *  occurred between frame updates.
     *
     * Phaser clears the input state for one-shot checks like justDown/justUp at the end of each
     * frame update. The actual input states (isDown, isUp) persist, but the "just" flags are reset
     * so they'll only trigger once per press/release event.
     *
     */

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