//
// Created by daltw on 2/1/2025.
//

#ifndef KOLLIE_LOG_H
#define KOLLIE_LOG_H

#include <android/log.h>

// Define log tag (use your app name or component name)
//#define LOG_TAG "KolliePhysics"
#define LOG_TAG "CollieLog"

// Define convenience macros for different log levels
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)


#endif //KOLLIE_LOG_H
