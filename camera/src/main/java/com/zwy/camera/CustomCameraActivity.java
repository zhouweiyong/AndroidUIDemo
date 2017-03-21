package com.zwy.camera;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

/**
 * 根据Google Android Doc，自定义一个Camera需要如下几个步骤：
 * 1.检查Camera是否存在，并在AndroidManifest.xml中赋予相关的权限；
 * 2.创建一个继承于SurfaceView并实现SurfaceHolder接口的Camera Preview类；
 * 3.新建一个Camera Preview布局文件；
 * 4.设置一个拍照的监听事件，例如单击按钮事件等；
 * 5.实现拍照，并保存拍照后的图片到设备；
 * 6.释放Camera。
 */
public class CustomCameraActivity extends Activity implements View.OnClickListener {

    protected Button btn1;
    protected SurfaceView svCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_custom_camera);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn1) {

        }
    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(CustomCameraActivity.this);
        svCamera = (SurfaceView) findViewById(R.id.sv_camera);
    }
}
