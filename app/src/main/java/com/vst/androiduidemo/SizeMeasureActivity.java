package com.vst.androiduidemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2017/3/13
 * class description:请输入类描述
 */
public class SizeMeasureActivity extends Activity {
    private TextView tv_measure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_size_measure);
        initView();
    }

    private void initView() {
        tv_measure = (TextView) findViewById(R.id.tv_measure);
        Log.i("zwy", "" + tv_measure.getWidth());
        tv_measure.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.i("zwy", "" + tv_measure.getWidth());
                Log.i("zwy", "" + tv_measure.getHeight());
            }
        });

    }
}
