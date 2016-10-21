package com.alphalayout.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alhpalayout.AlphaLayout;
import com.alphalayout.R;
import com.alphalayout.adapters.ListAdapter;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import butterknife.BindView;

/**
 * @author lu.meng
 */
public class ListSampleActivity extends BaseActivity implements AlphaLayout.OnRefreshListener {

    @BindView(R.id.alpha_layout)
    protected AlphaLayout alphaLayout;

    @BindView(R.id.title_header)
    TextView mTitleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initListView();
        alphaLayout.setOnRefreshListener(this);
    }

    /**
     * {@link ListView} initialize
     */
    private void initListView() {
        ListView listView = (ListView) findViewById(R.id.list_view);
        View headerLayout = LayoutInflater.from(this)
                .inflate(R.layout.layout_viewpager, null);
        SliderLayout sliderLayout = (SliderLayout) headerLayout.findViewById(R.id.slider);

        for (int resId : mPagerDatas) {
            DefaultSliderView mSliderView = new DefaultSliderView(this);
            mSliderView
                    .image(resId)
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            mSliderView.bundle(new Bundle());
            mSliderView.getBundle().putInt("position", mPagerDatas.indexOf(resId));
            sliderLayout.addSlider(mSliderView);
        }

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderLayout.setDuration(3000);
        ListAdapter adapter = new ListAdapter(this, R.layout.list_item, mSampleDatas);
        adapter.setViewPager(sliderLayout);
        listView.setAdapter(adapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_listsample;
    }

    @Override
    protected boolean isTransparentStatusBar() {
        return true;
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
