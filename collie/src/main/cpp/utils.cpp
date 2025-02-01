#include <jni.h>
#include <string>

#include "collie/log.h"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_wizneylabs_kollie_collie_RenderingEngine_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello Kollie from C++!";
    return env->NewStringUTF(hello.c_str());
}