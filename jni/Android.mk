LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

OPENCV_INSTALL_MODULES:=on
OPENCV_LIB_TYPE:=SHARED

include $(OPENCV_ANDROID_SDK_HOME)/OpenCV.mk

LOCAL_SRC_FILES  := handgesture_jni.cpp Tracker.cpp
LOCAL_C_INCLUDES += $(LOCAL_PATH)
LOCAL_CFLAGS    := -Werror -O3 -ffast-math
LOCAL_LDLIBS     += -llog -ldl
LOCAL_ARM_NEON := true
LOCAL_MODULE     := handgesture

include $(BUILD_SHARED_LIBRARY)
