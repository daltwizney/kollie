#include <jni.h>

//
// Created by daltw on 3/6/2025.
//

#include "kollie/uv_sphere.h"

extern "C"
JNIEXPORT jlong JNICALL
Java_com_wizneylabs_kollie_jni_UvSphere__1create(JNIEnv *env, jobject thiz) {

    UvSphere *sphere = new UvSphere();

    return reinterpret_cast<jlong>(sphere);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_jni_UvSphere__1destroy(JNIEnv *env, jobject thiz, jlong ptr,
                                                  jboolean free_glresources) {

    UvSphere *sphere = reinterpret_cast<UvSphere*>(ptr);

    sphere->destroy(free_glresources);

    delete sphere;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_jni_UvSphere__1draw(JNIEnv *env, jobject thiz, jlong ptr,
                                               jlong shader_program_ptr, jlong camera_ptr) {

    UvSphere *sphere = reinterpret_cast<UvSphere*>(ptr);
    ShaderProgram *program = reinterpret_cast<ShaderProgram*>(shader_program_ptr);
    PerspectiveCamera *camera = reinterpret_cast<PerspectiveCamera*>(camera_ptr);

    sphere->draw(program, camera);
}