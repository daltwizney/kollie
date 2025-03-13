package com.wizneylabs.kollie.core

import android.content.Context
import android.opengl.GLSurfaceView
import android.opengl.GLSurfaceView.EGLConfigChooser
import android.util.Log
import com.google.ar.core.Session
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLDisplay

internal class MultisampleConfigChooser : EGLConfigChooser {
    override fun chooseConfig(egl: EGL10, display: EGLDisplay?): EGLConfig? {
        // Define the configuration attributes for OpenGL ES 3.1
        val configSpec = intArrayOf(
            EGL10.EGL_RED_SIZE, 8,
            EGL10.EGL_GREEN_SIZE, 8,
            EGL10.EGL_BLUE_SIZE, 8,
            EGL10.EGL_ALPHA_SIZE, 8,
            EGL10.EGL_DEPTH_SIZE, 16,
            EGL10.EGL_RENDERABLE_TYPE, 0x40,  // EGL_OPENGL_ES3_BIT
            EGL10.EGL_SAMPLE_BUFFERS, 1,  // Enable sample buffers
            EGL10.EGL_SAMPLES, 4,  // 4x MSAA (can try 2 or 8)
            EGL10.EGL_NONE
        )

        val configs: Array<EGLConfig?> = arrayOfNulls<EGLConfig>(1)
        val numConfigs = IntArray(1)

        // Try to find a config with MSAA
        val success = egl.eglChooseConfig(display, configSpec, configs, 1, numConfigs)
        if (!success || numConfigs[0] <= 0) {
            Log.w("GLSurfaceView", "No MSAA config found, falling back to non-MSAA config")
            // Fallback config without MSAA, based on your original working setup
            val fallbackSpec = intArrayOf(
                EGL10.EGL_RED_SIZE, 8,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE, 8,
                EGL10.EGL_ALPHA_SIZE, 8,
                EGL10.EGL_DEPTH_SIZE, 16,
                EGL10.EGL_STENCIL_SIZE, 0,
                EGL10.EGL_RENDERABLE_TYPE, 0x40, // EGL_OPENGL_ES3_BIT
                EGL10.EGL_NONE
            )
            egl.eglChooseConfig(display, fallbackSpec, configs, 1, numConfigs)
            if (!success || numConfigs[0] <= 0) {
                throw RuntimeException("No suitable EGL config found")
            }
        }

        return configs[0]!!
    }
}

class Canvas(private val context: Context,
    private val _arSession: Session
) : GLSurfaceView(context) {

    val TAG = Canvas::class.simpleName;

    private val renderer = CanvasRenderer(_arSession);

    init {

        // TODO: check for opengl es support!

        setEGLContextClientVersion(3)

        setEGLConfigChooser(MultisampleConfigChooser());

//        setEGLConfigChooser(
//            8, 8, 8, 8,
//            16,
//            0);

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

        // load line shader source
        vertexShaderSource = this._loadShaderFromAssets("shaders/line2D.vert");
        fragmentShaderSource = this._loadShaderFromAssets("shaders/line2D.frag");

        renderer.shaderSources["line2D"] = Pair(vertexShaderSource, fragmentShaderSource);

        // load cube shader source
        vertexShaderSource = this._loadShaderFromAssets("shaders/cube/cube.vert");
        fragmentShaderSource = this._loadShaderFromAssets("shaders/cube/cube.frag");

        renderer.shaderSources["cube"] = Pair(vertexShaderSource, fragmentShaderSource);

        // load phong shader resource
        vertexShaderSource = this._loadShaderFromAssets("shaders/standard/phong.vert");
        fragmentShaderSource = this._loadShaderFromAssets("shaders/standard/phong.frag");

        renderer.shaderSources["phong"] = Pair(vertexShaderSource, fragmentShaderSource);
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