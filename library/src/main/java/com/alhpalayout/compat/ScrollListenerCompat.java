package com.alhpalayout.compat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;

import com.alhpalayout.widgets.AlphaScrollView;

/**
 * @author lu.meng
 */

public class ScrollListenerCompat {

    public interface AlphaScrollListener {
        void onScroll(int distance);
    }

    private AlphaScrollListener mAlphaScrollListener;

    public ScrollListenerCompat initWithListener(AlphaScrollListener alphaScrollListener) {
        mAlphaScrollListener = alphaScrollListener;
        return this;
    }

    public void setListener(View target) {
        if (null == mAlphaScrollListener) {
            throw new RuntimeException("You must call initWithListener() first");
        }

        if (target instanceof AbsListView)
            ((AbsListView) target).setOnScrollListener(new AbsListener(mAlphaScrollListener));
        else if (target instanceof AlphaScrollView)
            ((AlphaScrollView) target).setAlphaScrollListener(mAlphaScrollListener);
        else if (target instanceof RecyclerView)
            ((RecyclerView) target).addOnScrollListener(new RecyclerListener(mAlphaScrollListener));
    }
}
