#include <jni.h>

//
// Created by daltw on 2/8/2025.
//

#include "kollie/grid_2d.h"

extern "C"
JNIEXPORT jlong JNICALL
Java_com_wizneylabs_kollie_jni_Grid2D__1create(JNIEnv *env, jobject thiz) {

    Grid2D *grid = new Grid2D();

    return reinterpret_cast<jlong>(grid);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_jni_Grid2D__1destroy(JNIEnv *env, jobject thiz, jlong ptr,
                                                jboolean free_glresources) {

    Grid2D* grid = reinterpret_cast<Grid2D*>(ptr);

    grid->destroy(free_glresources);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wizneylabs_kollie_jni_Grid2D__1draw(JNIEnv *env, jobject thiz, jlong ptr) {

    Grid2D* grid = reinterpret_cast<Grid2D*>(ptr);

    grid->draw();
}