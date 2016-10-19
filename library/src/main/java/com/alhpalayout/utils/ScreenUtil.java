package com.alhpalayout.utils;

import android.content.Context;
import android.content.res.Resources;

/**
 * @author lu.meng
 */
public class ScreenUtil {

    public static int dp2Pixel(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            result = resources.getDimensionPixelSize(resourceId);
        else
            result = dp2Pixel(context, 25);

        return result;
    }
}
