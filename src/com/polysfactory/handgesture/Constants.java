package com.polysfactory.handgesture;

import org.opencv.android.CameraBridgeViewBase;

import android.hardware.Camera;
import android.util.Log;

public class Constants {
    public static final String MARKER_FILE_DIR = "image";
    public static final String MARKER_FILE_NAME = "marker.jpg";
    public static final int CAMERA_INDEX;
    public static final boolean FLIP;
    static {
        if (Camera.getNumberOfCameras() > 1) {
            Log.d(L.TAG, "Use front camera");
            CAMERA_INDEX = CameraBridgeViewBase.CAMERA_ID_FRONT;
            FLIP = true;
        } else {
            Log.d(L.TAG, "Use back camera");
            CAMERA_INDEX = CameraBridgeViewBase.CAMERA_ID_ANY;
            FLIP = false;
        }
    }
}
