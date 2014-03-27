#include "Draw.h"

namespace Apps
{	
	Draw::Draw ():notFindCount(0),prevfind(false),color(CV_RGB(255,255,255)) {
	}
	
	void Draw::trackMarker (Mat& destImage, Point& _r, Point& _b, Point& _g, Point& _y) {
        if (destImage.cols != image.cols || destImage.rows != image.rows) {
            LOGD("resize image\n");
            image = Mat(destImage.size(), destImage.type());
        }
		if (_r.x >= 0 && _b.x < 0) {
			// find only red
			//cvRectangle(dst, cvPoint(p.x-2, p.y-2), cvPoint(p.x+2, p.y+2), color, 3);
			if (prevfind){
				line(image, prevPoint, _r, color, THICKNESS);
			} else {
				// if there is no prev point, do nothing
			}
			prevPoint = Point(_r);
			prevfind = true;
			notFindCount = 0;
		} else {
			if(++notFindCount >= ALLOW_ERROR_COUNT) {
				prevfind = false;
			}
		}
        //image.copyTo(destImage);
        addWeighted(destImage, 0.5, image, 0.5, 0.0, destImage);
	}
}