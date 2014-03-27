package com.polysfactory.handgesture;

import java.io.File;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class MainActivity extends Activity implements CvCameraViewListener2 {

    private static final int REQUEST_MARKER_CAPTURE = 100;
    private Mat mFrame;
    private NativeBridge mNativeDetector;

    private CameraBridgeViewBase mOpenCvCameraView;

    private MenuItem mCaptureRedMenu;
    private ViewGroup mMarkerPreview;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(L.TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.marker_tracking);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.fd_activity_surface_view);
        mOpenCvCameraView.setCameraIndex(Constants.CAMERA_INDEX);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setMaxFrameSize(640, 360);

        mMarkerPreview = (ViewGroup) findViewById(R.id.marker_preview_container);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNativeDetector != null) {
            mNativeDetector.stop();
            mNativeDetector = null;
        }
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setupMarkerPreview();
        mNativeDetector = new NativeBridge(Marker.HAND.getFilePath(this), Marker.HAND.getFilePath(this));
        mNativeDetector.start();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.enableView();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mFrame = new Mat();
        Log.d(L.TAG, "startsize:" + width + "," + height);
        mNativeDetector.setSize(width, height, width, height);
    }

    public void onCameraViewStopped() {
        // XXX: do we need it??
        mFrame.release();
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

        Log.d(L.TAG, "main:oncamera frame");

        mFrame = inputFrame.rgba();
        if (Constants.FLIP) {
            Core.flip(mFrame, mFrame, 1);
        }

        if (mNativeDetector != null) {
            mNativeDetector.process(mFrame);
        }

        return mFrame;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mCaptureRedMenu = menu.add(R.string.capture_marker);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mCaptureRedMenu == item) {
            Intent intent = new Intent(this, CaptureActivity.class);
            intent.putExtra(CaptureActivity.EXTRA_KEY_MARKER, Marker.HAND);
            startActivityForResult(intent, REQUEST_MARKER_CAPTURE);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_MARKER_CAPTURE == requestCode) {
            // TODO reload marker
        }
    }

    private void setupMarkerPreview() {
        mMarkerPreview.removeAllViews();
        for (final Marker marker : Marker.values()) {
            File markerFile = marker.getFile(this);
            if (!markerFile.exists()) {
                IOUtils.copy(this, marker.getDefaultRes(), markerFile);
            }

            Mat mat = Highgui.imread(markerFile.getAbsolutePath());
            Bitmap markerBitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mat, markerBitmap);
            ImageView imageView = new ImageView(this);
            imageView.setMaxWidth(100);
            imageView.setMaxHeight(100);
            int width = 100;
            int height = 100;
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width, height);
            imageView.setLayoutParams(parms);
            imageView.setScaleType(ScaleType.CENTER_INSIDE);
            imageView.setImageBitmap(markerBitmap);
            imageView.setFocusable(true);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                    intent.putExtra(CaptureActivity.EXTRA_KEY_MARKER, marker);
                    startActivityForResult(intent, REQUEST_MARKER_CAPTURE);
                }
            });
            mMarkerPreview.addView(imageView);
        }
    }
}
