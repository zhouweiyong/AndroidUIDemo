package com.vst.androiduidemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vst.androiduidemo.widget.kslidingmenu.KSlideMenu;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2017/3/2
 * class description:请输入类描述
 */
public class KSlideMenuActivity extends Activity implements View.OnClickListener {

    private Button btn1;
    private KSlideMenu k_slide_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kslidemenu);
        initView();

    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.btn1);

        btn1.setOnClickListener(this);
        k_slide_menu = (KSlideMenu) findViewById(R.id.k_slide_menu);
        k_slide_menu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                k_slide_menu.toggle();
                break;
        }
    }
}
