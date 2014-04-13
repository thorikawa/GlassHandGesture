package com.polysfactory.handgesture;

import java.io.File;

import android.content.Context;

public enum Marker {
    HAND("marker_hand.jpg", R.raw.hand);

    String filename;

    private int defaultRes;

    Marker(String file, int defaultRes) {
        this.filename = file;
        this.defaultRes = defaultRes;
    }

    public File getFile(Context context) {
        return IOUtils.getFilePath(context, Constants.MARKER_FILE_DIR, filename);
    }

    public String getFilePath(Context context) {
        return this.getFile(context).getAbsolutePath();
    }

    public int getDefaultRes() {
        return defaultRes;
    }
}
