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

        renderer = new Renderer();
    }
}

JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_collie_RenderingEngine_resize(JNIEnv* env, jobject obj, jint width, jint height) {
    if (renderer) {
        renderer->resize(width, height);
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
Java_com_wizneylabs_kollie_collie_RenderingEngine_drawFullScreenQuad(JNIEnv *env, jobject thiz,
                                                                     jlong shader_program_id) {
    if (renderer)
    {
        renderer->drawFullScreenQuad(shader_program_id);
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_collie_RenderingEngine_drawGrid(JNIEnv *env, jobject thiz) {
    if (renderer)
    {
        renderer->drawGrid();
    }
}