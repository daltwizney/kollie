package com.wizneylabs.kollie.core

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.Log

class Canvas(private val context: Context) : GLSurfaceView(context) {

    val TAG = Canvas::class.simpleName;

    private val renderer = CanvasRenderer();

    init {

        // TODO: check for opengl es support!

        setEGLContextClientVersion(3)

        setEGLConfigChooser(8, 8, 8, 8, 16, 0)

        _loadShaders();

        // finish setting up surface view
        setRenderer(renderer);

        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY;
    }

    private fun _loadShaders() {

        // load full screen quad shader source
        var vertexShaderSource = this._loadShaderFromAssets("shaders/circle.vert");
        var fragmentShaderSource = this._loadShaderFromAssets("shaders/circle.frag");

        renderer.shaderSources["fullScreenQuad"] = Pair(vertexShaderSource, fragmentShaderSource);

        // load grid shader source
        vertexShaderSource = this._loadShaderFromAssets("shaders/grid2D.vert");
        fragmentShaderSource = this._loadShaderFromAssets("shaders/grid2D.frag");

        renderer.shaderSources["grid2D"] = Pair(vertexShaderSource, fragmentShaderSource);
    }

    private fun _loadShaderFromAssets(fileName: String): String {

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