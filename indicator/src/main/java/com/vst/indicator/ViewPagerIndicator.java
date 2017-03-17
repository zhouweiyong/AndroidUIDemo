package com.vst.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2017/3/15
 * class description:请输入类描述
 */
public class ViewPagerIndicator extends LinearLayout {
    private static final String TAG = ViewPagerIndicator.class.getSimpleName();
    private ViewPager mViewPager;
    /**
     * ViewPager的滑动事件被Indicator使用，如果要监听ViewPager的滑动事件，需要监听Indicator的事件
     */
    private OnIndicatorPageChangeListener mOnIndicatorPageChangeListener;
    //默认显示4个tab
    private static final int DEFAULT_VISIBLE_COUNT = 4;
    //可见的tab数量
    private int mVisibleCount;
    //屏幕宽度
    private int mScreenWidth;
    //每个tab的宽度
    private int mTabWidth;
    //Indicator的画笔
    private Paint mIndicatorPaint;
    //绘制Indicator的路径
    private Path mIndicatorPath;
    //绘制Indicator的画布X轴偏移量
    private float mTranslateX;
    /**
     * 根据滑动记录Indicator的起始和结束的x坐标
     * 用以绘制Indicator
     * 但这种绘制方法在tab可见数量为2时会出现问题
     * 所以采用了画布偏移的绘制方法
     */
    private float mStartX;
    private float mEndX;
    //用以记录滑动时候的起始x轴坐标
    private int mLastX;
    //用以记录拦截事件的滑动起始x坐标
    private int mInterceptLastX;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        DisplayMetrics dp = getResources().getDisplayMetrics();
        mScreenWidth = dp.widthPixels;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mVisibleCount = typedArray.getInt(R.styleable.ViewPagerIndicator_visible_count, DEFAULT_VISIBLE_COUNT);
        typedArray.recycle();
        mTabWidth = mScreenWidth / mVisibleCount;

        mIndicatorPaint = createPaint(Color.parseColor("#003399"), 10, Paint.Style.STROKE, 5);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(TAG, "onMeasure");
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        /**
         * 测量整个控件的宽度
         */
        int childCount = getChildCount();
        int totalWidth = 0;
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
//            view.measure(mTabWidth, heightMeasureSpec);
            totalWidth += mTabWidth;
        }
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(totalWidth, widthMode);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "w:" + w + " h:" + h + " oldw:" + oldw + " oldh:" + oldh);

        //生成Indicator的绘制路径
        mIndicatorPath = new Path();
        mIndicatorPath.moveTo(0, getHeight());
        mIndicatorPath.lineTo(mTabWidth, getHeight());
        refreshTabColor(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "onDraw");
        /**
         * 根据滑动记录Indicator的起始和结束的x坐标
         * 用以绘制Indicator
         * 但这种绘制方法在tab可见数量为2时会出现问题
         * 所以采用了画布偏移的绘制方法
         */
//        mIndicatorPaint = createPaint(Color.parseColor("#003399"), 10, Paint.Style.STROKE, 5);
//        canvas.save();
//        mIndicatorPath = new Path();
//        mIndicatorPath.moveTo(mStartX, getHeight());
//        mIndicatorPath.lineTo(mEndX, getHeight());
//        canvas.drawPath(mIndicatorPath, mIndicatorPaint);
//        canvas.restore();
        super.onDraw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        /**
         * 绘制Indicator，画布根据滑动进行x轴上的偏移
         */
        canvas.save();
        canvas.translate(mTranslateX, 0);
        canvas.drawPath(mIndicatorPath, mIndicatorPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    /**
     * 设置tab的内容，根据内容动态生成tab
     */
    public void setTab(List<String> tabList) {
        removeAllViews();
        int size = tabList.size();
        for (int i = 0; i < size; i++) {
            TextView tv = createTab(tabList.get(i));
            addView(tv);

            tv.setTag(i);
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem((Integer) v.getTag());
                }
            });
        }
    }

    /**
     * 生成tab
     *
     * @param tab
     * @return
     */
    private TextView createTab(String tab) {
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tv.setLayoutParams(lp);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        tv.setWidth(mTabWidth);
        tv.setGravity(Gravity.CENTER);
        tv.setText(tab);
        return tv;
    }

    /**
     * 设置Indicator跟随的ViewPager
     *
     * @param mViewPager
     */
    public void setViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i(TAG, "positon:" + position);
                if (mOnIndicatorPageChangeListener != null)
                    mOnIndicatorPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);

                //获取Indicator画布偏移量
                mTranslateX = mTabWidth * position + positionOffset * mTabWidth;
                //mStartX = mTabWidth * position + positionOffset * mTabWidth;
                // mEndX = mTabWidth * position + positionOffset * mTabWidth + mTabWidth;
                Log.i(TAG, "mStartX:" + mStartX + " mEndX:" + mEndX);
                if (mVisibleCount >= 2 & position >= mVisibleCount - 2 && positionOffset > 0 && getChildCount() > mVisibleCount && position < getChildCount() - 2) {
                    scrollTo((int) ((position - mVisibleCount + 2) * mTabWidth + mTabWidth * positionOffset), 0);
                }
                invalidate();
            }

            @Override
            public void onPageSelected(int position) {
                if (mOnIndicatorPageChangeListener != null)
                    mOnIndicatorPageChangeListener.onPageSelected(position);

                refreshTabColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mOnIndicatorPageChangeListener != null)
                    mOnIndicatorPageChangeListener.onPageScrollStateChanged(state);
            }
        });
    }


    /**
     * 根据条件进行拦截滑动事件
     * 如果不根据条件进行拦截的话，也可以滑动
     * 但是tab的点击事件也会被拦截失效
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInterceptLastX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int cX = (int) ev.getX();
                int d = Math.abs(mInterceptLastX - cX);
                if (d > 10) return true;
                mInterceptLastX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_UP:
                mInterceptLastX = 0;
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "mLastX:" + mLastX);
                if (mLastX <= 0) {
                    mLastX = (int) event.getRawX();
                }
                int cX = (int) event.getRawX();
                Log.i(TAG, "cX:" + cX);
                int d = mLastX - cX;
                int scrollX = getScrollX();
                /**
                 * 滑动Indicator
                 */
                if ((d < 0 && scrollX > 0) || (d > 0 && scrollX < this.getMeasuredWidth() - mScreenWidth)) {
                    this.scrollBy(d, 0);
                    scrollX = getScrollX();
                    //如果Indicator滑出边界，则自动返回
                    if (scrollX < 0) {//最小边界
                        Log.i(TAG, "回退");
                        scrollTo(0, 0);
                    } else if (scrollX > this.getMeasuredWidth() - mScreenWidth) {//最大边界
                        scrollTo(this.getMeasuredWidth() - mScreenWidth, 0);
                    }
                }
                mLastX = (int) event.getRawX();
                break;
            case MotionEvent.ACTION_UP:
                //一定要记得清空记录的坐标
                mLastX = 0;
                break;
        }
        return true;
    }

    private void refreshTabColor(int position) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            TextView tv = (TextView) getChildAt(i);
            if (i != position) {
                tv.setTextColor(Color.BLACK);
            } else {
                tv.setTextColor(Color.RED);
            }
        }
    }

    public void setOnIndicatorPageChangeListener(OnIndicatorPageChangeListener mOnIndicatorPageChangeListener) {
        this.mOnIndicatorPageChangeListener = mOnIndicatorPageChangeListener;
    }

    /**
     * Indicator事件监听
     */
    public static interface OnIndicatorPageChangeListener {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        public void onPageSelected(int position);

        public void onPageScrollStateChanged(int state);
    }


    /**
     * 生成画笔
     */
    private Paint createPaint(int paintColor, int textSize, Paint.Style style, int lineWidth) {
        Paint paint = new Paint();
        paint.setColor(paintColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(lineWidth);
        paint.setDither(true);
        paint.setTextSize(textSize);
        paint.setStyle(style);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        return paint;
    }
}
