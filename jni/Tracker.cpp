#include "Tracker.h"
//#define AREA_THRESHOLD 4000
#define AREA_THRESHOLD 1000

namespace Apps
{
    /**
     * get HS only Histogram from IplImage
     */
    void getHSHist(Mat& img, MatND& hist) {
        // Compute HSV image and separate into colors
        Mat hsv;
        cvtColor(img, hsv, CV_BGR2HSV);

        // copied the code from fancy demo linked from here:
        // http://docs.opencv.org/doc/tutorials/imgproc/histograms/back_projection/back_projection.html
        // Build and fill the histogram
        int hbins = 30, sbins = 32;
        int histSize[] = { hbins, sbins };
        float hranges[] = { 0, 179 };
        float sranges[] = { 0, 255 };
        const float* ranges[] = { hranges, sranges };
        int channels[] = {0, 1};
        calcHist(&hsv, 1, channels, Mat(), // do not use mask
                 hist, 2, histSize, ranges);
        
        return;
    }

    /**
     * find rectangle which represents the marker by histogram from image
     */
    Rect findMarker (Mat& img, MatND& hist, int* find) {
        Mat hsv;
        cvtColor(img, hsv, CV_BGR2HSV);
        Mat backProject(hsv.size(), CV_8UC1);
        float hranges[] = { 0, 179 };
        float sranges[] = { 0, 255 };
        const float* ranges[] = { hranges, sranges };
        int channels[] = {0, 1};
        calcBackProject(&hsv, 1, channels, hist, backProject, ranges, 1, true);// Calculate back projection
        
        // do we need it?
        //cvNormalizeHist(hist, 1.0); // Normalize it
        
        threshold(backProject, backProject, 30, 255, CV_THRESH_BINARY);
        
        //cvMorphologyEx(back_img, back_img, 0, 0, CV_MOP_OPEN);
        //cvMorphologyEx(back_img, back_img, 0, 0, CV_MOP_CLOSE);
        
        // smoothing is really important to detect "rough" contour
        GaussianBlur(backProject, backProject, Size(3,3), 1, 1);
        
        vector<vector<Point> > contours;
        vector<Vec4i> hierarchy;
        findContours(backProject, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE, Point(0, 0));
        
        //Find max contours rect
        Rect maxRect;
        double maxArea = 0.0F;
        for (int i = 0; i < contours.size(); i++) {
            Rect rect = boundingRect(Mat(contours[i]));
#if DEBUG
            drawContours(img, contours, i, Scalar(255, 255, 255), 2, 8, hierarchy, 0, Point(0, 0));
#endif
            // LOGD("draw bounding: %d %d %d %d\n", rect.x, rect.y, rect.width, rect.height);
            double area = contourArea(contours[i]);
            if (area > maxArea) {
                maxRect = rect;
                maxArea = area;
            }
            //LOGD("area: %f\n", area);
        }
        
        if (maxArea > AREA_THRESHOLD) {
            LOGD("find! %f\n", maxArea);
            *find = 1;
        } else {
            LOGD("not find... %f\n", maxArea);
            *find = 0;
        }
        return maxRect;
    }

    /**
     * find center of CvRect
     */
    inline Point center (Rect rect) {
        return Point(rect.x+rect.width/2, rect.y+rect.height/2);
    }

    void Tracker::setSize(int srcWidth, int srcHeight, int destWidth, int destHeight) {
        srcSize = Size(srcWidth, srcHeight);
        destSize = Size(destWidth, destHeight);
        wRatio = (double)destWidth / (double)srcWidth;
        hRatio = (double)destHeight / (double)srcHeight;
    }

    Tracker::Tracker (string imageFileName) {
    	Mat image = imread(imageFileName);
        getHSHist(image, targetHist);
        setSize(IN_WIDTH, IN_HEIGHT, WIDTH, HEIGHT);
    }

    Tracker::~Tracker() {
    }

    void Tracker::process (Mat& src, Mat& dst, vector<Rect> &targetRectVec) {
        //cvFlip (frame, frame, 1);
        //dst = frame;

        resize(src, dst, destSize);

        int targetFind = 0;
        Rect targetRect = findMarker(src, targetHist, &targetFind);

        LOGD("find=%d\n", targetFind);

        Point p = center(targetRect);
        p.x = p.x * wRatio; p.y = p.y * hRatio;

        if (targetFind) {
        	targetRectVec.push_back(targetRect);
        	rectangle(dst, targetRect, CV_RGB(255,0,0), 3);
        }
    }
}
