package com.vst.androiduidemo.widget.kslidingmenu;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.vst.androiduidemo.R;

/**
 * 抽屉侧滑
 */
public class DrawerSlideMenu extends HorizontalScrollView {

    private static final String TAG = KSlideMenu.class.getSimpleName();
    //ScrollView下的包裹，一般是LinearLayout
    private LinearLayout mWrapperView;
    private ViewGroup mMenuView;
    private ViewGroup mContentView;

    private int mScreenWidth;
    private int mRightMargin;//dp
    private int mMenuViewWidth;
    private boolean isOne = true;


    public DrawerSlideMenu(Context context) {
        this(context, null);
    }

    public DrawerSlideMenu(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DrawerSlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.KSlideMenu, defStyleAttr, 0);
        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.KSlideMenu_rightMargin:
                    mRightMargin = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics()));
                    break;
            }
        }
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isOne) {
            initView();
            isOne = false;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initView() {
        mWrapperView = (LinearLayout) getChildAt(0);
        mMenuView = (ViewGroup) mWrapperView.getChildAt(0);
        mContentView = (ViewGroup) mWrapperView.getChildAt(1);

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;

        mMenuViewWidth = mMenuView.getLayoutParams().width = mScreenWidth - mRightMargin;
        mContentView.getLayoutParams().width = mScreenWidth;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            scrollTo(mMenuViewWidth, 0);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
//                smoothScrollTo(0, 0);
//                Log.i(TAG,"getScaleX:"+getScaleX())
                if (getScrollX() >= (mMenuViewWidth / 2)) {
                    smoothScrollTo(mMenuViewWidth, 0);
                } else {
                    smoothScrollTo(0, 0);
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    public void toggle() {
        if (getScrollX() == mMenuViewWidth) {
            smoothScrollTo(0, 0);
        } else {
            smoothScrollTo(mMenuViewWidth, 0);
        }
    }

    //跟普通侧滑的区别在这里
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
//        mMenuView.animate().x(l);
        ObjectAnimator.ofFloat(mMenuView, TRANSLATION_X, l).setDuration(0).start();
    }
}
