#include <jni.h>

//
// Created by daltw on 2/6/2025.
//

#include "kollie/log.h"

#include "kollie/full_screen_quad.h"

extern "C"
JNIEXPORT jlong JNICALL
Java_com_wizneylabs_kollie_FullScreenQuad__1create(JNIEnv *env, jobject thiz) {

    FullScreenQuad *quad = new FullScreenQuad();

    return reinterpret_cast<jlong>(quad);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_FullScreenQuad__1initBuffers(JNIEnv *env, jobject thiz, jlong ptr) {

    FullScreenQuad* quad = reinterpret_cast<FullScreenQuad*>(ptr);

    quad->initBuffers();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_FullScreenQuad__1draw(JNIEnv *env, jobject thiz, jlong ptr) {

    FullScreenQuad* quad = reinterpret_cast<FullScreenQuad*>(ptr);

    quad->draw();
}
extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_FullScreenQuad__1destroy(JNIEnv *env, jobject thiz, jlong ptr, jboolean freeGLResources) {

    FullScreenQuad* quad = reinterpret_cast<FullScreenQuad*>(ptr);

    quad->destroy(freeGLResources);

    delete quad;
}