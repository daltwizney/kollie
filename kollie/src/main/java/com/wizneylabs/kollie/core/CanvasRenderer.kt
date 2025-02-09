package com.wizneylabs.kollie.core

import android.opengl.GLSurfaceView
import com.wizneylabs.kollie.FullScreenQuad
import com.wizneylabs.kollie.RenderingEngine
import com.wizneylabs.kollie.ShaderProgram
import com.wizneylabs.kollie.jni.Camera2D
import javax.microedition.khronos.opengles.GL10

class CanvasRenderer: GLSurfaceView.Renderer {

    private val TAG = CanvasRenderer::class.simpleName;

    var camera2D: Camera2D? = null;

    var fullScreenShader: ShaderProgram? = null;
    var fullScreenQuad: FullScreenQuad? = null;

    var gridShader: ShaderProgram? = null;

    private var _width: Int = 0;
    private var _height: Int = 0;

    init {

        RenderingEngine.load();

        camera2D = Camera2D();

        fullScreenShader = ShaderProgram();
        fullScreenQuad = FullScreenQuad();

        gridShader = ShaderProgram();
    }

    override fun onSurfaceCreated(p0: GL10?, p1: javax.microedition.khronos.egl.EGLConfig?) {

        fullScreenShader?.compile();
        fullScreenQuad?.initBuffers();
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {

        _width = width;
        _height = height;

        RenderingEngine.resize(width, height);
    }

    override fun onDrawFrame(p0: GL10?) {

        RenderingEngine.clearColorBuffer();

        fullScreenShader?.let { shader ->

            shader.updateViewMatrix2D(camera2D!!);
            shader.updateProjectionMatrix2D(camera2D!!, _width, _height);

            shader.use();

            shader.setUniform2f("resolution", 1.0f * _width, 1.0f * _height);

            fullScreenQuad?.draw();
        }
    }

    fun destroy() {

        fullScreenShader?.destroy(false);
        fullScreenQuad?.destroy(false);

        fullScreenShader = null;
        fullScreenQuad = null;
    }
}
