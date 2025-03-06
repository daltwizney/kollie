#include <jni.h>

//
// Created by daltw on 2/6/2025.
//

#include "kollie/shader_program.h"

#include "kollie/camera_2d.h"

#include "kollie/perspective_camera.h"

#include "kollie/log.h"

#include "glm/ext.hpp"

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
Java_com_wizneylabs_kollie_ShaderProgram__1setUniform1f(JNIEnv *env, jobject thiz, jlong ptr,
                                                        jstring name, jfloat x) {

    ShaderProgram* program = reinterpret_cast<ShaderProgram*>(ptr);

    std::string uniformName = std::string(env->GetStringUTFChars(name, nullptr));

    program->setUniform1f(uniformName, x);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_ShaderProgram__1setUniform2f(JNIEnv *env, jobject thiz, jlong ptr,
                                                               jstring name, jfloat x, jfloat y) {

    ShaderProgram* program = reinterpret_cast<ShaderProgram*>(ptr);

    std::string uniformName = std::string(env->GetStringUTFChars(name, nullptr));

    program->setUniform2f(uniformName, x, y);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_ShaderProgram__1updateProjectionMatrix2D(JNIEnv *env, jobject thiz,
                                                                    jlong ptr, jlong camera_2D_ptr) {
    // TODO: implement _updateProjectionMatrix2D()
    ShaderProgram* program = reinterpret_cast<ShaderProgram*>(ptr);
    Camera2D* camera = reinterpret_cast<Camera2D*>(camera_2D_ptr);

    glm::mat4 projectionMatrix = camera->getProjectionMatrix();

    program->setUniformMatrix4fv("projection", 1, false,
                                 glm::value_ptr(projectionMatrix));
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_ShaderProgram__1updateViewMatrix2D(JNIEnv *env, jobject thiz, jlong ptr,
                                                              jlong camera_2D_ptr) {
    // TODO: implement _updateViewMatrix2D()
    ShaderProgram* program = reinterpret_cast<ShaderProgram*>(ptr);
    Camera2D* camera = reinterpret_cast<Camera2D*>(camera_2D_ptr);

    glm::mat4 viewMatrix = camera->getViewMatrix();

    program->setUniformMatrix4fv("view", 1, false,
                                 glm::value_ptr(viewMatrix));
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_ShaderProgram__1updateViewMatrix(JNIEnv *env, jobject thiz, jlong ptr,
                                                            jlong camera_ptr) {
    ShaderProgram* program = reinterpret_cast<ShaderProgram*>(ptr);
    PerspectiveCamera* camera = reinterpret_cast<PerspectiveCamera*>(camera_ptr);

    glm::mat4 viewMatrix = camera->getViewMatrix();

    program->setUniformMatrix4fv("view", 1, false,
                                 glm::value_ptr(viewMatrix));
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_ShaderProgram__1updateProjectionMatrix(JNIEnv *env, jobject thiz,
                                                                  jlong ptr, jlong camera_ptr) {
    ShaderProgram* program = reinterpret_cast<ShaderProgram*>(ptr);
    PerspectiveCamera* camera = reinterpret_cast<PerspectiveCamera*>(camera_ptr);

    glm::mat4 projectionMatrix = camera->getProjectionMatrix();

    program->setUniformMatrix4fv("projection", 1, false,
                                 glm::value_ptr(projectionMatrix));
}