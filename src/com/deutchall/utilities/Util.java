package com.deutchall.utilities;

import android.graphics.Bitmap;

public class Util {

	public static final int ANDROID_4 = 4;
	public static final int androidVersion = Integer.parseInt(android.os.Build.VERSION.RELEASE.substring(0, 1));
	
    public static Bitmap ScaleBitmap(Bitmap bm, float scalingFactor) {
    	
        int scaleHeight = (int) (bm.getHeight() * scalingFactor);
        int scaleWidth = (int) (bm.getWidth() * scalingFactor);

        return Bitmap.createScaledBitmap(bm, scaleWidth, scaleHeight, true);
    }
    
    public static int getChildPosition(int position, int last) {
    	int realPosition;
    	if (androidVersion >= ANDROID_4) {
			realPosition = position;
		} else {
			realPosition = last - position;
		}
    	return realPosition;
    }

}
