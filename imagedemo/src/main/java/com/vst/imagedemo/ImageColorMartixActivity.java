package com.vst.imagedemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

/**
 * Created by user on 2017/3/23.
 */

public class ImageColorMartixActivity extends Activity implements View.OnClickListener {

    private ImageView iv_show;
    private GridLayout glayout;
    private float[] mDatas = new float[20];
    private EditText[] mViews = new EditText[20];
    private int mLayoutWidth, mLayoutHeight;
    private Button btn1;
    private Button btn2;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_color_martix);
        initView();
    }

    private void initView() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img07);
        iv_show = (ImageView) findViewById(R.id.iv_show);
        glayout = (GridLayout) findViewById(R.id.glayout);

        glayout.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mViews.length; i++) {
                    EditText et = new EditText(ImageColorMartixActivity.this);
                    mViews[i] = et;
                    GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                    lp.width = glayout.getWidth() / 5;
                    lp.height = glayout.getHeight() / 4;
                    et.setLayoutParams(lp);
                    glayout.addView(et);
                }
                initDatas();
                setEditText();
            }
        });
        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
    }

    private void refreshData() {
        for (int i = 0; i < mViews.length; i++) {
            mDatas[i] = Float.valueOf(mViews[i].getText().toString());
        }
    }

    private void initDatas() {
        for (int i = 0; i < mDatas.length; i++) {
            if (i % 6 == 0) {
                mDatas[i] = 1;
            } else {
                mDatas[i] = 0;
            }
        }
    }

    private void setEditText() {
        for (int i = 0; i < mDatas.length; i++) {
            mViews[i].setText(String.valueOf(mDatas[i]));
        }
    }

    //通过矩阵设置图片
    private void setImageMartix() {
        Bitmap bitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(mDatas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(mBitmap, 0, 0, paint);
        iv_show.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                refreshData();
                setImageMartix();
                break;
            case R.id.btn2:
                initDatas();
                setImageMartix();
                setEditText();
                break;
        }
    }
}
