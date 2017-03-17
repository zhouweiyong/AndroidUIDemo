package com.vst.wchatmainui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2017/3/14
 * class description:请输入类描述
 */
public class ContentFragment extends Fragment {
    private static final String CONTENT_KEY = "content";

    public static ContentFragment newInstance(String content) {
        ContentFragment fragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CONTENT_KEY, content);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String content = bundle.getString(CONTENT_KEY);
        View contentView = inflater.inflate(R.layout.fragment_content, null);
        TextView tv = (TextView) contentView.findViewById(R.id.tv_content);
        tv.setText(content);
        return contentView;
    }
}
