package com.zwy.mysurfaceview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.text.LocaleDisplayNames;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zwy.mysurfaceview.R;

import java.util.ArrayList;
import java.util.Random;


/**
 * SurfaceView + HandlerThread
 * 1，自定义一个SurfaceView
 * 2，监听SurfaceView的生命周期
 * 3，自定义线程，继承HandlerThread
 * 4，第一线程处理相关的参数
 * 5，监听线程的生命周期
 * 6，通过SurfaceView的生命周期来执行我们的线程，绘制我们的图片
 * 7，添加触摸时间，创建DrawingItem对象
 */
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private final static String TAG = MySurfaceView.class.getSimpleName();
    private DrawThread mDrawThread;

    public MySurfaceView(Context context) {
        this(context, null);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //监听SurfaceView的生命周期
        getHolder().addCallback(this);
    }

    //-------SurfaceView的生命周期--------
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //创建
        mDrawThread = new DrawThread(getHolder(), BitmapFactory.decodeResource(getResources(), R.mipmap.dot));
        mDrawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //改变
        mDrawThread.updateSurfaceViewSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //销毁
        mDrawThread.quit();
        mDrawThread = null;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //监听按下的动作，生存一个新的绘制对象
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mDrawThread.addItem((int) event.getX(), (int) event.getY());
        }
        return super.onTouchEvent(event);
    }

    public void clear(){
        this.mDrawThread.clear();
    }

    class DrawThread extends HandlerThread implements Handler.Callback {
        //创建
        private static final int MSG_ADD = 0x110;
        //移动
        private static final int MSG_DRAW = 0x111;
        //销毁
        private static final int MSG_CLEAR = 0x112;
        //SurfaceView的宽高
        private int mSurfaceViewWidth, mSurfaceViewHeight;
        //缓存视图
        private SurfaceHolder mSurfaceHolder;

        private Paint mPaint;
        //我们需要绘制的图片
        private Bitmap mIconBitmap;
        //绘制对象的集合
        private ArrayList<DrawItem> mDrawItems;
        //判断线程是否在运行
        private boolean isRunning;
        //定义handler，更新ui线程
        private Handler mHandler;


        public DrawThread(SurfaceHolder surfaceHolder, Bitmap bitmap) {
            super("DrawThread");
            this.mSurfaceHolder = surfaceHolder;
            this.mIconBitmap = bitmap;
            this.mDrawItems = new ArrayList<>();
            this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        public void updateSurfaceViewSize(int width, int height) {
            this.mSurfaceViewWidth = width;
            this.mSurfaceViewHeight = height;
        }

        public void addItem(int x, int y) {
            Message message = Message.obtain(mHandler, MSG_ADD, x, y);
            mHandler.sendMessage(message);
        }

        @Override
        protected void onLooperPrepared() {
            super.onLooperPrepared();
            mHandler = new Handler(getLooper(), this);
            isRunning = true;

            //this.mHandler.sendEmptyMessage(MSG_ADD);
        }

        @Override
        public boolean quit() {
            this.isRunning = false;
            this.mHandler.removeCallbacksAndMessages(null);
            return super.quit();
        }

        public void clear(){
            mHandler.sendEmptyMessage(MSG_CLEAR);
        }

        @Override
        public boolean handleMessage(Message msg) {
            Log.i(TAG,Thread.currentThread().getName()+"  "+Thread.currentThread().getId());
            switch (msg.what) {
                case MSG_ADD:
                    Random random = new Random();
                    DrawItem drawItem = new DrawItem(msg.arg1, msg.arg2, random.nextBoolean(), random.nextBoolean());
                    mDrawItems.add(drawItem);
                    break;
                case MSG_DRAW:
                    if (!isRunning)
                        return true;
                    toDrawItem();
                    break;
                case MSG_CLEAR:
                    mDrawItems.clear();
                    break;
            }

            //循环发消息，有这个才能实现动画
            if (isRunning)
                this.mHandler.sendEmptyMessage(MSG_DRAW);

            return false;
        }

        private void toDrawItem() {
            //获取画布
            Canvas lockCanvas = this.mSurfaceHolder.lockCanvas();
            if (lockCanvas == null)
                return;
            //很重要，清空画布
            lockCanvas.drawColor(Color.BLACK);
            for (DrawItem mDrawItem : mDrawItems) {
                mDrawItem.x += mDrawItem.isHorizontal ? 5 : -5;

                if (mDrawItem.x >= this.mSurfaceViewWidth - mIconBitmap.getWidth()) {
                    mDrawItem.isHorizontal = false;
                } else {
                    mDrawItem.isHorizontal = true;
                }
                mDrawItem.y += mDrawItem.isVertical ? 5 : -5;
                if (mDrawItem.y >= this.mSurfaceViewHeight - mIconBitmap.getHeight()) {
                    mDrawItem.isVertical = false;
                } else {
                    mDrawItem.isVertical = true;
                }

                lockCanvas.drawBitmap(mIconBitmap, mDrawItem.x, mDrawItem.y, mPaint);
            }
            //很重要，解锁画布
            this.mSurfaceHolder.unlockCanvasAndPost(lockCanvas);
        }

        //绘制的对象
        class DrawItem {
            int x, y;
            boolean isVertical, isHorizontal;

            public DrawItem(int x, int y, boolean isVertical, boolean isHorizontal) {
                this.x = x;
                this.y = y;
                this.isVertical = isVertical;
                this.isHorizontal = isHorizontal;
            }
        }
    }
}
