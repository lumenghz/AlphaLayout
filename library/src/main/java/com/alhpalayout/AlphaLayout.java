package com.alhpalayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.alhpalayout.compat.ScrollListenerCompat;
import com.alhpalayout.refreshview.BaseRefreshView;
import com.alhpalayout.refreshview.RocketRefreshView;

import static com.alhpalayout.utils.ScreenUtil.dp2Pixel;

/**
 * @author lu.meng
 */
public class AlphaLayout extends ViewGroup implements ScrollListenerCompat.AlphaScrollListener {
    public static final int DIRECTION_UP = 0;
    public static final int DIRECTION_DOWN = 1;
    public static final int DEFAULT_MAX_TRANSPARENT_DISTANCE = 400;

    private static final int DRAG_MAX_DISTANCE = 140;
    private static final float DRAG_RATE = .5f;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;

    public static final int MAX_OFFSET_ANIMATION_DURATION = 700;

    private static final int INVALID_POINTER = -1;

    private int mTransparentDistance;
    private int mTotalDragDistance;
    private int mTouchSlop;

    private int mTargetPaddingTop;
    private int mTargetPaddingBottom;
    private int mTargetPaddingRight;
    private int mTargetPaddingLeft;

    private int mFrom;
    private int mCurrentOffsetTop;
    private int mActivePointerId;

    private int mHeaderLayoutId;

    private float mFromDragPercent;
    private float mCurrentDragPercent;
    private float mInitialMotionY;

    private boolean mRefreshing;
    private boolean mNotify;
    private boolean mIsBeingDragged;

    private View mTarget;
    private View mHeaderLayout;

    private ImageView mRefreshView;

    private BaseRefreshView mBaseRefreshView;

    private Interpolator mDecelerateInterpolator;

    private OnRefreshListener onRefreshListener;

    public interface OnRefreshListener {
        void onRefresh();

        void onScroll(int direction, float percent);
    }

    public AlphaLayout(Context context) {
        this(context, null);
    }

    public AlphaLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflateRefreshView(context);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AlphaLayout);
        mHeaderLayoutId = a.getResourceId(R.styleable.AlphaLayout_headerLayout, 0);
        mTransparentDistance = a.getDimensionPixelOffset(R.styleable.AlphaLayout_transparent_distance, DEFAULT_MAX_TRANSPARENT_DISTANCE);
        a.recycle();

        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        setWillNotDraw(false);
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
    }

    @Override
    protected void onFinishInflate() {
        inflateHeaderLayout(getContext(), mHeaderLayoutId);
        ensureTarget();
        new ScrollListenerCompat().initWithListener(this).setListener(mTarget);
    }

    /**
     * {@link BaseRefreshView} initialize
     */
    private void inflateRefreshView(Context context) {
        mRefreshView = new ImageView(context);
        setRefreshing(false);
        mBaseRefreshView = new RocketRefreshView(getContext(), this);
        mTotalDragDistance = dp2Pixel(context, DRAG_MAX_DISTANCE);
        mRefreshView.setImageDrawable(mBaseRefreshView);
        addView(mRefreshView);
    }

    /**
     * {@link #mHeaderLayout} initialize
     *
     * @param context        context
     * @param headerLayoutId resId of headerLayout
     */
    private void inflateHeaderLayout(Context context, @LayoutRes int headerLayoutId) {
        mHeaderLayout = LayoutInflater.from(context).inflate(headerLayoutId, this, false);
        mHeaderLayout.getBackground().setAlpha(0);
        addView(mHeaderLayout);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ensureTarget();
        if (mTarget == null) return;

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingBottom() - getPaddingTop(), MeasureSpec.EXACTLY);

        mTarget.measure(widthMeasureSpec, heightMeasureSpec);
        mRefreshView.measure(widthMeasureSpec, heightMeasureSpec);
        if (null != mHeaderLayout)
            measureChild(mHeaderLayout, widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        ensureTarget();

        if (null == mTarget) return;

        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();
        int headerHeight = mHeaderLayout.getMeasuredHeight();

        mTarget.layout(left, top + mCurrentOffsetTop, left + width - right, top + height - bottom + mCurrentOffsetTop);
        mRefreshView.layout(left, top, left + width - right, top + height - bottom);
        if (null != mHeaderLayout)
            mHeaderLayout.layout(left, top, left + width - right, top + headerHeight - bottom);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnabled() || canChildScrollUp() || mRefreshing)
            return false;

        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setTargetOffsetTop(0, true);
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                final float initialMotionY = getMotionEventY(ev, mActivePointerId);
                if (initialMotionY == -1)
                    return false;
                mInitialMotionY = initialMotionY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER)
                    return false;

                final float y = getMotionEventY(ev, mActivePointerId);
                if (-1 == y)
                    return false;
                final float yDiff = y - mInitialMotionY;
                if (yDiff > mTouchSlop && !mIsBeingDragged)
                    mIsBeingDragged = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
        }

        return mIsBeingDragged;
    }

    @Override
    public void onScroll(int distance) {
        int boundedDistance = Math.min(mTransparentDistance, Math.abs(distance));

        float percent;
        if (boundedDistance == 0)
            percent = 0.0f;
        else
            percent = (float) Math.abs(boundedDistance) / (float) mTransparentDistance;

        if (null != onRefreshListener)
            onRefreshListener.onScroll(DIRECTION_UP, percent);
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = ev.findPointerIndex(activePointerId);
        if (index < 0)
            return -1;
        return ev.getY(index);
    }

    /**
     * Secondary pointer up event
     *
     * @param ev event
     */
    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (!mIsBeingDragged)
            return super.onTouchEvent(event);

        final int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = event.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0)
                    return false;

                final float y = event.getY(pointerIndex);
                final float yDiff = y - mInitialMotionY;
                final float scrollTop = yDiff * DRAG_RATE;
                mCurrentDragPercent = scrollTop / mTotalDragDistance;
                if (mCurrentDragPercent < 0)
                    return false;
                float boundedDragPercent = Math.min(1f, Math.abs(mCurrentDragPercent));
                float extraOS = Math.abs(scrollTop) - mTotalDragDistance;
                float slingshotDist = mTotalDragDistance;
                float tensionSlingshotPercent = Math.max(0,
                        Math.min(extraOS, slingshotDist * 2) / slingshotDist);
                float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow(
                        (tensionSlingshotPercent / 4), 2)) * 2f;
                float extraMove = (slingshotDist) * tensionPercent / 2;
                int targetY = (int) ((slingshotDist * boundedDragPercent) + extraMove);

                if (null != onRefreshListener)
                    onRefreshListener.onScroll(DIRECTION_DOWN, boundedDragPercent);

                mBaseRefreshView.setPercent(mCurrentDragPercent, true);
                setTargetOffsetTop(targetY - mCurrentOffsetTop, true);
                break;
            }
            case MotionEventCompat.ACTION_POINTER_DOWN:
                final int index = MotionEventCompat.getActionIndex(event);
                mActivePointerId = event.getPointerId(index);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                final int pointerIndex = event.findPointerIndex(mActivePointerId);
                final float y = event.getY(pointerIndex);
                final float overScrollTop = (y - mInitialMotionY) * DRAG_RATE;
                mIsBeingDragged = false;
                if (overScrollTop > mTotalDragDistance) {
                    setRefreshing(true, true);
                } else {
                    mRefreshing = false;
                    animateOffsetToStartPosition();
                }
                mActivePointerId = INVALID_POINTER;
                return false;
            }
        }

        return true;
    }

    /**
     * Whether child can scroll up
     *
     * @return true if can scroll up, false otherwise
     */
    private boolean canChildScrollUp() {
        if (Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0 && (
                        absListView.getFirstVisiblePosition() > 0
                                || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
            } else {
                return mTarget.getScrollY() > 0;
            }
        } else
            return ViewCompat.canScrollVertically(mTarget, -1);
    }

    /**
     * @return total {@link #mTotalDragDistance}
     */
    public int getTotalDragDistance() {
        return mTotalDragDistance;
    }

    public void setRefreshing(boolean refreshing) {
        if (mRefreshing != refreshing) {
            setRefreshing(refreshing, false);
        }
    }

    /**
     * invalidate refresh status
     *
     * @param refreshing status
     * @param notify     notify user
     */
    private void setRefreshing(boolean refreshing, final boolean notify) {
        if (mRefreshing != refreshing) {
            mNotify = notify;
            ensureTarget();
            mRefreshing = refreshing;
            if (mRefreshing) {
                mBaseRefreshView.setPercent(1f, true);
                animateOffsetToCorrectPosition();
            } else {
                animateOffsetToStartPosition();
            }
        }
    }

    /**
     * Make sure the target is child of {@link AbsListView}
     */
    private void ensureTarget() {
        if (mTarget != null) return;

        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child != mRefreshView && (null != mHeaderLayout && child != mHeaderLayout)) {
                    mTarget = child;
                    mTargetPaddingBottom = mTarget.getPaddingBottom();
                    mTargetPaddingLeft = mTarget.getPaddingLeft();
                    mTargetPaddingRight = mTarget.getPaddingRight();
                    mTargetPaddingTop = mTarget.getPaddingTop();
                }
            }
        }
    }

    private void animateOffsetToStartPosition() {
        mFromDragPercent = mCurrentDragPercent;
        mFrom = mCurrentOffsetTop;
        long animationDuration = Math.abs((long) (MAX_OFFSET_ANIMATION_DURATION * mFromDragPercent));

        mAnimateToStartPosition.reset();
        mAnimateToStartPosition.setDuration(animationDuration);
        mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
        mAnimateToStartPosition.setAnimationListener(mToStartListener);
        mRefreshView.clearAnimation();
        mRefreshView.startAnimation(mAnimateToStartPosition);
    }

    private void animateOffsetToCorrectPosition() {
        mFrom = mCurrentOffsetTop;
        mFromDragPercent = mCurrentDragPercent;

        mAnimateToCorrectPosition.reset();
        mAnimateToCorrectPosition.setDuration(MAX_OFFSET_ANIMATION_DURATION);
        mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);

        mRefreshView.clearAnimation();
        mRefreshView.startAnimation(mAnimateToCorrectPosition);

        if (mRefreshing) {
            mBaseRefreshView.start();
            if (mNotify)
                if (null != onRefreshListener)
                    onRefreshListener.onRefresh();

        } else {
            mBaseRefreshView.stop();
            animateOffsetToStartPosition();
        }

        mCurrentOffsetTop = mTarget.getTop();
        mTarget.setPadding(mTargetPaddingLeft, mTargetPaddingTop, mTargetPaddingRight, mTargetPaddingBottom);
    }

    private final Animation mAnimateToStartPosition = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            moveToStart(interpolatedTime);
        }
    };

    private final Animation mAnimateToCorrectPosition = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int targetTop;
            int endTarget = mTotalDragDistance;
            targetTop = (mFrom + (int) ((endTarget - mFrom) * interpolatedTime));
            int offset = targetTop - mTarget.getTop();

            mCurrentDragPercent = mFromDragPercent - (mFromDragPercent - 1.0f) * interpolatedTime;
            mBaseRefreshView.setPercent(mCurrentDragPercent, false);
            setTargetOffsetTop(offset, false);
        }
    };

    private Animation.AnimationListener mToStartListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mBaseRefreshView.stop();
            mCurrentOffsetTop = mTarget.getTop();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };

    /**
     * Move view to start position if need
     *
     * @param interpolatedTime interpolatedTime
     */
    private void moveToStart(float interpolatedTime) {
        int targetTop = mFrom - (int) (mFrom * interpolatedTime);
        float targetPercent = mFromDragPercent * (1.0f - interpolatedTime);
        int offset = targetTop - mTarget.getTop();

        mCurrentDragPercent = targetPercent;
        mBaseRefreshView.setPercent(mCurrentDragPercent, true);
        mTarget.setPadding(mTargetPaddingLeft, mTargetPaddingTop, mTargetPaddingRight, mTargetPaddingBottom + targetTop);
        if (null != onRefreshListener)
            onRefreshListener.onScroll(DIRECTION_DOWN, mCurrentDragPercent);
        setTargetOffsetTop(offset, false);
    }

    private void setTargetOffsetTop(int offset, boolean requiresUpdate) {
        mTarget.offsetTopAndBottom(offset);
        mBaseRefreshView.offsetTopAndBottom(offset);
        mCurrentOffsetTop = mTarget.getTop();
        if (requiresUpdate && Build.VERSION.SDK_INT < 11) {
            invalidate();
        }
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public void setmTransparentDistance(int mTransparentDistance) {
        this.mTransparentDistance = mTransparentDistance;
    }

    public View getHeaderLayout() {
        return mHeaderLayout;
    }

}
