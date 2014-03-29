#include <handgesture_jni.h>
#include <Tracker.h>
#include <opencv2/core/core.hpp>
#include <opencv2/contrib/detection_based_tracker.hpp>

#include <string>
#include <vector>

using namespace std;
using namespace cv;
using namespace Apps;

inline void vector_Rect_to_Mat(vector<Rect>& v_rect, Mat& mat) {
	mat = Mat(v_rect, true);
}

JNIEXPORT jlong JNICALL Java_com_polysfactory_handgesture_NativeBridge_nativeCreateObject(
		JNIEnv * jenv, jclass, jstring jImageFile) {
	LOGD("Java_com_polysfactory_handgesture_NativeBridge_nativeCreateObject enter");
    const char* imageFile = jenv->GetStringUTFChars(jImageFile, NULL);
	jlong tracker;
	try {
		tracker = (jlong) new Tracker(string(imageFile));
	} catch (cv::Exception& e) {
		LOGD("nativeCreateObject caught cv::Exception: %s", e.what());
		jclass je = jenv->FindClass("org/opencv/core/CvException");
		if (!je)
			je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je, e.what());
	} catch (...) {
		LOGD("nativeCreateObject caught unknown exception");
		jclass je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je,
				"Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
		return 0;
	}

	LOGD("Java_com_polysfactory_handgesture_NativeBridge_nativeCreateObject exit");
	return tracker;
}

JNIEXPORT void JNICALL Java_com_polysfactory_handgesture_NativeBridge_nativeDestroyObject(
		JNIEnv * jenv, jclass, jlong thiz) {
	LOGD("Java_com_polysfactory_handgesture_NativeBridge_nativeDestroyObject enter");
	try {
	} catch (cv::Exception& e) {
		LOGD("nativeestroyObject caught cv::Exception: %s", e.what());
		jclass je = jenv->FindClass("org/opencv/core/CvException");
		if (!je)
			je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je, e.what());
	} catch (...) {
		LOGD("nativeDestroyObject caught unknown exception");
		jclass je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je,
				"Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
	}
	LOGD("Java_com_polysfactory_handgesture_NativeBridge_nativeDestroyObject exit");
}

JNIEXPORT void JNICALL Java_com_polysfactory_handgesture_NativeBridge_nativeStart(
		JNIEnv * jenv, jclass, jlong thiz) {
	LOGD("Java_com_polysfactory_handgesture_NativeBridge_nativeStart enter");
	try {
	} catch (cv::Exception& e) {
		LOGD("nativeStart caught cv::Exception: %s", e.what());
		jclass je = jenv->FindClass("org/opencv/core/CvException");
		if (!je)
			je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je, e.what());
	} catch (...) {
		LOGD("nativeStart caught unknown exception");
		jclass je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je,
				"Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
	}
	LOGD("Java_com_polysfactory_handgesture_NativeBridge_nativeStart exit");
}

JNIEXPORT void JNICALL Java_com_polysfactory_handgesture_NativeBridge_nativeStop(
		JNIEnv * jenv, jclass, jlong thiz) {
	LOGD("Java_com_polysfactory_handgesture_NativeBridge_nativeStop enter");
	try {
	} catch (cv::Exception& e) {
		LOGD("nativeStop caught cv::Exception: %s", e.what());
		jclass je = jenv->FindClass("org/opencv/core/CvException");
		if (!je)
			je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je, e.what());
	} catch (...) {
		LOGD("nativeStop caught unknown exception");
		jclass je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je,
				"Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
	}
	LOGD("Java_com_polysfactory_handgesture_NativeBridge_nativeStop exit");
}

JNIEXPORT void JNICALL Java_com_polysfactory_handgesture_NativeBridge_nativeProcess(
		JNIEnv * jenv, jclass, jlong thiz, jlong imageRgba, jlong handRectMat) {
	LOGD("Java_com_polysfactory_handgesture_NativeBridge_nativeDetect enter");
	try {
		vector < Rect > handRectVec;
		((Tracker*)thiz)->process(*((Mat*)imageRgba), *((Mat*)imageRgba), handRectVec);
		vector_Rect_to_Mat(handRectVec, *((Mat*) handRectMat));
	} catch (cv::Exception& e) {
		LOGD("nativeCreateObject caught cv::Exception: %s", e.what());
		jclass je = jenv->FindClass("org/opencv/core/CvException");
		if (!je)
			je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je, e.what());
	} catch (...) {
		LOGD("nativeDetect caught unknown exception");
		jclass je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je,
				"Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
	}
	LOGD("Java_com_polysfactory_handgesture_NativeBridge_nativeDetect exit");
}

JNIEXPORT void JNICALL Java_com_polysfactory_handgesture_NativeBridge_nativeSetSize
  (JNIEnv * jenv, jclass, jlong thiz, jint srcWidth, jint srcHeight, jint destWidth, jint destHeight) {
	try {
		((Tracker*)thiz)->setSize((int)srcWidth, (int)srcHeight, (int)destWidth, (int)destHeight);
	} catch (cv::Exception& e) {
		LOGD("nativeCreateObject caught cv::Exception: %s", e.what());
		jclass je = jenv->FindClass("org/opencv/core/CvException");
		if (!je)
			je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je, e.what());
	} catch (...) {
		LOGD("nativeDetect caught unknown exception");
		jclass je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je,
				"Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
	}
}
