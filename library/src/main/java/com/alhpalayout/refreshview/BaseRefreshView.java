package com.alhpalayout.refreshview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

import com.alhpalayout.AlphaLayout;


/**
 * @author lu.meng
 */

public abstract class BaseRefreshView extends Drawable implements Drawable.Callback, Animatable {

    private AlphaLayout mAlphaLayout;

    public BaseRefreshView(Context context, AlphaLayout mAlphaLayout) {
        this.mAlphaLayout = mAlphaLayout;
    }

    protected Context getContext() {
        return mAlphaLayout == null ? null : mAlphaLayout.getContext();
    }

    public abstract void setPercent(float percent, boolean invalidate);

    public abstract void offsetTopAndBottom(int offset);

    protected abstract void initialDimens(int viewWidth);

    protected abstract void setupAnimations();

    @TargetApi(11)
    @Override
    public void invalidateDrawable(Drawable who) {
        final Callback callback = getCallback();
        if (null != callback) {
            callback.invalidateDrawable(who);
        }
    }

    @TargetApi(11)
    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        final Callback callback = getCallback();
        if (null != callback) {
            callback.scheduleDrawable(who, what, when);
        }
    }

    @TargetApi(11)
    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        final Callback callback = getCallback();
        if (null != callback) {
            callback.unscheduleDrawable(who, what);
        }
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
    }

    @Override
    public void setAlpha(int i) {
    }
}
