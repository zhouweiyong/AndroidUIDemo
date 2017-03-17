package com.vst.indicator;

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
 * created on 2017/3/15
 * class description:请输入类描述
 */
public class ContentFragment extends Fragment {
    private static final String KEY = "content";

    public static ContentFragment newInstance(String content) {
        ContentFragment fragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY, content);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_content, null);
        TextView tv = (TextView) contentView.findViewById(R.id.tv_content);
        String content = (String) getArguments().get(KEY);
        tv.setText(content);
        return contentView;
    }
}
