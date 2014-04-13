package com.polysfactory.handgesture;

import java.io.File;
import java.util.List;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.polysfactory.handgesture.HandGestureDetector.HandGestureListener;

public class MainActivity extends Activity implements CvCameraViewListener2, HandGestureListener {

    private static final int REQUEST_CAPTURE_IMAGE = 100;
    private Mat mFrame;
    private NativeBridge mNativeDetector;

    private CameraBridgeViewBase mOpenCvCameraView;

    private MenuItem mCaptureTargetMenu;
    private ViewGroup mTargetImagePreview;

    // private CardScrollView mCardScrollView;
    private ViewPager mCardScrollView;
    private ViewGroup mContainer;
    private HandGestureDetector mHandGestureDetector;

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
        // mOpenCvCameraView.setMaxFrameSize(640, 360);
        mOpenCvCameraView.setMaxFrameSize(320, 192);

        mTargetImagePreview = (ViewGroup) findViewById(R.id.marker_preview_container);

        mContainer = (ViewGroup) findViewById(R.id.container);
        mCardScrollView = new ViewPager(this);
        mCardScrollView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openOptionsMenu();

            }
        });
        mCardScrollView.setAdapter(new SamplePagerAdapter(this));
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        // LayoutParams lp = new LayoutParams(160, 90);
        mContainer.addView(mCardScrollView, 2, lp);

        mHandGestureDetector = new HandGestureDetector();
        mHandGestureDetector.setHandGestureListener(this);
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
        setupTargetImagePreview();
        mNativeDetector = new NativeBridge(Marker.HAND.getFilePath(this));
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

        mFrame = inputFrame.rgba();
        if (Constants.FLIP) {
            Core.flip(mFrame, mFrame, 1);
        }

        MatOfRect handRectMat = new MatOfRect();
        if (mNativeDetector != null) {
            mNativeDetector.process(mFrame, handRectMat);
        }
        List<Rect> handRectList = handRectMat.toList();
        if (handRectList.size() > 0) {
            mHandGestureDetector.handle(handRectList.get(0));
        } else {
            mHandGestureDetector.handle(null);
        }

        return mFrame;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_CAMERA) {
            // Stop the preview and release the camera.
            // Execute your logic as quickly as possible
            // so the capture happens quickly.
            // TODO
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mCaptureTargetMenu = menu.add(R.string.capture_marker);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mCaptureTargetMenu == item) {
            Intent intent = new Intent(this, CaptureActivity.class);
            intent.putExtra(CaptureActivity.EXTRA_KEY_MARKER, Marker.HAND);
            startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CAPTURE_IMAGE == requestCode) {
            // TODO reload marker
        }
    }

    private void setupTargetImagePreview() {
        mTargetImagePreview.removeAllViews();
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
            mTargetImagePreview.addView(imageView);
        }
    }

    @Override
    public void onLeftMove() {
        Log.d(L.TAG, "onLeftMove");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int selected = mCardScrollView.getCurrentItem();
                if (selected > 0) {
                    mCardScrollView.setCurrentItem(selected - 1, true);
                }
            }
        });
    }

    @Override
    public void onRightMove() {
        Log.d(L.TAG, "onRightMove");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int selected = mCardScrollView.getCurrentItem();
                if (selected < mCardScrollView.getAdapter().getCount()) {
                    mCardScrollView.setCurrentItem(selected + 1, true);
                }
            }
        });
    }

}
