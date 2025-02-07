#include <jni.h>

//
// Created by daltw on 2/6/2025.
//

#include "kollie/shader_program.h"

extern "C"
JNIEXPORT jlong JNICALL
Java_com_wizneylabs_kollie_ShaderProgram__1create(JNIEnv *env, jobject thiz) {

    ShaderProgram* program = new ShaderProgram();

    return reinterpret_cast<jlong>(program);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_ShaderProgram__1compile(JNIEnv *env, jobject thiz, jlong ptr,
    jstring vertex_shader_source, jstring fragment_shader_source) {

    ShaderProgram* program = reinterpret_cast<ShaderProgram*>(ptr);

    program->vertexShaderSource = env->GetStringUTFChars(vertex_shader_source, nullptr);
    program->fragmentShaderSource = env->GetStringUTFChars(fragment_shader_source, nullptr);

    program->compile();
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_wizneylabs_kollie_ShaderProgram__1canUse(JNIEnv *env, jobject thiz, jlong ptr) {

    ShaderProgram* program = reinterpret_cast<ShaderProgram*>(ptr);

    return program->canUse();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_ShaderProgram__1use(JNIEnv *env, jobject thiz, jlong ptr) {

    ShaderProgram* program = reinterpret_cast<ShaderProgram*>(ptr);

    program->use();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_ShaderProgram__1destroy(JNIEnv *env, jobject thiz, jlong ptr, jboolean freeGLResources) {

    ShaderProgram* program = reinterpret_cast<ShaderProgram*>(ptr);

    program->destroy(freeGLResources);

    delete program;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_ShaderProgram__1setUniform2f(JNIEnv *env, jobject thiz, jlong ptr,
                                                               jstring name, jfloat x, jfloat y) {

    ShaderProgram* program = reinterpret_cast<ShaderProgram*>(ptr);

    std::string uniformName = std::string(env->GetStringUTFChars(name, nullptr));

    program->setUniform2f(uniformName, x, y);
}