#include <jni.h>

//
// Created by daltw on 2/17/2025.
//

#include "kollie/perspective_camera.h"

extern "C"
JNIEXPORT jlong JNICALL
Java_com_wizneylabs_kollie_jni_PerspectiveCamera__1create(JNIEnv *env, jobject thiz, jfloat fov,
                                                          jfloat aspect_ratio, jfloat near_plane,
                                                          jfloat far_plane) {

    PerspectiveCamera* camera = new PerspectiveCamera(
            fov, aspect_ratio, near_plane, far_plane);

    return reinterpret_cast<jlong>(camera);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_jni_PerspectiveCamera__1destroy(JNIEnv *env, jobject thiz, jlong ptr,
                                                           jboolean free_glresources) {

    PerspectiveCamera* camera = reinterpret_cast<PerspectiveCamera*>(ptr);

    delete camera;
}
extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_jni_PerspectiveCamera__1setAspectRatio(JNIEnv *env, jobject thiz,
                                                                  jlong ptr, jfloat aspect_ratio) {

    PerspectiveCamera* camera = reinterpret_cast<PerspectiveCamera*>(ptr);

    camera->setAspectRatio(aspect_ratio);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_jni_PerspectiveCamera__1setPosition(JNIEnv *env, jobject thiz, jlong ptr,
                                                               jfloat x, jfloat y, jfloat z) {

    PerspectiveCamera* camera = reinterpret_cast<PerspectiveCamera*>(ptr);

    camera->setPosition(glm::vec3(x, y, z));
}
extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_jni_PerspectiveCamera__1lookAt(JNIEnv *env, jobject thiz, jlong ptr,
                                                          jfloat x, jfloat y, jfloat z) {

    PerspectiveCamera* camera = reinterpret_cast<PerspectiveCamera*>(ptr);

    camera->lookAt(glm::vec3(x, y, z));
}