package com.wizneylabs.kollie.core

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.Log

import com.wizneylabs.kollie.KollieLoader


class Canvas(private val context: Context) : GLSurfaceView(context) {

    val TAG = Canvas::class.simpleName;

    private val renderer = CanvasRenderer();

    init {

        KollieLoader // ensures library loaded

        // TODO: check for opengl es support!

        setEGLContextClientVersion(3)

        setEGLConfigChooser(8, 8, 8, 8, 16, 0)

        // load shader source
        val vertexShaderSource = this.loadShaderFromAssets("shaders/2d_passthrough.vert");
        val fragmentShaderSource = this.loadShaderFromAssets("shaders/circle.frag");

        // compile & use shader program
        renderer.fullScreenShader?.vertexShaderSource = vertexShaderSource;
        renderer.fullScreenShader?.fragmentShaderSource = fragmentShaderSource;

        // finish setting up surface view
        setRenderer(renderer);

        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY;
    }

    fun loadShaderFromAssets(fileName: String): String {

        var shaderCode = "";

        try {
            shaderCode = context.assets.open(fileName)
                .bufferedReader().use { it.readText() };
        }
        catch (e: Exception) {

            Log.e(TAG, "failed to load shader: ${fileName}");
        }

        return shaderCode;
    }

    override fun onDetachedFromWindow() {

        renderer.destroy()

        super.onDetachedFromWindow()
    }
}