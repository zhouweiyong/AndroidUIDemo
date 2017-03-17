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
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2017/3/16
 * class description:请输入类描述
 */
public class GuaGuaKaView extends View {
    private static final String TAG = GuaGuaKaView.class.getSimpleName();
    /**
     * 屏幕宽高
     */
    private int mScreenWidth;
    private int mScreenHeight;
    //刮刮卡的遮盖层和涂抹路径绘制的Bitmap和canvas
    private Bitmap mGuaGuaKaBitmap;
    private Canvas mGuaGuaKaCanvas;
    //画遮盖层的画笔
    private Paint mDstPaint;
    //画涂抹路径的画笔
    private Paint mSrcPaint;
    //画背景图片Bitmap和刮刮卡遮盖层Bitmap的画笔
    private Paint mBitmapPaint;
    //涂抹路径
    private Path mLinePath;
    //背景Bitmap
    private Bitmap mBackBitmap;
    /**
     * 记录涂抹路径坐标
     */
    private float mStartX, mStartY;
    private float mEndX, mEndY;

    public GuaGuaKaView(Context context) {
        this(context, null);
    }

    public GuaGuaKaView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuaGuaKaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //获取屏幕宽高
        DisplayMetrics dp = getResources().getDisplayMetrics();
        mScreenWidth = dp.widthPixels;
        mScreenHeight = dp.heightPixels;
        /**
         * 初始化画笔
         */
        mDstPaint = createPaint(Color.parseColor("#cccccc"), 5, Paint.Style.FILL, 5);
        mSrcPaint = createPaint(Color.BLUE, 5, Paint.Style.STROKE, 20);
        mBitmapPaint = new Paint();
        mLinePath = new Path();
        //初始化遮盖层画板
        mGuaGuaKaBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        mGuaGuaKaCanvas = new Canvas(mGuaGuaKaBitmap);
        //获取背景图片
        mBackBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img04);
        //绘制遮盖层
        mGuaGuaKaCanvas.drawRect(new Rect(0, 0, mScreenWidth, mScreenHeight), mDstPaint);
        //设置遮盖模式
        PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        //一定要使用涂抹路径的paint进行设置，而不是遮盖层的paint
        mSrcPaint.setXfermode(porterDuffXfermode);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        canvas.drawBitmap(mBackBitmap, null, new Rect(0, 0, mScreenWidth, mScreenHeight), mBitmapPaint);
        //在遮盖板的bitmap上绘制涂抹路径
        mGuaGuaKaCanvas.drawPath(mLinePath, mSrcPaint);
        //把遮盖板bitmap绘制到View的画板上
        canvas.drawBitmap(mGuaGuaKaBitmap, null, new Rect(0, 0, mScreenWidth, mScreenHeight), mBitmapPaint);

    }


    /***
     * 监听滑动事件，获取涂抹坐标
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mStartY = event.getY();
                mLinePath.moveTo(mStartX, mStartY);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "ACTION_MOVE");
                mEndX = event.getX();
                mEndY = event.getY();
                mLinePath.lineTo(mEndX, mEndY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "ACTION_UP");
                break;
        }
        return true;
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
