package com.zwy.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by zhouweiyong on 2017/3/20.
 */
public class PopCameraActivity extends AppCompatActivity implements View.OnClickListener {
    protected Button btn1;
    protected ImageView ivShow;

    private static final int CAMERA_OK = 0X110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pop_camera);
        initView();
    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(PopCameraActivity.this);
        ivShow = (ImageView) findViewById(R.id.iv_show);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn1) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_OK);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode==CAMERA_OK){
                /**
                 * 返回的是一张缩略图
                 */
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                ivShow.setImageBitmap(bitmap);
            }

        }
    }
}
