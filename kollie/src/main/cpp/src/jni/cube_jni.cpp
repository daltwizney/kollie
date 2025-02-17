#include <jni.h>

//
// Created by daltw on 2/17/2025.
//

#include "kollie/cube.h"

extern "C"
JNIEXPORT jlong JNICALL
Java_com_wizneylabs_kollie_jni_Cube__1create(JNIEnv *env, jobject thiz) {

    Cube* cube = new Cube();

    return reinterpret_cast<jlong>(cube);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_jni_Cube__1destroy(JNIEnv *env, jobject thiz, jlong ptr,
                                              jboolean free_glresources) {

    Cube* cube = reinterpret_cast<Cube*>(ptr);

    cube->destroy(free_glresources);

    delete cube;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_jni_Cube__1draw(JNIEnv *env, jobject thiz, jlong ptr, jlong shader_program_ptr) {

    Cube* cube = reinterpret_cast<Cube*>(ptr);
    ShaderProgram *program = reinterpret_cast<ShaderProgram*>(shader_program_ptr);

    cube->draw(program);
}