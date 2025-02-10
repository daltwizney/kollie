package com.wizneylabs.kollie.core

import android.opengl.GLSurfaceView
import android.util.Log
import com.wizneylabs.kollie.FullScreenQuad
import com.wizneylabs.kollie.RenderingEngine
import com.wizneylabs.kollie.ShaderProgram
import com.wizneylabs.kollie.jni.Camera2D
import javax.microedition.khronos.opengles.GL10

class CanvasRenderer: GLSurfaceView.Renderer {

    private val TAG = CanvasRenderer::class.simpleName;

    var camera2D: Camera2D? = null;

    val shaderSources = hashMapOf<String, Pair<String, String>>();

    private var _fullScreenShader: ShaderProgram? = null;
    private var _fullScreenQuad: FullScreenQuad? = null;

    var gridShader: ShaderProgram? = null;

    private var _width: Int = 0;
    private var _height: Int = 0;

    init {

        RenderingEngine.initialize();

        _fullScreenShader = ShaderProgram();
        gridShader = ShaderProgram();
    }

    override fun onSurfaceCreated(p0: GL10?, p1: javax.microedition.khronos.egl.EGLConfig?) {

        val fullScreenShaderSource = shaderSources["fullScreenQuad"];

        if (fullScreenShaderSource != null)
        {
            _fullScreenShader?.vertexShaderSource = fullScreenShaderSource.first;
            _fullScreenShader?.fragmentShaderSource = fullScreenShaderSource.second;
            _fullScreenShader?.compile();
        }
        else
        {
            throw RuntimeException("Unable to load full screen quad shader!");
        }
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {

        _width = width;
        _height = height;

        if (camera2D == null)
        {
            camera2D = Camera2D(width, height);
        }

        if (_fullScreenQuad == null)
        {
            _fullScreenQuad = FullScreenQuad();
        }

        camera2D?.setScreenDimensions(width, height);

        _fullScreenQuad?.resize(width, height);

        Log.d(TAG, "surface changed, size = ${width}, ${height}");

        RenderingEngine.resize(width, height);
    }

    override fun onDrawFrame(p0: GL10?) {

        RenderingEngine.clearColorBuffer();

        _fullScreenShader?.let { shader ->

            shader.use();

            shader.updateViewMatrix2D(camera2D!!);
            shader.updateProjectionMatrix2D(camera2D!!, _width, _height);

            shader.setUniform2f("resolution", 1.0f * _width, 1.0f * _height);

            _fullScreenQuad?.draw();
        }
    }

    fun destroy() {

        _fullScreenShader?.destroy(false);
        _fullScreenQuad?.destroy(false);

        _fullScreenShader = null;
        _fullScreenQuad = null;
    }
}
