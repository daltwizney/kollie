#include <jni.h>

//
// Created by daltw on 2/6/2025.
//

#include "kollie/shader_program.h"

#include "kollie/camera_2d.h"

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
Java_com_wizneylabs_kollie_ShaderProgram__1setUniform2f(JNIEnv *env, jobject thiz, jlong ptr,
                                                               jstring name, jfloat x, jfloat y) {

    ShaderProgram* program = reinterpret_cast<ShaderProgram*>(ptr);

    std::string uniformName = std::string(env->GetStringUTFChars(name, nullptr));

    program->setUniform2f(uniformName, x, y);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_ShaderProgram__1updateProjectionMatrix2D(JNIEnv *env, jobject thiz,
                                                                    jlong ptr, jlong camera_ptr,
                                                                    jint screenWidth,
                                                                    jint screenHeight) {
    // TODO: implement _updateProjectionMatrix2D()
    ShaderProgram* program = reinterpret_cast<ShaderProgram*>(ptr);
    Camera2D* camera = reinterpret_cast<Camera2D*>(camera_ptr);

//    glm::mat4 projectionMatrix = camera->getProjectionMatrix();

    LOGE("TODO: this works only when screenHeight and screenWidth are flipped... seems to be b.c. we force landscape orientation at activity level...");

    glm::mat4 projectionMatrix = glm::ortho(
            0.0f, screenHeight * 1.0f, 0.0f, screenWidth * 1.0f, -1.0f, 1.0f);

//    LOGD("screen size = (%d, %d)", screenWidth, screenHeight);

    program->setUniformMatrix4fv("projection", 1, false,
                                 glm::value_ptr(projectionMatrix));
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_ShaderProgram__1updateViewMatrix2D(JNIEnv *env, jobject thiz, jlong ptr,
                                                              jlong camera_ptr) {
    // TODO: implement _updateViewMatrix2D()
    ShaderProgram* program = reinterpret_cast<ShaderProgram*>(ptr);
    Camera2D* camera = reinterpret_cast<Camera2D*>(camera_ptr);

//    glm::mat4 viewMatrix = camera->getViewMatrix();

    glm::mat4 viewMatrix = glm::lookAt(
            glm::vec3(0.0f, 0.0f, 0.0f),  // camera position (at origin)
            glm::vec3(0.0f, 0.0f, -1.0f), // looking down -Z axis
            glm::vec3(0.0f, 1.0f, 0.0f)   // up vector
    );

    program->setUniformMatrix4fv("view", 1, false,
                                 glm::value_ptr(viewMatrix));
}