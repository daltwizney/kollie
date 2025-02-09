#include <jni.h>

#include "kollie/camera_2d.h"

//
// Created by daltw on 2/8/2025.
//

extern "C"
JNIEXPORT jlong JNICALL
Java_com_wizneylabs_kollie_jni_Camera2D__1create(JNIEnv *env, jobject thiz) {

    Camera2D *camera = new Camera2D();

    return reinterpret_cast<jlong>(camera);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_jni_Camera2D__1destroy(JNIEnv *env, jobject thiz, jlong ptr,
                                                  jboolean free_glresources) {

    Camera2D* camera = reinterpret_cast<Camera2D*>(ptr);

    delete camera;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_jni_Camera2D__1setPosition(JNIEnv *env, jobject thiz, jlong ptr,
                                                      jfloat x, jfloat y, jfloat z) {

    Camera2D* camera = reinterpret_cast<Camera2D*>(ptr);

    camera->setPosition(glm::vec3(x, y, z));
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_jni_Camera2D__1lookAt(JNIEnv *env, jobject thiz, jlong ptr, jfloat x,
                                                 jfloat y, jfloat z) {

    Camera2D* camera = reinterpret_cast<Camera2D*>(ptr);

    camera->lookAt(glm::vec3(x, y, z));
}