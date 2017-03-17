package com.vst.androiduidemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vst.androiduidemo.R;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2017/2/27
 * class description:请输入类描述
 */
public class XListView extends ListView {
    private static final String TAG = XListView.class.getSimpleName();
    private static final int STATE_PULL_REFRESH = 0;
    private static final int STATE_RELEASE_REFRESH = 1;
    private static final int STATE_REFRESHING = 2;
    private int currentState = STATE_PULL_REFRESH;

    private int headerViewMeasuredHeight;
    private View headerView;
    private int startY;
    private RotateAnimation rotateUp;
    private RotateAnimation rotateDown;


    public XListView(Context context) {
        super(context);
        init();
    }

    public XListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private ImageView iv_arrow;
    private ProgressBar pbar;
    private TextView tv_title;
    private TextView tv_time;

    /**
     * 获取头部和尾部布局
     * 初始化隐藏
     */
    private void init() {
        headerView = View.inflate(getContext(), R.layout.refresh_head_view, null);
        iv_arrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
        pbar = (ProgressBar) headerView.findViewById(R.id.pbar);
        tv_title = (TextView) headerView.findViewById(R.id.tv_title);
        tv_time = (TextView) headerView.findViewById(R.id.tv_time);
        this.addHeaderView(headerView);
        headerView.measure(0, 0);
        headerViewMeasuredHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -headerViewMeasuredHeight, 0, 0);

        rotateUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateUp.setDuration(500);
        rotateUp.setFillAfter(true);

        rotateDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateUp.setDuration(500);
        rotateUp.setFillAfter(true);
    }

    private boolean isFirstPostion = false;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getRawY();
                Log.i(TAG, "startX:" + startY);
                if (getFirstVisiblePosition() > 0) {
                    isFirstPostion = false;
                } else {
                    isFirstPostion = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (getFirstVisiblePosition() > 0) {
                    isFirstPostion = false;
                }
                if (!isFirstPostion && getFirstVisiblePosition() == 0) {
                    startY = (int) ev.getRawY();
                    isFirstPostion = true;
                }
                int endY = (int) ev.getRawY();
                int d = endY - startY;
                if (d > 0 && isFirstPostion && currentState != STATE_REFRESHING) {
                    int topPadding = d - headerViewMeasuredHeight;
                    headerView.setPadding(0, topPadding, 0, 0);
                    if (topPadding <= 0 && currentState == STATE_RELEASE_REFRESH) {//下拉
                        currentState = STATE_PULL_REFRESH;
                        changeState();
                    } else if (topPadding > 0 && currentState == STATE_PULL_REFRESH) {//松开刷新
                        currentState = STATE_RELEASE_REFRESH;
                        changeState();
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                if (currentState == STATE_RELEASE_REFRESH) {
                    currentState = STATE_REFRESHING;
                    changeState();
                }

                break;
        }
        return super.onTouchEvent(ev);
    }

    private void changeState() {
        switch (currentState) {
            case STATE_PULL_REFRESH:
                tv_title.setText("下拉刷新");
                iv_arrow.startAnimation(rotateDown);
                break;
            case STATE_RELEASE_REFRESH:
                tv_title.setText("松开更新");
                iv_arrow.startAnimation(rotateUp);
                break;
            case STATE_REFRESHING:
                headerView.setPadding(0, 0, 0, 0);
                tv_title.setText("正在更新...");
                iv_arrow.clearAnimation();
                iv_arrow.setVisibility(INVISIBLE);
                pbar.setVisibility(VISIBLE);
                break;
        }
    }


}
