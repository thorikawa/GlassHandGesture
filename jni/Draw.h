#ifndef _DRAW_H_
#define _DRAW_H_

#include <opencv2/opencv.hpp>
#include "common.h"

using namespace cv;

namespace Apps
{
	class Draw
	{
	private:
        const static int ALLOW_ERROR_COUNT = 5;
        const static int THICKNESS = 2;
        int notFindCount;
        bool prevfind;
        Point prevPoint;
        Scalar color;
        Mat image;
	public:
		Draw();
		void trackMarker (Mat& destImg, Point& _r, Point& _b, Point& _g, Point& _y);
	};	
}

#endif