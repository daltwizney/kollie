package com.wizneylabs.examples

import android.content.Context
import android.opengl.EGLConfig
import android.opengl.GLSurfaceView
import android.util.Log

import com.wizneylabs.kollie.collie.RenderingEngine
import javax.microedition.khronos.opengles.GL10

class MyRenderer: GLSurfaceView.Renderer {

    private val nativeRenderer = RenderingEngine()

    fun setShaderSource(vertexShader: String, fragmentShader: String) {
        nativeRenderer.setShaderSource(vertexShader, fragmentShader);
    }

    override fun onSurfaceCreated(p0: GL10?, p1: javax.microedition.khronos.egl.EGLConfig?) {
        nativeRenderer.init()
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        nativeRenderer.resize(width, height)
    }

    override fun onDrawFrame(p0: GL10?) {
        nativeRenderer.draw()
    }

    fun destroy() {

        nativeRenderer.destroy();
    }
}

class MyGLSurfaceView(private val context: Context) : GLSurfaceView(context) {

    val TAG = MyGLSurfaceView::class.simpleName;

    private val renderer = MyRenderer();

    init {

        // TODO: check for opengl es support!

        setEGLContextClientVersion(3)

        setEGLConfigChooser(8, 8, 8, 8, 16, 0)

        // load shader source
        val vertexShader = this.loadShaderFromAssets("shaders/2d_passthrough.vert");
        val fragmentShader = this.loadShaderFromAssets("shaders/circle.frag");

        Log.d(TAG, "vertexShader = ${vertexShader}");
        Log.d(TAG, "fragShader = ${fragmentShader}");

        renderer.setShaderSource(vertexShader, fragmentShader);

        setRenderer(renderer);

        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY;
    }

    fun loadShaderFromAssets(fileName: String): String {

        var shaderCode = "";

        try {
            shaderCode = context.assets.open("shaders/2d_passthrough.vert")
                .bufferedReader().use { it.readText() };
        }
        catch (e: Exception) {

            Log.e(TAG, "failed to load shader: ${fileName}");
        }

        return shaderCode;
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        renderer.destroy()
    }
}