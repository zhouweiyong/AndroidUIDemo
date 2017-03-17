package com.vst.androiduidemo.widget.klistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vst.androiduidemo.R;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2017/2/28
 * class description:请输入类描述
 */
public class KListViewHeader extends LinearLayout {
    private ImageView klv_header_arrow;
    private ProgressBar xlv_header_pb;
    private TextView klv_header_hint;

    public static final int STATE_PULL_REFRESH = 0;
    public static final int STATE_RELEASE_REFRESH = 1;
    public static final int STATE_REFRESHING = 2;
    private static final String PULL_TEXT = "下拉刷新";
    private static final String RELEASE_TEXT = "松开刷新";
    private static final String REFRESHING_TEXT = "正在刷新";

    private int currentState = STATE_PULL_REFRESH;
    private View headerView;
    private static final int DURATION = 200;
    private RotateAnimation rotateDown;
    private RotateAnimation rotateUp;

    public KListViewHeader(Context context) {
        super(context);
        initView();
    }

    public KListViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public KListViewHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        headerView = LayoutInflater.from(getContext()).inflate(R.layout.klistview_header, null);
        klv_header_arrow = (ImageView) headerView.findViewById(R.id.klv_header_arrow);
        xlv_header_pb = (ProgressBar) headerView.findViewById(R.id.xlv_header_pb);
        klv_header_hint = (TextView) headerView.findViewById(R.id.klv_header_hint);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        addView(headerView, layoutParams);

        rotateUp = new RotateAnimation(0f, -180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateUp.setDuration(DURATION);
        rotateUp.setFillAfter(true);

        rotateDown = new RotateAnimation(-180f, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateDown.setDuration(DURATION);
        rotateDown.setFillAfter(true);
    }

    public void setState(int state) {
        if (state == currentState) return;

        if (state == STATE_REFRESHING) {
            klv_header_arrow.clearAnimation();
            klv_header_arrow.setVisibility(INVISIBLE);
            xlv_header_pb.setVisibility(VISIBLE);
        } else {
            klv_header_arrow.setVisibility(VISIBLE);
            xlv_header_pb.setVisibility(INVISIBLE);
        }
        switch (state) {
            case STATE_PULL_REFRESH:
                if (currentState == STATE_RELEASE_REFRESH) {
                    klv_header_arrow.startAnimation(rotateDown);
                }
                if (currentState == STATE_REFRESHING) {
                    klv_header_arrow.clearAnimation();
                }
                klv_header_hint.setText(PULL_TEXT);
                break;
            case STATE_RELEASE_REFRESH:
                if (currentState == STATE_PULL_REFRESH) {
                    klv_header_arrow.startAnimation(rotateUp);
                    klv_header_hint.setText(RELEASE_TEXT);
                }
                break;
            case STATE_REFRESHING:
                klv_header_hint.setText(REFRESHING_TEXT);
                break;
        }
        currentState = state;
    }

    public void setVisiableHeight(int height) {
        if (height < 0) height = 0;
        ViewGroup.LayoutParams layoutParams = headerView.getLayoutParams();
        layoutParams.height = height;
        headerView.setLayoutParams(layoutParams);
    }

    public int getVisiableHeight() {
        return headerView.getHeight();
    }

    public void show() {
        headerView.setVisibility(VISIBLE);
    }

    public void hide() {
        headerView.setVisibility(INVISIBLE);
    }
}
