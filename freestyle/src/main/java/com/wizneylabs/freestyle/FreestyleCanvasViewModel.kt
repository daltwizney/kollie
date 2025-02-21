package com.wizneylabs.freestyle

import android.app.Application
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.joml.Vector2f
import kotlin.math.sin

class Transform2D() {

    val position = Vector2f(0.0f, 0.0f);
    val rotation = 0.0f;
}

data class FreestyleCircle(
    var radius: Float = 100.0f,
    var color: Color = Color.Magenta,
    val transform: Transform2D = Transform2D()) {
}

class FreestyleCanvasViewModel(private val application: Application)
    : AndroidViewModel(application) {

    val TAG = FreestyleCanvasViewModel::class.simpleName;

    var t = 0.0f;
    var dt = 0.0f;

    var previousFrameTimeNanos = 0L;

    private val _circle = MutableStateFlow<FreestyleCircle>(FreestyleCircle());

    val circleData: StateFlow<FreestyleCircle> = _circle.asStateFlow();

    init {

        // TODO: this position is going to come from wherever the user taps on screen
        _circle.value.transform.position.x = 400f;
        _circle.value.transform.position.y = 400f;
    }

    private fun _updateTime(frameTimeNanos: Long) {

        t = frameTimeNanos / 1_000_000_000f;

        if (previousFrameTimeNanos != 0L)
        {
            val dtNanos = frameTimeNanos - previousFrameTimeNanos;

            dt = dtNanos / 1_000_000_000f;
        }

        previousFrameTimeNanos = frameTimeNanos;

//        Log.d(TAG, "t = $t, dt = $dt");
    }

    fun update(frameTimeNanos: Long) {

        _updateTime(frameTimeNanos);

        // animate circle
        val newRadius = 100f + (sin(t) * 50f);

        Log.d(TAG, "newRadius = $newRadius");

        _circle.value = _circle.value.copy(radius = newRadius);
    }
}