#include <jni.h>
#include <string>

#include <android/log.h>

#include "glm/glm.hpp"

// Define log tag (use your app name or component name)
//#define LOG_TAG "KolliePhysics"
#define LOG_TAG "KollieGameViewModelTapInput"

// Define convenience macros for different log levels
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C"
JNIEXPORT jstring JNICALL
Java_com_wizneylabs_kollie_collie_RenderingEngine_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello Kollie from C++!";
    return env->NewStringUTF(hello.c_str());
}

// Structure to hold the native version of the data
struct GridCollisionQueryOutput {
    int row;
    int column;
};

// Helper function to convert native struct to Kotlin object
jobject createGridCollisionOutput(JNIEnv* env, const GridCollisionQueryOutput& collision) {

    // TODO: glm test - remove before flight!
    glm::vec3 myVector(42.0f, 69.0f, 23.0f);

    LOGD("my glm vector = (%f, %f, %f)", myVector.x, myVector.y, myVector.z);


    // Find the Kotlin class
    jclass outputClass = env->FindClass("com/wizneylabs/kollie/collie/GridCollisionQueryOutput");

    if (outputClass == nullptr) {
        return nullptr; // Class not found
    }

    // Get the constructor
    jmethodID constructor = env->GetMethodID(outputClass, "<init>", "()V");
    if (constructor == nullptr) {
        return nullptr; // Constructor not found
    }

    // Create a new instance
    jobject result = env->NewObject(outputClass, constructor);
    if (result == nullptr) {
        return nullptr; // Failed to create object
    }

    // Get field IDs
    jfieldID rowField = env->GetFieldID(outputClass, "row", "I");
    jfieldID columnField = env->GetFieldID(outputClass, "column", "I");

    if (rowField == nullptr || columnField == nullptr) {
        return nullptr; // Fields not found
    }

    // Set the fields
    env->SetIntField(result, rowField, collision.row);
    env->SetIntField(result, columnField, collision.column);

    return result;
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_wizneylabs_kollie_collie_RenderingEngine_gridCollisionQuery(JNIEnv *env, jobject thiz,
                                                                     jobject input) {
    // TODO: get field IDs once during class init!
    jclass queryInputClass = env->GetObjectClass(input);

    jfieldID cellSizeFieldID = env->GetFieldID(queryInputClass, "cellSize", "I");
    jfieldID pointXFieldID = env->GetFieldID(queryInputClass, "pointX", "I");
    jfieldID pointYFieldID = env->GetFieldID(queryInputClass, "pointY", "I");

    // now parse the data from the field
    jint cellSize = env->GetIntField(input, cellSizeFieldID);
    jint pointX = env->GetIntField(input, pointXFieldID);
    jint pointY = env->GetIntField(input, pointYFieldID);

//    __android_log_print(ANDROID_LOG_DEBUG, "KollieGameViewModelTapInput",
//                        "physics x = %d, y = %d, cellSize = %d",
//                        pointX, pointY, cellSize);

    // compute output cell row + column
    GridCollisionQueryOutput output;

    output.row = static_cast<int>(pointY / cellSize);
    output.column = static_cast<int>(pointX / cellSize);

    // return result
    return createGridCollisionOutput(env, output);
}