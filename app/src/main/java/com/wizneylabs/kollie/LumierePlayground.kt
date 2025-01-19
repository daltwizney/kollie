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

//    val shader = remember {
//        RuntimeShader("""
//            uniform float2 resolution;
//            uniform float time;
//
//            half4 main(float2 coord) {
//                float2 uv = coord/resolution.xy;
//                float3 color = float3(
//                    sin(uv.x * 6.28 + time) * 0.5 + 0.5,
//                    sin(uv.y * 6.28 + time) * 0.5 + 0.5,
//                    sin((uv.x + uv.y) * 6.28 + time) * 0.5 + 0.5
//                );
//                return half4(color, 1.0);
//            }
//        """.trimIndent())
//    }

    val shader = remember {
//        RuntimeShader(ShaderLoader(context).loadShader("gradient.agsl"))
//        RuntimeShader(ShaderLoader(context).loadShader("circle.agsl"))

        try {
            RuntimeShader(ShaderLoader(context).loadShader("emissiveCircle.agsl"))
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

            shader.setFloatUniform(
                "resolution",
                size.width,
                size.height
            )
//        shader.setFloatUniform("time", time)
//            shader.setFloatUniform("radius", 0.3f);

            drawRect(
                brush = ShaderBrush(shader),
                size = size
            )
        }
    }
}
