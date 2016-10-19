package com.alhpalayout.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.alhpalayout.R;
import com.alhpalayout.utils.ScreenUtil;

/**
 * @author lu.meng
 */

public class AlphaHeaderLayout extends RelativeLayout {

    private int statusbarHeight;

    private boolean considerPadding = true;

    public AlphaHeaderLayout(Context context) {
        super(context);
        init(context, null);
    }

    public AlphaHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AlphaHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AlphaHeaderLayout);
        considerPadding = array.getBoolean(R.styleable.AlphaHeaderLayout_considerPadding, true);
        array.recycle();

        initialStatusbarHeight();
    }

    private void initialStatusbarHeight() {
        statusbarHeight = ScreenUtil.getStatusBarHeight(getContext());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (!considerPadding) return;

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) continue;

            int xOffset = 0;
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            final int[] rules = params.getRules();
            if (rules[CENTER_IN_PARENT] != 0 || rules[CENTER_VERTICAL] != 0)
                xOffset = (getPaddingTop() - getPaddingBottom()) / 2;

            if (xOffset != 0)
                child.offsetTopAndBottom(xOffset);
        }
    }

    @Override
    public int getPaddingTop() {
        return super.getPaddingTop() + statusbarHeight;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return super.getSuggestedMinimumHeight();
    }
}
