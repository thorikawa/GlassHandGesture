package com.polysfactory.handgesture;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;

public class NativeBridge {
    public NativeBridge(String redMarker) {
        mNativeObj = nativeCreateObject(redMarker);
    }

    public void start() {
        nativeStart(mNativeObj);
    }

    public void stop() {
        nativeStop(mNativeObj);
    }

    public void process(Mat imageRgba, MatOfRect handRect) {
        nativeProcess(mNativeObj, imageRgba.getNativeObjAddr(), handRect.getNativeObjAddr());
    }

    public void release() {
        nativeDestroyObject(mNativeObj);
        mNativeObj = 0;
    }

    public void setSize(int srcWidth, int srcHeight, int destWidth, int destHeight) {
        nativeSetSize(mNativeObj, srcWidth, srcHeight, destWidth, destHeight);
    }

    private long mNativeObj = 0;

    private static native long nativeCreateObject(String redMarker);

    private static native void nativeDestroyObject(long thiz);

    private static native void nativeStart(long thiz);

    private static native void nativeStop(long thiz);

    private static native void nativeProcess(long thiz, long imageRgba, long handRect);

    private static native void nativeSetSize(long thiz, int srcWidth, int srcHeight, int destWidth, int destHeight);
}
