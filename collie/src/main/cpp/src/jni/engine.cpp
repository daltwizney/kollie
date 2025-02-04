#include <jni.h>

#include <string>

#include <pthread.h>

#include "collie/log.h"

#include "collie/renderer.h"

static Renderer* renderer = nullptr;

extern "C" {

JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_collie_RenderingEngine_init(JNIEnv* env, jobject obj) {

    if (!renderer) {

        LOGE("TODO: we need to separate renderer initialization from shader compilation, so we can pass shaders after initialization happens!");

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

//        LOGD("frameCounter = %d", renderer->frameCounter());
    }
}

JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_collie_RenderingEngine_destroy(JNIEnv* env, jobject obj) {

    LOGD("Renderer destroy() called!");

    if (renderer) {
        renderer->destroy();
        delete renderer;
        renderer = nullptr;
    }
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_wizneylabs_kollie_collie_RenderingEngine_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello Kollie from C++!";
    return env->NewStringUTF(hello.c_str());
}

}


extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_collie_RenderingEngine_setShaderSource(JNIEnv *env, jobject thiz,
                                                            jstring vertex_shader_src,
                                                            jstring fragment_shader_src) {
    if (renderer) {

        auto vertSource = std::string(env->GetStringUTFChars(vertex_shader_src, 0));
        auto fragSource = std::string(env->GetStringUTFChars(fragment_shader_src, 0));

        renderer->setShaderSource(vertSource, fragSource);
    }
}