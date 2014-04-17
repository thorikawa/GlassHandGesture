package com.polysfactory.handgesture;

import java.io.File;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class CaptureActivity extends Activity implements CvCameraViewListener2, OnClickListener {

    // for marker detecting
    private static final int LINE_WIDTH = 3;

    private Button finishCaptureButton;

    enum State {
        CAMERA, SELECTING, TRACKING
    }

    private State mState = State.CAMERA;

    private Mat mCaptured;

    private Mat mFrame;

    private CameraBridgeViewBase mOpenCvCameraView;

    private File mMarkerFile;
    private Marker mMarker;

    public static final String EXTRA_KEY_MARKER = "marker";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.capture_marker);

        finishCaptureButton = (Button) findViewById(R.id.button_capture);
        finishCaptureButton.setOnClickListener(this);
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.camera_view);

        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setCameraIndex(Constants.CAMERA_INDEX);
        mOpenCvCameraView.setMaxFrameSize(400, 240);

        mMarker = (Marker) getIntent().getSerializableExtra(EXTRA_KEY_MARKER);
        mMarkerFile = mMarker.getFile(this);

        startCameraMode();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
    }

    public void onCameraViewStopped() {
        // XXX: do we need it??
        // mFrame.release();
        // mCacheBitmap.recycle();
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

        mFrame = inputFrame.rgba();
        if (Constants.FLIP) {
            Core.flip(mFrame, mFrame, 1);
        }
        mCaptured = mFrame.clone();
        Core.rectangle(mFrame, new Point(130, 90), new Point(190, 150), new Scalar(255, 0, 0), LINE_WIDTH, 8, 0);

        Log.d(L.TAG, "onCameraFrame col:" + mFrame.cols() + ", rows:" + mFrame.rows());

        return mFrame;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(L.TAG, "called onCreateOptionsMenu");
        menu.add(R.string.selected);
        menu.add(R.string.capture_again);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(L.TAG, "called onOptionsItemSelected; selected item: " + item);
        String title = item.getTitle().toString();
        if (title.equals(getString(R.string.selected))) {
            finishSelecting();
            setResult(999);
            finish();
        } else if (title.equals(getString(R.string.capture_again))) {
            startCameraMode();
        }
        return true;
    }

    private void startCameraMode() {
        mState = State.CAMERA;
        mOpenCvCameraView.enableView();
        finishCaptureButton.setVisibility(View.VISIBLE);
    }

    private void finishSelecting() {
        Log.d(L.TAG, "imwrite");
        Mat cropped = mCaptured.submat(new Rect(130, 90, 60, 60));
        Highgui.imwrite(mMarkerFile.getAbsolutePath(), cropped);
    }

    private void startSelectMode() {
        mState = State.SELECTING;
        mOpenCvCameraView.disableView();
        openOptionsMenu();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.button_capture:
            startSelectMode();
            break;
        default:
            break;
        }
    }
}
