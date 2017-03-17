package com.vst.androiduidemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vst.androiduidemo.widget.ScrollView;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2017/3/1
 * class description:请输入类描述
 */
public class ScrollActivity extends Activity implements View.OnClickListener {

    private ScrollView scrollView;
    private Button btn1;
    private Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        initView();
    }

    private void initView() {
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                scrollView.scrollBy(0, 10);
                break;
            case R.id.btn2:
                scrollView.scrollTo(0, 10);
                break;
        }
    }
}
