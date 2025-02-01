package com.wizneylabs.examples

import android.content.Context
import android.opengl.EGLConfig
import android.opengl.GLSurfaceView

import com.wizneylabs.kollie.collie.RenderingEngine
import javax.microedition.khronos.opengles.GL10

class MyRenderer: GLSurfaceView.Renderer {

    private val nativeRenderer = RenderingEngine()

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

class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {

    private val renderer = MyRenderer();

    init {

        // TODO: check for opengl es support!

        setEGLContextClientVersion(3)

        setEGLConfigChooser(8, 8, 8, 8, 16, 0)

        setRenderer(renderer);

        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY;
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        renderer.destroy()
    }
}