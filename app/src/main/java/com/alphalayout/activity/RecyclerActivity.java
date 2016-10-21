package com.alphalayout.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alhpalayout.AlphaLayout;
import com.alphalayout.R;
import com.alphalayout.adapters.RecyclerAdapter;

import butterknife.BindView;

/**
 * @author glority - lu.meng
 */
public class RecyclerActivity extends BaseActivity implements AlphaLayout.OnRefreshListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.alpha_layout)
    AlphaLayout alphaLayout;

    @BindView(R.id.title_header)
    TextView mTitleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initRecyclerView();
        alphaLayout.setOnRefreshListener(this);
    }

    @Override
    protected boolean isTransparentStatusBar() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recyclersample;
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerAdapter(mSampleDatas));
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
