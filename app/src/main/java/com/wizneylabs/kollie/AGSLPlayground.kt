package com.wizneylabs.kollie

import android.content.Context
import android.graphics.RuntimeShader
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.unit.dp

class ShaderLoader(private val context: Context) {
    fun loadShader(fileName: String): String {
        return context.resources
            .assets
            .open("shaders/$fileName")
            .bufferedReader()
            .use { it.readText() }
    }
}

@Composable
fun MyAGSLCanvas(context: Context) {

    val shader = remember {

        try {
//            RuntimeShader(ShaderLoader(context).loadShader("gradient.agsl"))
//            RuntimeShader(ShaderLoader(context).loadShader("circle.agsl"))
            RuntimeShader(ShaderLoader(context).loadShader("blending_test.agsl"));
        } catch (e: IllegalArgumentException) {
            Log.e("Shader", "Compilation failed: ${e.message}")
            null
        }
    }

    var time by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos { frameTimeNanos ->
                time = (frameTimeNanos / 1_000_000_000f)
            }
        }
    }

    if (shader != null)
    {
        Canvas(modifier = Modifier
            .padding(vertical = 20.dp)
            .fillMaxSize()
        ) {

            shader.setFloatUniform("colorA",
                floatArrayOf(1.0f, 0.0f, 0.0f, 1.0f));

            shader.setFloatUniform("colorB",
                floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f));

            shader.setFloatUniform(
                "resolution",
                size.width,
                size.height
            )

//            shader.setFloatUniform("time", time)
//            shader.setFloatUniform("radius", 0.3f);

            drawRect(
                brush = ShaderBrush(shader),
                size = size
            )
        }
    }
}
