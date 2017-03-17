package com.vst.androiduidemo.widget.klistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vst.androiduidemo.R;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2017/3/1
 * class description:请输入类描述
 */
public class KListViewFooter extends LinearLayout {


    private View footerView;
    private ProgressBar pbar_footer;
    private TextView xlv_footer_hint;
    private RelativeLayout xlv_footer_content;
    private int footerContentHeight;

    public static final int STATE_PULL_REFRESH = 0;
    public static final int STATE_RELEASE_REFRESH = 1;
    public static final int STATE_REFRESHING = 2;

    public int currentState;

    public KListViewFooter(Context context) {
        super(context);
        initView();
    }

    public KListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public KListViewFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.klistview_footer, null);
        footerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        addView(footerView);

        xlv_footer_content = (RelativeLayout) footerView.findViewById(R.id.xlv_footer_content);
        pbar_footer = (ProgressBar) footerView.findViewById(R.id.pbar_footer);
        xlv_footer_hint = (TextView) footerView.findViewById(R.id.xlv_footer_hint);


    }

    public void setState(int state) {
        if (state == currentState) return;
        if (state != STATE_REFRESHING) {
            pbar_footer.setVisibility(INVISIBLE);
        } else {
            pbar_footer.setVisibility(VISIBLE);
        }

        switch (state) {
            case STATE_PULL_REFRESH:
                xlv_footer_hint.setText("上拉刷新");
                break;
            case STATE_RELEASE_REFRESH:
                xlv_footer_hint.setText("松开刷新");
                break;
            case STATE_REFRESHING:
                xlv_footer_hint.setText("正在刷新");
                break;
        }
        currentState = state;
    }

    public void setVisiableHeight(int height) {
        if (height > 0) {
            ViewGroup.LayoutParams layoutParams = footerView.getLayoutParams();
            layoutParams.height = height;
            footerView.setLayoutParams(layoutParams);
        }
    }

    public int getVisiableHeight() {
        return footerView.getHeight();
    }

    public void setBottomMargin(int bottomMargin) {
        MarginLayoutParams layoutParams = (MarginLayoutParams) footerView.getLayoutParams();
        layoutParams.bottomMargin = bottomMargin;
        footerView.setLayoutParams(layoutParams);
    }
}
