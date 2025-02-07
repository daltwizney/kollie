package com.wizneylabs.examples

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.Log

import com.wizneylabs.kollie.collie.CollieLoader
import com.wizneylabs.kollie.collie.FullScreenQuad
import com.wizneylabs.kollie.collie.RenderingEngine
import com.wizneylabs.kollie.collie.ShaderProgram
import javax.microedition.khronos.opengles.GL10

class MyRenderer: GLSurfaceView.Renderer {

    private val TAG = MyRenderer::class.simpleName;

    var fullScreenShader: ShaderProgram? = ShaderProgram();
    var fullScreenQuad: FullScreenQuad? = FullScreenQuad();

    var gridShader: ShaderProgram? = ShaderProgram();

    private var _width: Int = 0;
    private var _height: Int = 0;

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

class MyGLSurfaceView(private val context: Context) : GLSurfaceView(context) {

    val TAG = MyGLSurfaceView::class.simpleName;

    private val renderer = MyRenderer();

    init {

        CollieLoader // ensures library loaded

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