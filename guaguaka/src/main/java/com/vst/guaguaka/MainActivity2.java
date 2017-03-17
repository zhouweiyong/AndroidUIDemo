package com.vst.guaguaka;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2017/3/16
 * class description:请输入类描述
 */
public class MainActivity2 extends Activity {
    private GuaGuaKaView2 mGuaGuaKa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mGuaGuaKa = (GuaGuaKaView2) findViewById(R.id.guagua);
        mGuaGuaKa.setOnGuaGuaKaListener(new GuaGuaKaView2.OnGuaGuaKaListener() {
            @Override
            public void complete() {
                Toast.makeText(MainActivity2.this, "刮奖完成。。。", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
