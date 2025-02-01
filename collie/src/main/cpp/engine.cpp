#include <jni.h>

#include <GLES3/gl31.h>

#include "collie/log.h"

class Renderer {

public:

    void init();
    void resize(int width, int height);
    void draw();
};

void Renderer::init() {

    glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
    LOGI("OpenGL initialized!");
}

void Renderer::resize(int width, int height) {
    glViewport(0, 0, width, height);
    LOGI("Surface resized to %dx%d", width, height);
}

void Renderer::draw() {
    glClear(GL_COLOR_BUFFER_BIT);
}

static Renderer* renderer = nullptr;

extern "C" {

JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_collie_RenderingEngine_init(JNIEnv* env, jobject obj) {
    if (!renderer) {
        renderer = new Renderer();
        renderer->init();
    }
}

JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_collie_RenderingEngine_resize(JNIEnv* env, jobject obj, jint width, jint height) {
    if (renderer) {
        renderer->resize(width, height);
    }
}

JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_collie_RenderingEngine_draw(JNIEnv* env, jobject obj) {
    if (renderer) {
        renderer->draw();

        LOGD("we're drawing!");
    }
}

JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_collie_RenderingEngine_destroy(JNIEnv* env, jobject obj) {
    if (renderer) {
        delete renderer;
        renderer = nullptr;
    }
}

}

