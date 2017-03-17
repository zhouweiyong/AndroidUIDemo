package com.vst.guaguaka;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2017/3/16
 * class description:请输入类描述
 */
public class GuaGuaKaView2 extends View {
    private static final String TAG = GuaGuaKaView2.class.getSimpleName();
    //刮刮卡的遮盖层和涂抹路径绘制的Bitmap和canvas
    private Bitmap mGuaguaBitmap;
    private Canvas mGuaGuaCanvas;
    //遮盖层图片
    private Bitmap mDstBitmap;
    //画遮盖层的画笔
    private Paint mDstPaint;
    //画涂抹路径的画笔
    private Paint mSrcPaint;
    //绘制图片的画笔
    private Paint mBitmapPaint;
    //绘制背景文字的画笔
    private Paint mBackTextPaint;
    //绘制涂抹的路径
    private Path mSrcPath;
    //记录涂抹的坐标
    private float mStartX, mStartY, mEndX, mEndY;
    //背景文字，遮盖层被涂抹后出现
    private String mText = "Hello World";
    //计算文字的宽高数据
    private Rect mTextRect = new Rect();
    //View的宽高
    private int mViewWith, mViewHeight;
    //标记，判断是否已经完成涂抹
    private boolean isComplete = false;
    //刮刮卡涂抹的监听
    private OnGuaGuaKaListener mOnGuaGuaKaListener;
    private Handler mHandler = new Handler();


    public GuaGuaKaView2(Context context) {
        this(context, null);
    }

    public GuaGuaKaView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuaGuaKaView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        /**
         * 初始化画笔
         */
        mDstPaint = new Paint();
        mSrcPaint = createPaint(Color.parseColor("#000000"), 10, Paint.Style.STROKE, 20);
        mBitmapPaint = new Paint();
        mBackTextPaint = createPaint(Color.BLACK, 30, Paint.Style.FILL, 30);
        //获取背景文字的宽高信息
        mBackTextPaint.getTextBounds(mText, 0, mText.length(), mTextRect);
        //初始化涂抹路径
        mSrcPath = new Path();
        //获取遮盖层图片
        mDstBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img04);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "w:" + w + " h:" + h);
        mViewWith = w;
        mViewHeight = h;
        //生成遮盖层的画板和canvas
        mGuaguaBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mGuaGuaCanvas = new Canvas(mGuaguaBitmap);
        //绘制遮盖层图片
        mGuaGuaCanvas.drawBitmap(mDstBitmap, null, new Rect(0, 0, w, h), mDstPaint);
        //设置遮盖模式，一定要使用涂抹路径的画笔进行设置
        PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        mSrcPaint.setXfermode(porterDuffXfermode);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景的文字
        canvas.drawText(mText, mViewWith / 2 - mTextRect.width() / 2, mViewHeight / 2 + mTextRect.height() / 2, mBackTextPaint);

        if (!isComplete) {//判断是否完成涂抹
            //绘制涂抹路径
            mGuaGuaCanvas.drawPath(mSrcPath, mSrcPaint);
            //把遮盖层的画板绘制到View中
            canvas.drawBitmap(mGuaguaBitmap, null, new Rect(0, 0, mViewWith, mViewHeight), mBitmapPaint);
        }
    }

    /***
     * 监听滑动事件，获取涂抹坐标
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isComplete) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mStartY = event.getY();
                mSrcPath.moveTo(mStartX, mStartY);
                break;
            case MotionEvent.ACTION_MOVE:
                mEndX = event.getX();
                mEndY = event.getY();
                mSrcPath.lineTo(mEndX, mEndY);
                mStartX = event.getX();
                mStartY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                new Thread(mRunnable).start();
                break;
        }
        return true;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    /**
     * 另开线程获取mGuaguaBitmap的像素信息
     * 当涂抹的面积超过60%后，则取消遮盖层，全部显示背景文字
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int[] pixels = new int[mViewWith * mViewHeight];
            int wipeArea = 0;
            int totalArea = mViewWith * mViewHeight;
            //获取Bitmap的像素信息
            mGuaguaBitmap.getPixels(pixels, 0, mViewWith, 0, 0, mViewWith, mViewHeight);
            for (int i = 0; i < mViewWith * mViewHeight; i++) {
                if (pixels[i] == 0) {
                    wipeArea++;
                }
            }

            float per = wipeArea * 100 / totalArea;
            if (per > 60) {
                isComplete = true;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mOnGuaGuaKaListener != null)
                            mOnGuaGuaKaListener.complete();
                    }
                });
                //子线程中调用重绘使用postInvalidate
                postInvalidate();
            }
            Log.i(TAG, "per:" + per);
        }
    };


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

    public void setOnGuaGuaKaListener(OnGuaGuaKaListener mOnGuaGuaKaListener) {
        this.mOnGuaGuaKaListener = mOnGuaGuaKaListener;
    }

    public static interface OnGuaGuaKaListener {
        void complete();
    }

}
