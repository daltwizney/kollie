#include <jni.h>

//
// Created by daltw on 2/6/2025.
//

#include "collie/log.h"

#include "collie/full_screen_quad.h"

extern "C"
JNIEXPORT jlong JNICALL
Java_com_wizneylabs_kollie_collie_FullScreenQuad__1create(JNIEnv *env, jobject thiz) {

    FullScreenQuad *quad = new FullScreenQuad();

    return reinterpret_cast<jlong>(quad);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_collie_FullScreenQuad__1init(JNIEnv *env, jobject thiz, jlong ptr) {


}

//extern "C"
//JNIEXPORT void JNICALL
//Java_com_wizneylabs_kollie_collie_FullScreenQuad_draw(JNIEnv *env, jobject thiz) {
//

//}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_collie_FullScreenQuad__1draw(JNIEnv *env, jobject thiz, jlong ptr) {

    FullScreenQuad* quad = reinterpret_cast<FullScreenQuad*>(ptr);

    quad->draw();

    // TODO: get screen quad VAO id from quad object at address stored in 'ptr'
}