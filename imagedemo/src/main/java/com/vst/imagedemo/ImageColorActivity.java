package com.vst.imagedemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

/**
 * Created by user on 2017/3/23.
 */

public class ImageColorActivity extends Activity implements SeekBar.OnSeekBarChangeListener {
    private SeekBar sb_saturation;
    private static final int COLOR_MAX = 255;
    private ImageView iv_show;
    private Bitmap mBitmap;
    private SeekBar sb_hue_r;
    private SeekBar sb_hue_g;
    private SeekBar sb_hue_b;
    private SeekBar sb_lum_r;
    private SeekBar sb_lum_g;
    private SeekBar sb_lum_b;
    private SeekBar sb_lum_a;
    private float mHueRValue, mHueGValue, mHueBValue, mLumRValue, mLumGValue, mLumBValue, mLumAValue, mSaturationValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_color);
        initView();

    }

    private void initView() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img04);
        iv_show = (ImageView) findViewById(R.id.iv_show);
        sb_saturation = (SeekBar) findViewById(R.id.sb_saturation);
        sb_saturation.setOnSeekBarChangeListener(this);
        sb_hue_r = (SeekBar) findViewById(R.id.sb_hue_r);
        sb_hue_r.setOnSeekBarChangeListener(this);
        sb_hue_g = (SeekBar) findViewById(R.id.sb_hue_g);
        sb_hue_g.setOnSeekBarChangeListener(this);
        sb_hue_b = (SeekBar) findViewById(R.id.sb_hue_b);
        sb_hue_b.setOnSeekBarChangeListener(this);
        sb_lum_r = (SeekBar) findViewById(R.id.sb_lum_r);
        sb_lum_r.setOnSeekBarChangeListener(this);
        sb_lum_g = (SeekBar) findViewById(R.id.sb_lum_g);
        sb_lum_g.setOnSeekBarChangeListener(this);
        sb_lum_b = (SeekBar) findViewById(R.id.sb_lum_b);
        sb_lum_b.setOnSeekBarChangeListener(this);
        sb_lum_a = (SeekBar) findViewById(R.id.sb_lum_a);
        sb_lum_a.setOnSeekBarChangeListener(this);
        sb_hue_r.setMax(COLOR_MAX);
        sb_hue_g.setMax(COLOR_MAX);
        sb_hue_b.setMax(COLOR_MAX);
        sb_lum_r.setMax(COLOR_MAX);
        sb_lum_g.setMax(COLOR_MAX);
        sb_lum_b.setMax(COLOR_MAX);
        sb_lum_a.setMax(COLOR_MAX);
        sb_saturation.setMax(COLOR_MAX);
        sb_saturation.setProgress(COLOR_MAX);
        sb_lum_r.setProgress(COLOR_MAX);
        sb_lum_g.setProgress(COLOR_MAX);
        sb_lum_b.setProgress(COLOR_MAX);
        sb_lum_a.setProgress(COLOR_MAX);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sb_hue_r:
                mHueRValue = progress;
                break;
            case R.id.sb_hue_g:
                mHueGValue = progress;
                break;
            case R.id.sb_hue_b:
                mHueBValue = progress;
                break;
            case R.id.sb_saturation:
                mSaturationValue = progress * 1.0f / COLOR_MAX;
                break;
            case R.id.sb_lum_r:
                mLumRValue = progress * 1.0f / COLOR_MAX;
                break;
            case R.id.sb_lum_g:
                mLumGValue = progress * 1.0f / COLOR_MAX;
                break;
            case R.id.sb_lum_b:
                mLumBValue = progress * 1.0f / COLOR_MAX ;
                break;
            case R.id.sb_lum_a:
                mLumAValue = progress * 1.0f / COLOR_MAX;
                break;
        }
        Log.i("zwy", "progress:" + progress);
        Log.i("zwy", "mSaturationValue:" + mSaturationValue);
        iv_show.setImageBitmap(ImageUtils.handlerImageColor(mBitmap, mHueRValue, mHueGValue, mHueBValue, mSaturationValue, mLumRValue, mLumGValue, mLumBValue, mLumAValue));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
