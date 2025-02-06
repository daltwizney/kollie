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

    var currentShader: ShaderProgram = ShaderProgram();

    var quad = FullScreenQuad();

    private var _width: Int = 0;
    private var _height: Int = 0;

    private val _nativeRenderer = RenderingEngine()

    private var _shaderProgramID: Long = -1;

    override fun onSurfaceCreated(p0: GL10?, p1: javax.microedition.khronos.egl.EGLConfig?) {

        quad.initBuffers();
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {

        _width = width;
        _height = height;

        Log.e(TAG, "TODO: update shader 'resolution' uniform!");

        _nativeRenderer.resize(width, height)
    }

    override fun onDrawFrame(p0: GL10?) {

        if (!currentShader.canUse())
        {
            Log.e(TAG, "Current shader failed to compile!");
            return;
        }

        currentShader.use();
        quad.draw();
    }

    fun destroy() {

        currentShader.destroy();
        quad.destroy();
        _nativeRenderer.destroy();
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
        renderer.currentShader.vertexShaderSource = vertexShaderSource;
        renderer.currentShader.fragmentShaderSource = fragmentShaderSource;

        renderer.currentShader.compile();

        Log.e(TAG, "TODO: need to update shader 'resolution' uniform!");

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
        super.onDetachedFromWindow()
        renderer.destroy()
    }
}