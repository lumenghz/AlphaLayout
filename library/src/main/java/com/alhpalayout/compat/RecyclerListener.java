package com.alhpalayout.compat;

import android.support.v7.widget.RecyclerView;

/**
 * @author lu.meng
 */

public class RecyclerListener extends RecyclerView.OnScrollListener {

    private ScrollListenerCompat.AlphaScrollListener alphaScrollListener;

    public RecyclerListener(ScrollListenerCompat.AlphaScrollListener alphaScrollListener) {
        this.alphaScrollListener = alphaScrollListener;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dx != 0)
            throw new RuntimeException("Sorry, we only support vertical direction now");

        if (null != alphaScrollListener)
            alphaScrollListener.onScroll(Math.abs(dy));
    }
}
