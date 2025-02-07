#include <jni.h>

#include <string>

#include <pthread.h>

#include "kollie/log.h"

#include "kollie/renderer.h"

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_RenderingEngine_00024Companion_resize(JNIEnv *env, jobject thiz,
                                                                        jint width, jint height) {
    Renderer::resize(width, height);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_RenderingEngine_00024Companion_clearColorBuffer(JNIEnv *env,
                                                                                  jobject thiz) {
    Renderer::clearColorBuffer();
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_wizneylabs_kollie_RenderingEngine_00024Companion_isContextValid(JNIEnv *env,
                                                                                jobject thiz) {
    return Renderer::isContextValid();
}