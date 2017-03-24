package com.vst.imagedemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Created by user on 2017/3/23.
 */

public class ImageUtils {

    public static Bitmap handlerImageColor(Bitmap bitmap, float hueR, float hueG, float hueB, float saturation, float lumR, float lumG, float lumB, float lumA) {
        Bitmap tmpBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tmpBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ColorMatrix imageMatrix = new ColorMatrix();
        /**
         * 调整色调
         */
        ColorMatrix hueRMatrix = new ColorMatrix();
        hueRMatrix.setRotate(0, hueR);
        imageMatrix.postConcat(hueRMatrix);
        ColorMatrix hueGMatrix = new ColorMatrix();
        hueGMatrix.setRotate(1, hueG);
        imageMatrix.postConcat(hueGMatrix);
        ColorMatrix hueBMatrix = new ColorMatrix();
        hueBMatrix.setRotate(2, hueB);
        imageMatrix.postConcat(hueBMatrix);

        /**
         * 调整饱和度
         */
        ColorMatrix saturationMatrix = new ColorMatrix();
        saturationMatrix.setSaturation(saturation);
        imageMatrix.postConcat(saturationMatrix);

        /**
         * 调整亮度
         */
        ColorMatrix lumMatrix = new ColorMatrix();
        lumMatrix.setScale(lumR, lumG, lumB, lumA);
        imageMatrix.postConcat(lumMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return tmpBitmap;
    }
}
