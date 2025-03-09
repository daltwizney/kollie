#include <jni.h>

//
// Created by daltw on 3/6/2025.
//

extern "C"
JNIEXPORT jlong JNICALL
Java_com_wizneylabs_kollie_jni_UvSphere__1create(JNIEnv *env, jobject thiz) {
    // TODO: implement _create()
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_jni_UvSphere__1destroy(JNIEnv *env, jobject thiz, jlong ptr,
                                                  jboolean free_glresources) {
    // TODO: implement _destroy()
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_jni_UvSphere__1draw(JNIEnv *env, jobject thiz, jlong ptr,
                                               jlong shader_program_ptr, jlong camera_ptr) {
    // TODO: implement _draw()
}