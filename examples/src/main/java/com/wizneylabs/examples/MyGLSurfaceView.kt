package com.wizneylabs.examples

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.Log

import com.wizneylabs.kollie.collie.RenderingEngine
import javax.microedition.khronos.opengles.GL10

class MyShaderProgram(
    renderer: RenderingEngine,
    vertexShaderSource: String,
    fragmentShaderSource: String) {

    private val _renderer = renderer;

    private val _vertexShaderSource = vertexShaderSource;
    private val _fragmentShaderSource = fragmentShaderSource;

    fun compile() {
        _renderer.compileShader(_vertexShaderSource, _fragmentShaderSource);
    }

//    fun
}

class MyRenderer: GLSurfaceView.Renderer {

    private val TAG = MyRenderer::class.simpleName;

    private val _nativeRenderer = RenderingEngine()

    private var _vertexShaderSource = "";
    private var _fragmentShaderSource = "";

    private var _shaderProgramID: Long = -1;

    fun setShaderSource(vertexShader: String, fragmentShader: String) {

        _vertexShaderSource = vertexShader;
        _fragmentShaderSource = fragmentShader;
    }

    override fun onSurfaceCreated(p0: GL10?, p1: javax.microedition.khronos.egl.EGLConfig?) {

        _nativeRenderer.init();
        _shaderProgramID = _nativeRenderer.compileShader(_vertexShaderSource, _fragmentShaderSource);
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {

        _nativeRenderer.resize(width, height)
    }

    override fun onDrawFrame(p0: GL10?) {

        if (_shaderProgramID < 0)
        {
            Log.e(TAG, "invalid shader program ID - failed to draw!");
            return;
        }

        _nativeRenderer.drawFullScreenQuad(_shaderProgramID);
    }

    fun destroy() {

        _nativeRenderer.destroy();
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
        val vertexShaderSource = this.loadShaderFromAssets("shaders/2d_passthrough.vert");
        val fragmentShaderSource = this.loadShaderFromAssets("shaders/circle.frag");

        renderer.setShaderSource(vertexShaderSource, fragmentShaderSource);

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