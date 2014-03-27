package com.polysfactory.handgesture;

import org.opencv.android.OpenCVLoader;

import android.app.Application;

public class HandGestureApplication extends Application {
    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        } else {
            System.loadLibrary("handgesture");
        }
    }

    public void onCreate() {
    };
}
