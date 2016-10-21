package com.alphalayout.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alphalayout.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author glority - lu.meng
 */

abstract class BaseActivity extends AppCompatActivity {

    public static final int REFRESH_DELAY = 2000;

    protected static final String ICON = "icon";
    protected static final String COLOR = "color";
    protected static final String TITLE = "title";

    protected List<Map<String, Object>> mSampleDatas;

    protected Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mUnbinder = ButterKnife.bind(this);

        if (isTransparentStatusBar())
            transparentStatusBar();

        initDataSet();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void initDataSet() {
        Map<String, Object> mMap;
        mSampleDatas = new ArrayList<>();

        int[] icons = {
                R.drawable.analytics,
                R.drawable.clock,
                R.drawable.coding,
                R.drawable.desk_lamp,
                R.drawable.marker,
                R.drawable.projector,
                R.drawable.usb
        };

        int[] colors = {
                R.color.analytics,
                R.color.clock,
                R.color.coding,
                R.color.desk_lamp,
                R.color.marker,
                R.color.projector,
                R.color.usb
        };

        String[] titles = new String[] {
                "Analytics", "Clock", "Coding", "DeskLamp", "Marker", "Projector", "USB"
        };

        for (int i = 0; i < icons.length; i++) {
            mMap = new HashMap<>();
            mMap.put(ICON, icons[i]);
            mMap.put(COLOR, colors[i]);
            mMap.put(TITLE, titles[i]);
            mSampleDatas.add(mMap);
        }
    }

    /**
     * Transparent status bar color
     */
    private void transparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected abstract boolean isTransparentStatusBar();

    @LayoutRes
    protected abstract int getLayoutId();
}
