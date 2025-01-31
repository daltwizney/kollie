package com.wizneylabs.examples

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder

import com.wizneylabs.kollie.collie.RenderingEngine

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
class MyGLSurfaceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : GLSurfaceView(context, attrs) {

    private var renderer = RendererImpl();

    init {
        /*
         * We do not call setEGLContextClientVersion() because our native code creates its
         * own EGL context. We still need to set a renderer so that onDrawFrame is called.
         */
        setRenderer(renderer)
        // Choose continuous rendering (or RENDERMODE_WHEN_DIRTY if you prefer to request renders)
        renderMode = RENDERMODE_CONTINUOUSLY
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        super.surfaceCreated(holder)
        // Pass the Surface from the holder to native code.
        renderer.renderingEngine.nativeInit(holder.surface)
        renderer.renderingEngine.nativeRender()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        renderer.renderingEngine.nativeDestroy()
        super.surfaceDestroyed(holder)
    }

    private class RendererImpl : Renderer {

        var renderingEngine = RenderingEngine();

        override fun onSurfaceCreated(
            gl: javax.microedition.khronos.opengles.GL10?,
            config: javax.microedition.khronos.egl.EGLConfig?
        ) {
            // Nothing to do here – initialization is done in surfaceCreated.
        }

        override fun onSurfaceChanged(
            gl: javax.microedition.khronos.opengles.GL10?,
            width: Int,
            height: Int
        ) {
            // The native code sets the viewport; nothing to do here.
        }

        override fun onDrawFrame(gl: javax.microedition.khronos.opengles.GL10?) {
            // Call our native render function to draw the triangle.
            Log.d("RenderTest", "rendering!");
            renderingEngine.nativeRender()
        }
    }
}