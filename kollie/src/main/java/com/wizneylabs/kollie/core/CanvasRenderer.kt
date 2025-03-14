package com.wizneylabs.kollie.core

import android.graphics.Shader
import android.opengl.GLSurfaceView
import android.util.Log
import com.google.ar.core.Session
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.wizneylabs.kollie.FullScreenQuad
import com.wizneylabs.kollie.RenderingEngine
import com.wizneylabs.kollie.ShaderProgram
import com.wizneylabs.kollie.jni.Camera2D
import com.wizneylabs.kollie.jni.Cube
import com.wizneylabs.kollie.jni.Grid2D
import com.wizneylabs.kollie.jni.PerspectiveCamera
import com.wizneylabs.kollie.jni.UvSphere
import javax.microedition.khronos.opengles.GL10

class CanvasRenderer(private val _arSession: Session): GLSurfaceView.Renderer {

    private val TAG = CanvasRenderer::class.simpleName;

    var camera2D: Camera2D? = null;

    var perspectiveCamera: PerspectiveCamera? = null;

    val shaderSources = hashMapOf<String, Pair<String, String>>();

    private var _fullScreenShader: ShaderProgram? = null;
    private var _fullScreenQuad: FullScreenQuad? = null;

    private var _gridShader: ShaderProgram? = null;
    private var _lineShader: ShaderProgram? = null;
    private var _grid: Grid2D? = null;

    private var _cubeShader: ShaderProgram? = null;
    private var _cube: Cube? = null;

    private var _sphereShader: ShaderProgram? = null;
    private var _sphere: UvSphere? = null;

    private var _width: Int = 0;
    private var _height: Int = 0;

    init {
    }

    override fun onSurfaceCreated(p0: GL10?, p1: javax.microedition.khronos.egl.EGLConfig?) {

        Log.d(TAG, "Kollie surface created!");

        RenderingEngine.initialize();

        _arSession.setCameraTextureName(RenderingEngine.getArCoreTextureId().toInt());

        _grid = Grid2D();

        _fullScreenShader = ShaderProgram();
        _gridShader = ShaderProgram();
        _lineShader = ShaderProgram();
        _cubeShader = ShaderProgram();
        _sphereShader = ShaderProgram();

        // compile full screen quad shader
        var shaderSource = shaderSources["fullScreenQuad"];

        if (shaderSource != null)
        {
            _fullScreenShader?.vertexShaderSource = shaderSource.first;
            _fullScreenShader?.fragmentShaderSource = shaderSource.second;
            _fullScreenShader?.compile();
        }

        // compile grid shader
        shaderSource = shaderSources["grid2D"];

        if (shaderSource != null)
        {
            _gridShader?.vertexShaderSource = shaderSource.first;
            _gridShader?.fragmentShaderSource = shaderSource.second;
            _gridShader?.compile();
        }

        // compile line shader
        shaderSource = shaderSources["line2D"];

        if (shaderSource != null)
        {
            _lineShader?.vertexShaderSource = shaderSource.first;
            _lineShader?.fragmentShaderSource = shaderSource.second;
            _lineShader?.compile();
        }

        // compile cube shader
        shaderSource = shaderSources["cube"];

        if (shaderSource != null)
        {
            _cubeShader?.vertexShaderSource = shaderSource.first;
            _cubeShader?.fragmentShaderSource = shaderSource.second;
            _cubeShader?.compile();
        }

        // compile phong shader
        shaderSource = shaderSources["phong"];

        if (shaderSource != null)
        {
            _sphereShader?.vertexShaderSource = shaderSource.first;
            _sphereShader?.fragmentShaderSource = shaderSource.second;
            _sphereShader?.compile();
        }
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {

        _width = width;
        _height = height;

        val aspectRatio = (width * 1.0f) / height;

        if (camera2D == null)
        {
            camera2D = Camera2D(width, height);
        }

        if (perspectiveCamera == null)
        {
            perspectiveCamera = PerspectiveCamera(45.0f, aspectRatio, 0.1f, 100.0f);
            perspectiveCamera?.setPosition(0.0f, 3.0f, 10.0f);
            perspectiveCamera?.lookAt(0.0f, 0.0f, 0.0f);
        }

        if (_fullScreenQuad == null)
        {
            _fullScreenQuad = FullScreenQuad();
        }

        if (_cube == null)
        {
            _cube = Cube();
        }

        if (_sphere == null)
        {
            _sphere = UvSphere();
        }

        camera2D?.setScreenDimensions(width, height);

        perspectiveCamera?.setAspectRatio(aspectRatio);

        _fullScreenQuad?.resize(width, height);

        RenderingEngine.resize(width, height);
    }

    override fun onDrawFrame(p0: GL10?) {

        RenderingEngine.clearColorBuffer();
        RenderingEngine.clearDepthBuffer();

//        val frame =
//            try {
//                _arSession.update();
//            } catch (e: CameraNotAvailableException) {
//                Log.e("ExamplesMainActivity", "Camera not available during onDrawFrame", e)
//                return
//            }

//        _drawFullScreenQuad();
        _drawSphere();
    }

    private fun _drawSphere() {

        _sphereShader?.let { shader ->

            shader.use();

            shader.setUniform1f("ambient", 0.1f);

            shader.updateViewMatrix(perspectiveCamera!!);
            shader.updateProjectionMatrix(perspectiveCamera!!);

            // TODO: setting "model" uniform inside draw call for now,
            // but need to update shader JNI to accept mat4's from kotlin!

            _sphere?.draw(shader, perspectiveCamera!!);
        }
    }

    private fun _drawCube() {

        _cubeShader?.let { shader ->

            shader.use();

            shader.setUniform1f("ambient", 0.1f);

            shader.updateViewMatrix(perspectiveCamera!!);
            shader.updateProjectionMatrix(perspectiveCamera!!);

            // TODO: setting "model" uniform inside draw call for now,
            // but need to update shader JNI to accept mat4's from kotlin!

            _cube?.draw(shader, perspectiveCamera!!);
        }
    }

    private fun _drawFullScreenQuad() {

        _fullScreenShader?.let { shader ->

            shader.use();

            shader.updateViewMatrix2D(camera2D!!);
            shader.updateProjectionMatrix2D(camera2D!!, _width, _height);

            shader.setUniform2f("resolution", 1.0f * _width, 1.0f * _height);

            _fullScreenQuad?.draw();
        }
    }

    private fun _drawGrid() {

        _gridShader?.let { shader ->

            shader.use();

            shader.updateViewMatrix2D(camera2D!!);
            shader.updateProjectionMatrix2D(camera2D!!, _width, _height);

            _grid?.draw();
        }
    }

    private fun _drawLine() {

        _lineShader?.let { shader ->

            shader.use();

            // TODO: update view + projection matrices and draw!
        }
    }

    fun destroy() {

        _cubeShader?.destroy(false);
        _cubeShader = null;

        _cube?.destroy();
        _cube = null;

        _sphere?.destroy();
        _sphere = null;

        _fullScreenShader?.destroy(false);
        _fullScreenShader = null;

        _fullScreenQuad?.destroy(false);
        _fullScreenQuad = null;

        _gridShader?.destroy(false);
        _gridShader = null;

        _grid?.destroy(false);
        _grid = null;
    }
}
