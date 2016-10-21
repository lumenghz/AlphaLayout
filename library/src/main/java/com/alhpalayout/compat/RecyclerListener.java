package com.alhpalayout.compat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager instanceof LinearLayoutManager) {
            final int position = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            if (recyclerView.getChildCount() == 0 || position > 0) return;

            View view = recyclerView.getChildAt(0);
            int distance = view == null ? 0 : view.getTop();
            if (null != alphaScrollListener)
                alphaScrollListener.onScroll(Math.abs(distance));
        } else {
            throw new RuntimeException("Sorry, we only support LinearLayoutManager now");
        }
    }
}
