#include <jni.h>

//
// Created by daltw on 2/6/2025.
//

#include "collie/shader_program.h"

extern "C"
JNIEXPORT jlong JNICALL
Java_com_wizneylabs_kollie_collie_ShaderProgram__1create(JNIEnv *env, jobject thiz) {

    ShaderProgram* program = new ShaderProgram();

    return reinterpret_cast<jlong>(program);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_collie_ShaderProgram__1compile(JNIEnv *env, jobject thiz, jlong ptr) {

    ShaderProgram* program = reinterpret_cast<ShaderProgram*>(ptr);

    program->compile();
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_wizneylabs_kollie_collie_ShaderProgram__1canUse(JNIEnv *env, jobject thiz, jlong ptr) {

    ShaderProgram* program = reinterpret_cast<ShaderProgram*>(ptr);

    return program->canUse();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_collie_ShaderProgram__1use(JNIEnv *env, jobject thiz, jlong ptr) {

    ShaderProgram* program = reinterpret_cast<ShaderProgram*>(ptr);

    program->use();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_collie_ShaderProgram__1destroy(JNIEnv *env, jobject thiz, jlong ptr) {

    ShaderProgram* program = reinterpret_cast<ShaderProgram*>(ptr);

    program->destroy();
}