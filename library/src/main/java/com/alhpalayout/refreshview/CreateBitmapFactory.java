package com.alhpalayout.refreshview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;

/**
 * @author lu.meng
 */
public class CreateBitmapFactory {

    public static BitmapFactory.Options mOptions;

    {
        mOptions = new BitmapFactory.Options();
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    static Bitmap getBitmapFromImage(@DrawableRes int id, Context context) {
        return BitmapFactory.decodeResource(context.getResources(), id, mOptions);
    }
}
