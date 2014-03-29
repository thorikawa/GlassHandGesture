#ifndef _GUNMAN_H_
#define _GUNMAN_H_

#include <opencv2/opencv.hpp>
#include <opencv2/legacy/compat.hpp>
#include "common.h"

using namespace std;
using namespace cv;

namespace Apps
{
    class Tracker {
    private:
        MatND targetHist;
        double wRatio;
        double hRatio;
        void init ();
        Size srcSize;
        Size destSize;
    public:
        Tracker(string imageFileName);
        ~Tracker();
        void process (Mat &src, Mat &dst, vector<Rect> &targetRectVec);
        void setSize(int srcWidth, int srcHeight, int destWidth, int destHeight);
  };
}

#endif
