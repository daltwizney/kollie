// native_renderer.cpp

#include <jni.h>
#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <EGL/egl.h>
#include <GLES3/gl3.h>
#include <android/log.h>
#include <stdlib.h>

#define LOG_TAG "NativeRenderer"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

// Global variables for EGL and GL objects.
static EGLDisplay eglDisplay   = EGL_NO_DISPLAY;
static EGLContext eglContext   = EGL_NO_CONTEXT;
static EGLSurface eglSurface   = EGL_NO_SURFACE;
static ANativeWindow* nativeWindow = nullptr;

static GLuint gProgram = 0;
static GLuint gVBO     = 0;

// -----------------------------------------------------------------------------
// Helper: compile a shader and check for errors.
GLuint compileShader(GLenum type, const char* source) {
    GLuint shader = glCreateShader(type);
    glShaderSource(shader, 1, &source, nullptr);
    glCompileShader(shader);
    GLint compiled = 0;
    glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
    if (!compiled) {
        char log[512];
        glGetShaderInfoLog(shader, 512, nullptr, log);
        LOGE("Shader compilation failed: %s", log);
        glDeleteShader(shader);
        return 0;
    }
    return shader;
}

// -----------------------------------------------------------------------------
// Create an EGL context and window surface from the given ANativeWindow.
bool initEGL(ANativeWindow* window) {
    eglDisplay = eglGetDisplay(EGL_DEFAULT_DISPLAY);
    if (eglDisplay == EGL_NO_DISPLAY) {
        LOGE("Unable to get EGL display");
        return false;
    }
    if (!eglInitialize(eglDisplay, nullptr, nullptr)) {
        LOGE("Unable to initialize EGL");
        return false;
    }

    // Choose an EGL configuration.
    const EGLint configAttribs[] = {
            EGL_RENDERABLE_TYPE, EGL_OPENGL_ES3_BIT,
            EGL_SURFACE_TYPE,    EGL_WINDOW_BIT,
            EGL_RED_SIZE,        8,
            EGL_GREEN_SIZE,      8,
            EGL_BLUE_SIZE,       8,
            EGL_ALPHA_SIZE,      8,
            EGL_NONE
    };

    EGLConfig config;
    EGLint numConfigs;
    if (!eglChooseConfig(eglDisplay, configAttribs, &config, 1, &numConfigs) || numConfigs < 1) {
        LOGE("No suitable EGL configs found");
        return false;
    }

    // Create an EGL window surface.
    eglSurface = eglCreateWindowSurface(eglDisplay, config, window, nullptr);
    if (eglSurface == EGL_NO_SURFACE) {
        LOGE("Unable to create EGL surface");
        return false;
    }

    // Create an EGL context (requesting OpenGL ES 3).
    const EGLint contextAttribs[] = {
            EGL_CONTEXT_CLIENT_VERSION, 3,
            EGL_NONE
    };
    eglContext = eglCreateContext(eglDisplay, config, EGL_NO_CONTEXT, contextAttribs);
    if (eglContext == EGL_NO_CONTEXT) {
        LOGE("Unable to create EGL context");
        return false;
    }

    // Bind the context and surface to the current thread.
    if (!eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
        LOGE("Unable to make EGL context current");
        return false;
    }

    // Set the viewport to the surface dimensions.
    EGLint width, height;
    eglQuerySurface(eglDisplay, eglSurface, EGL_WIDTH, &width);
    eglQuerySurface(eglDisplay, eglSurface, EGL_HEIGHT, &height);
    glViewport(0, 0, width, height);

    return true;
}

// -----------------------------------------------------------------------------
// Initialize OpenGL state: compile shaders, link a program, and create a VBO.
bool initGL() {
    // Minimal vertex shader (uses OpenGL ES 3.1 syntax).
    const char* vertexShaderSource = R"(
        #version 310 es
        layout(location = 0) in vec4 a_position;
        void main() {
            gl_Position = a_position;
        }
    )";

    // Minimal fragment shader: output a solid red color.
    const char* fragmentShaderSource = R"(
        #version 310 es
        precision mediump float;
        out vec4 fragColor;
        void main() {
            fragColor = vec4(1.0, 0.0, 0.0, 1.0);
        }
    )";

    GLuint vertexShader = compileShader(GL_VERTEX_SHADER, vertexShaderSource);
    if (!vertexShader) return false;
    GLuint fragmentShader = compileShader(GL_FRAGMENT_SHADER, fragmentShaderSource);
    if (!fragmentShader) return false;

    // Create and link the shader program.
    gProgram = glCreateProgram();
    glAttachShader(gProgram, vertexShader);
    glAttachShader(gProgram, fragmentShader);
    glLinkProgram(gProgram);
    GLint linked = 0;
    glGetProgramiv(gProgram, GL_LINK_STATUS, &linked);
    if (!linked) {
        char log[512];
        glGetProgramInfoLog(gProgram, 512, nullptr, log);
        LOGE("Program linking failed: %s", log);
        glDeleteProgram(gProgram);
        gProgram = 0;
        return false;
    }
    // Shaders are no longer needed after linking.
    glDeleteShader(vertexShader);
    glDeleteShader(fragmentShader);

    // Set up a VBO containing three vertices for a triangle.
    float triangleVertices[] = {
            0.0f,  0.5f, 0.0f,   // top
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f    // bottom right
    };

    glGenBuffers(1, &gVBO);
    glBindBuffer(GL_ARRAY_BUFFER, gVBO);
    glBufferData(GL_ARRAY_BUFFER, sizeof(triangleVertices), triangleVertices, GL_STATIC_DRAW);

    return true;
}

// -----------------------------------------------------------------------------
// Called from Kotlin to initialize EGL/GL using the provided Surface.
extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_collie_RenderingEngine_nativeInit(JNIEnv* env, jobject /* this */, jobject surface) {
    if (nativeWindow != nullptr) {
        // Already initialized.
        return;
    }
    // Obtain an ANativeWindow* from the Java Surface.
    nativeWindow = ANativeWindow_fromSurface(env, surface);
    if (!initEGL(nativeWindow)) {
        LOGE("EGL initialization failed");
        return;
    }
    if (!initGL()) {
        LOGE("OpenGL initialization failed");
        return;
    }
}

// -----------------------------------------------------------------------------
// Called every frame from Kotlin to draw a triangle.
extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_collie_RenderingEngine_nativeRender(JNIEnv* /* env */, jobject /* this */) {
    if (eglDisplay == EGL_NO_DISPLAY) return;

    // Clear the screen.
    glClearColor(0.1f, 0.2f, 0.3f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);

    // Use our shader program and bind our VBO.
    glUseProgram(gProgram);
    glBindBuffer(GL_ARRAY_BUFFER, gVBO);
    glEnableVertexAttribArray(0);
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), (void*)0);
    // Draw the triangle.
    glDrawArrays(GL_TRIANGLES, 0, 3);
    glDisableVertexAttribArray(0);

    // Swap the buffers to display the frame.
    eglSwapBuffers(eglDisplay, eglSurface);
}

// -----------------------------------------------------------------------------
// Called from Kotlin when the view is destroyed.
extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_collie_RenderingEngine_nativeDestroy(JNIEnv* /* env */, jobject /* this */) {
    if (eglDisplay != EGL_NO_DISPLAY) {
        eglMakeCurrent(eglDisplay, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT);
        if (eglContext != EGL_NO_CONTEXT) {
            eglDestroyContext(eglDisplay, eglContext);
            eglContext = EGL_NO_CONTEXT;
        }
        if (eglSurface != EGL_NO_SURFACE) {
            eglDestroySurface(eglDisplay, eglSurface);
            eglSurface = EGL_NO_SURFACE;
        }
        eglTerminate(eglDisplay);
        eglDisplay = EGL_NO_DISPLAY;
    }
    if (nativeWindow) {
        ANativeWindow_release(nativeWindow);
        nativeWindow = nullptr;
    }
}
