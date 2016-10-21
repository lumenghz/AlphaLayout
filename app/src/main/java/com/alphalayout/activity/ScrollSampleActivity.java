package com.alphalayout.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.widget.TextView;

import com.alhpalayout.AlphaLayout;
import com.alphalayout.R;

import butterknife.BindView;

/**
 * @author lu.meng
 */
public class ScrollSampleActivity extends BaseActivity implements AlphaLayout.OnRefreshListener {

    @BindView(R.id.alpha_scroll_sample)
    AlphaLayout alphaLayout;

    @BindView(R.id.title_header)
    TextView mTitleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alphaLayout.setOnRefreshListener(this);
    }

    @Override
    protected boolean isTransparentStatusBar() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scrollsample;
    }

    @Override
    public void onRefresh() {
        alphaLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                alphaLayout.setRefreshing(false);
            }
        }, REFRESH_DELAY);
    }

    @Override
    public void onScroll(int direction, float percent) {
        if (direction == AlphaLayout.DIRECTION_DOWN) {
            ViewCompat.setAlpha(alphaLayout.getHeaderLayout(), 1.0f - percent);
        } else {
            alphaLayout.getHeaderLayout().getBackground().setAlpha((int) (255 * percent));
            mTitleView.getBackground().mutate().setAlpha((int) (255 * (1 - percent)));
        }
    }
}
