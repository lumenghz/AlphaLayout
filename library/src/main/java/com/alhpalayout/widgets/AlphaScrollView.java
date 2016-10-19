package com.alhpalayout.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.alhpalayout.compat.ScrollListenerCompat;

/**
 * @author lu.meng
 */
public class AlphaScrollView extends ScrollView {

    private ScrollListenerCompat.AlphaScrollListener alphaScrollListener;

    public AlphaScrollView(Context context) {
        super(context);
    }

    public AlphaScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlphaScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public AlphaScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (null != alphaScrollListener) {
            alphaScrollListener.onScroll(this.getScrollY());
        }
    }

    public void setAlphaScrollListener(ScrollListenerCompat.AlphaScrollListener alphaScrollListener) {
        this.alphaScrollListener = alphaScrollListener;
    }
}
