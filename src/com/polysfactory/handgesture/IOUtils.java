package com.polysfactory.handgesture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

public class IOUtils {
    public static File getFilePath(Context context, String dirname, String filename) {
        File cascadeDir = context.getDir(dirname, Context.MODE_PRIVATE);
        return new File(cascadeDir, filename);
    }

    public static boolean copy(Context context, int res, File file) {
        copy(context, context.getResources().openRawResource(res), file);
        return true;
    }

    public static boolean copy(Context context, File in, File file) {
        try {
            copy(context, new FileInputStream(in), file);
        } catch (FileNotFoundException e) {
            Log.e(L.TAG, "file copy error", e);
            return false;
        }
        return true;
    }

    public static boolean copy(Context context, InputStream is, File file) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (FileNotFoundException e) {
            Log.e(L.TAG, "file copy error", e);
            return false;
        } catch (IOException e) {
            Log.e(L.TAG, "file copy error", e);
            return false;
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }
}
