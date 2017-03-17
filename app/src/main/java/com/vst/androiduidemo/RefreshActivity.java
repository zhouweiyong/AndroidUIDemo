package com.vst.androiduidemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.vst.androiduidemo.widget.XListView;

import java.util.ArrayList;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2017/2/27
 * class description:请输入类描述
 */
public class RefreshActivity extends Activity {

    private XListView xlv;
    private ArrayList<String> data;
    private int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        initView();
    }

    private void initView() {
        data = new ArrayList<>();
        xlv = (XListView) findViewById(R.id.xlv);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, getData());
        xlv.setAdapter(arrayAdapter);
    }

    private ArrayList<String> getData() {
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            temp.add("content" + index++);
        }
        data.addAll(temp);
        return data;
    }
}
