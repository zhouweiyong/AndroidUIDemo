package com.vst.androiduidemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

import com.vst.androiduidemo.widget.klistview.KListView;

import java.util.ArrayList;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2017/2/28
 * class description:请输入类描述
 */
public class KRefreshActivity extends Activity implements KListView.OnKListViewListener {

    private KListView klistview;
    private ArrayList<String> data;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_krefresh);
        initView();
    }

    private void initView() {
        data = new ArrayList<>();
        klistview = (KListView) findViewById(R.id.klistview);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, getData());
        klistview.setAdapter(arrayAdapter);
        klistview.setOnKListViewListener(this);
//        klistview.autoRefresh();
    }

    private ArrayList<String> getData() {
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            temp.add("content" + index++);
        }
        data.addAll(temp);
        return data;
    }
    private Handler handler = new Handler();
    @Override
    public void onPullDownRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              klistview.refreshComplete();
            }
        },3000);
    }

    @Override
    public void onPullUpRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                klistview.refreshComplete();
            }
        },3000);
    }
}
