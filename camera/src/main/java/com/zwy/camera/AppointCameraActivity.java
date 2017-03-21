package com.zwy.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by zhouweiyong on 2017/3/20.
 */
public class AppointCameraActivity extends Activity implements View.OnClickListener {
    private static final String TAG = AppointCameraActivity.class.getSimpleName();
    protected Button btn1;
    protected ImageView ivShow;
    private static final int CAMERA_OK = 0X110;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_pop_camera);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn1) {
            path = getExternalCacheDir().getAbsolutePath() + "/image02.jpg";
            Uri uri = Uri.fromFile(new File(path));
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, CAMERA_OK);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_OK) {
                try {
                    FileInputStream fis = new FileInputStream(path);
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    ivShow.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(AppointCameraActivity.this);
        ivShow = (ImageView) findViewById(R.id.iv_show);
    }
}
