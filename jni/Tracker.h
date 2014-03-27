#ifndef _GUNMAN_H_
#define _GUNMAN_H_

#include <opencv2/opencv.hpp>
#include <opencv2/legacy/compat.hpp>
#include "common.h"
#include "Draw.h"

using namespace std;
using namespace cv;

namespace Apps
{
    class Tracker {
    private:
        MatND rHist;
        MatND yHist;
        Draw* draw;
        double wRatio;
        double hRatio;
        void init ();
        Size srcSize;
        Size destSize;
    public:
        Tracker(string imageFileName);
        Tracker(string rMarkerImage, string yMarkerImage);
        ~Tracker();
        void process (Mat &src, Mat &dst);
        void findMarkers (Mat& src, vector<Rect>& rRect, vector<Rect>& bRect);
        void setSize(int srcWidth, int srcHeight, int destWidth, int destHeight);
  };
}

#endif