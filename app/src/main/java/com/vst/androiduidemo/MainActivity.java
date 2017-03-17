package com.vst.androiduidemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.btn1);

        btn1.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(this);
        btn4 = (Button) findViewById(R.id.btn4);
        btn4.setOnClickListener(this);
        btn5 = (Button) findViewById(R.id.btn5);
        btn5.setOnClickListener(this);
        btn6 = (Button) findViewById(R.id.btn6);
        btn6.setOnClickListener(this);
        btn7 = (Button) findViewById(R.id.btn7);
        btn7.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                startActivity(new Intent(this, RefreshActivity.class));
                break;
            case R.id.btn2:
                startActivity(new Intent(this, KRefreshActivity.class));
                break;
            case R.id.btn3:
                startActivity(new Intent(this, ScrollActivity.class));
                break;
            case R.id.btn4:
                startActivity(new Intent(this, KSlideMenuActivity.class));
                break;
            case R.id.btn5:
                startActivity(new Intent(this, DrawerSlideMenuActivity.class));
                break;
            case R.id.btn6:
                startActivity(new Intent(this, QQSlideMenuActivity.class));
                break;
            case R.id.btn7:
                startActivity(new Intent(this, SizeMeasureActivity.class));
                break;
        }
    }
}
