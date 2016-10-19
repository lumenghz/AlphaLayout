package com.alhpalayout.compat;

import android.view.View;
import android.widget.AbsListView;

/**
 * @author lu.meng
 */
public class AbsListener implements AbsListView.OnScrollListener {

    private ScrollListenerCompat.AlphaScrollListener alphaScrollListener;

    public AbsListener(ScrollListenerCompat.AlphaScrollListener alphaScrollListener) {
        this.alphaScrollListener = alphaScrollListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount == 0 || firstVisibleItem > 0) return;

        View v = view.getChildAt(0);
        int top = (v == null) ? 0 : v.getTop();
        if (null != alphaScrollListener)
            alphaScrollListener.onScroll(Math.abs(top));
    }
}
