package com.vst.imagedemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by user on 2017/3/23.
 */

public class ImagePixelMartixActivity extends Activity {
    private ArrayList<ImageView> mViews;
    private GridLayout glayout;
    private Bitmap mBitmap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pixel_martix);
        initView();

    }

    private void initView() {
        mViews = new ArrayList<>();
        glayout = (GridLayout) findViewById(R.id.glayout);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img04);

        glayout.post(new Runnable() {
            @Override
            public void run() {
                int count = glayout.getChildCount();
                for (int i = 0; i < count; i++) {
                    ImageView imageView = (ImageView) glayout.getChildAt(i);
                    GridLayout.LayoutParams lp = (GridLayout.LayoutParams) imageView.getLayoutParams();
                    lp.width = glayout.getWidth() / 2;
                    lp.height = glayout.getHeight() / 2;
                    imageView.setLayoutParams(lp);
                    mViews.add(imageView);
                }
                mViews.get(0).setImageBitmap(mBitmap);
                setNegativeEffect();
                setOldPhotoEffect();
                setEmbossEffect();
            }
        });


    }

    //底片效果
    private void setNegativeEffect() {
        ImageView iv = mViews.get(1);
        Bitmap bitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawBitmap(mBitmap, 0, 0, paint);
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for (int i = 0; i < pixels.length; i++) {
            int pixelPoint = pixels[i];
            int r = Color.red(pixelPoint);
            int g = Color.green(pixelPoint);
            int b = Color.blue(pixelPoint);
            int a = Color.alpha(pixelPoint);
            r = 255 - r;
            g = 255 - g;
            b = 255 - b;
            pixels[i] = Color.argb(a, r, g, b);
        }
        bitmap.setPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        iv.setImageBitmap(bitmap);
    }

    //老照片效果
    private void setOldPhotoEffect() {
        ImageView iv = mViews.get(2);
        Bitmap bitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawBitmap(mBitmap, 0, 0, paint);
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for (int i = 0; i < pixels.length; i++) {
            int pixelPoint = pixels[i];
            int r = Color.red(pixelPoint);
            int g = Color.green(pixelPoint);
            int b = Color.blue(pixelPoint);
            int a = Color.alpha(pixelPoint);
            int nr = (int) (0.393 * r + 0.769 * g + 0.189 * b);
            int ng = (int) (0.349 * r + 0.686 * g + 0.168 * b);
            int nb = (int) (0.272 * r + 0.534 * g + 0.131 * b);
            pixels[i] = Color.argb(a, nr, ng, nb);
        }
        bitmap.setPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        iv.setImageBitmap(bitmap);
    }

    //浮雕效果
    private void setEmbossEffect() {
        ImageView iv = mViews.get(3);
        Bitmap bitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawBitmap(mBitmap, 0, 0, paint);
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for (int i = 0; i < pixels.length - 1; i++) {
            int pixelPoint = pixels[i];
            int nexPixelPoint = pixels[i + 1];
            int a = Color.alpha(pixelPoint);
            int nr = Color.red(nexPixelPoint) - Color.red(pixelPoint) + 127;
            int ng = Color.green(nexPixelPoint) - Color.green(pixelPoint) + 127;
            int nb = Color.blue(nexPixelPoint) - Color.blue(pixelPoint) + 127;
            pixels[i] = Color.argb(a, nr, ng, nb);
        }
        bitmap.setPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        iv.setImageBitmap(bitmap);
    }
}
