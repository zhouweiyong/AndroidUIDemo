package com.vst.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2017/3/15
 * class description:请输入类描述
 */
public class TestView extends View {


    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint mIndicatorPaint = createPaint(Color.parseColor("#003399"), 0, Paint.Style.STROKE, 5);
        Path mIndicatorPath = new Path();
        mIndicatorPath.moveTo(0, 200);
        mIndicatorPath.lineTo(100, 200);
        canvas.save();
        canvas.drawPath(mIndicatorPath, mIndicatorPaint);
        canvas.restore();
        super.onDraw(canvas);
    }


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
