package com.vst.androiduidemo.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2017/3/1
 * class description:请输入类描述
 */
public class ScrollView extends ViewGroup {

    private static final String TAG = ScrollView.class.getSimpleName();
    private Scroller scroller;
    private float lastY;

    public ScrollView(Context context) {
        super(context);
        initView();
    }

    public ScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        scroller = new Scroller(getContext(), new DecelerateInterpolator());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 800);
        View view1 = new View(getContext());
        view1.setBackgroundColor(Color.BLUE);
        addView(view1, layoutParams);

        View view2 = new View(getContext());
        view2.setBackgroundColor(Color.GREEN);
        addView(view2, layoutParams);

        View view3 = new View(getContext());
        view3.setBackgroundColor(Color.GRAY);
        addView(view3, layoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int childCount = getChildCount();
        int totalHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            int measuredWidth = childView.getMeasuredWidth();
            int measuredHeight = childView.getMeasuredHeight();
            totalHeight += measuredHeight;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(totalHeight, heightMode);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int totalHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int measuredWidth = childView.getMeasuredWidth();
            int measuredHeight = childView.getMeasuredHeight();
            childView.layout(0, totalHeight, measuredWidth, totalHeight + measuredHeight);
            totalHeight += measuredHeight;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "ScrollY:" + getScrollY());
                int scrollY = getScrollY();
                int d = (int) (event.getRawY() - lastY);
                lastY = event.getRawY();
                if (scrollY > 0) {
                    //移动相对位置
                    scrollBy(0, -(int) (d / 1.5));
                } else {
                    scrollBy(0, -d / 4);
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "ACTION_UP>>>" + getScrollY());
                if (getScrollY() < 0) {
                    /**
                     * 滚动的动画实现：
                     * 1，前两个参数：滚动前的x和y坐标
                     * 2，x和y即将移动的值，即最终位置的坐标和滚动前的坐标的差值
                     * 3，动画持续的时间
                     * 这是一个动画的辅助实现，最终需要在computeScroll中实现
                     */
                    scroller.startScroll(0, getScrollY(), 0, 0 - getScrollY(), 500);
                }
                //调用computeScroll
                invalidate();
                break;
        }
        return true;
    }

    /**
     * 这是父类的一个空函数，调用scroller后必须实现这个函数
     * scroller调用startScroll方法后scroller.computeScrollOffset()为true
     * scroller.getCurrY()返回的是每个时间段的当前值
     * 最终的返回值为：getScrollY()-（0,0-getScrollY()）
     */
    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            Log.i(TAG, "getCurrY:" + scroller.getCurrY());
            //移动到绝对位置
            scrollTo(0, scroller.getCurrY());
        }
        postInvalidate();
        super.computeScroll();
    }
}
