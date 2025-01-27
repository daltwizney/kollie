#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_wizneylabs_kollie_physics_NativeLib_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello Kollie from C++!";
    return env->NewStringUTF(hello.c_str());
}