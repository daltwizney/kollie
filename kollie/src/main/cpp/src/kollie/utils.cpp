#include <pthread.h>
#include <jni.h>

#include "kollie/log.h"


// this func is helpful for checking which thread current func is running on
void checkThread() {
    pthread_t tid = pthread_self();
    __android_log_print(ANDROID_LOG_DEBUG, "kollieLog", "Thread ID: %lu", tid);
}

__attribute__((constructor))
void onLibraryLoad() {
    __android_log_print(ANDROID_LOG_DEBUG, "kollieLog", "Library loaded.");
}

__attribute__((destructor))
void onLibraryUnload() {
    __android_log_print(ANDROID_LOG_DEBUG, "kollieLog", "Library UNLOADED!");
}

extern "C" {

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    LOGD("JNI_OnLoad called, library loaded.");
    return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM* vm, void* reserved) {
    LOGD("JNI_OnUnload called, library unloaded.");
}
}