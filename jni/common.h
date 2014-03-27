#ifndef _HANDGESTURE_COMMON_H_
#define _HANDGESTURE_COMMON_H_

#define IN_WIDTH 320
#define IN_HEIGHT 240
#define WIDTH 800
#define HEIGHT 600
#define DEBUG 0
#define CONSOLE 0

#ifdef __ANDROID__
#include <android/log.h>
#define LOG_TAG "N3/Native"
#if CONSOLE
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__);
#else
#define LOGD(...)
#endif
#else
#define LOGD(...) printf(__VA_ARGS__);
#endif

#endif
