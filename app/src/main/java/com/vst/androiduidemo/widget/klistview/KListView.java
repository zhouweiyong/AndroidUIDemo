package com.vst.androiduidemo.widget.klistview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListView;
import android.widget.Scroller;

import com.vst.androiduidemo.R;

/**
 * http://blog.csdn.net/zhaokaiqiang1992/article/details/42392731
 */
public class KListView extends ListView {
    private static final String TAG = KListView.class.getSimpleName();
    private KListViewHeader headerView;
    private View headViewContent;
    private int headerViewContentHeight;
    private float lastY;
    private static final float OFFSET_RADIO = 2f;
    private static final int SCROLL_DURATION = 500;

    private boolean isRefreshing = false;
    private Scroller scroller;
    private static final int SCROLLBACK_HEADER = 0;
    private static final int SCROLLBACK_FOOTER = 1;
    private int scrollBack;
    private KListViewFooter footerView;
    private View footerViewContent;
    private int footerContentHeight;
    private int totalCount;

    public KListView(Context context) {
        super(context);
        initView();
    }

    public KListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public KListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        scroller = new Scroller(getContext(), new DecelerateInterpolator());
        headerView = new KListViewHeader(getContext());
        headViewContent = headerView.findViewById(R.id.klv_header_content);
        addHeaderView(headerView);
        headerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                headerViewContentHeight = headViewContent.getHeight();
                if (isRefreshing) {
                    refreshHeader();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        footerView = new KListViewFooter(getContext());
        footerViewContent = footerView.findViewById(R.id.xlv_footer_content);
        footerViewContent.measure(0, 0);
        footerContentHeight = footerViewContent.getMeasuredHeight();
//        footerView.setBottomMargin(-footerContentHeight);
        addFooterView(footerView);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        totalCount = getAdapter().getCount();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY = ev.getRawY() - lastY;
                lastY = ev.getRawY();
                /**
                 * 更新header高度的条件：
                 * 1，位置必须是在第一条信息，即ListView的顶部
                 * 2，下拉状态,即位移为正
                 * 3，如果header的高度大于0，则位移可以小于0
                 */
                if (!isRefreshing && getFirstVisiblePosition() == 0 && (deltaY > 0 || headerView.getVisiableHeight() > 0)) {
                    updataHeader(deltaY / OFFSET_RADIO);
                } else if (!isRefreshing && getLastVisiblePosition() == totalCount - 1 && (deltaY < 0 || footerView.getVisiableHeight() > 0)) {
                    updataFooter(deltaY / 3);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (getFirstVisiblePosition() == 0) {
                    if (headerView.getVisiableHeight() >= headerViewContentHeight) {
                        isRefreshing = true;
                    }
                    refreshHeader();
                } else if (getLastVisiblePosition() == totalCount - 1) {
                    if (footerView.getVisiableHeight() >= footerContentHeight) {
                        isRefreshing = true;
                    }
                    refreshFooter();
                }


                break;
        }
        return super.onTouchEvent(ev);
    }

    private void updataHeader(float delta) {
        headerView.setVisiableHeight((int) (delta + headerView.getVisiableHeight()));
        if (headerView.getVisiableHeight() > headerViewContentHeight) {
            headerView.setState(KListViewHeader.STATE_RELEASE_REFRESH);
        } else {
            headerView.setState(KListViewHeader.STATE_PULL_REFRESH);
        }
    }

    private void refreshHeader() {
        if (isRefreshing) {
            headerView.setState(KListViewHeader.STATE_REFRESHING);
            if (onKListViewListener != null) onKListViewListener.onPullDownRefresh();
        }

        scrollBack = SCROLLBACK_HEADER;
        int height = headerView.getVisiableHeight();
        if ((height == headerViewContentHeight && isRefreshing) || (height == 0 && !isRefreshing))
            return;
        int finalHeight = 0;
        if (isRefreshing) finalHeight = headerViewContentHeight;
        scroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        invalidate();
    }

    private void updataFooter(float delta) {
        footerView.setVisiableHeight((int) (footerView.getVisiableHeight() + (-delta)));
        if (footerView.getVisiableHeight() > footerContentHeight) {
            footerView.setState(KListViewFooter.STATE_RELEASE_REFRESH);
        } else {
            footerView.setState(KListViewFooter.STATE_PULL_REFRESH);
        }
    }

    private void refreshFooter() {
        if (isRefreshing) {
            footerView.setState(KListViewFooter.STATE_REFRESHING);
            if (onKListViewListener != null) onKListViewListener.onPullUpRefresh();
        }

        scrollBack = SCROLLBACK_FOOTER;
        int height = footerView.getVisiableHeight();
        if ((isRefreshing && height == footerContentHeight) || (!isRefreshing && height == 0))
            return;
        int finalHeight = 0;
        if (isRefreshing && height > footerContentHeight) finalHeight = footerContentHeight;
        scroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            if (scrollBack == SCROLLBACK_HEADER) {
                headerView.setVisiableHeight(scroller.getCurrY());
            } else if (scrollBack == SCROLLBACK_FOOTER) {
                footerView.setVisiableHeight(scroller.getCurrY());
            }
            Log.i(TAG, "scroller:" + scroller.getCurrY());
        }
        super.computeScroll();
    }


    public void refreshComplete() {
        if (isRefreshing) {
            isRefreshing = false;
            if (scrollBack == SCROLLBACK_HEADER) {
                refreshHeader();
            } else {
                refreshFooter();
            }
        }
    }

    public static interface OnKListViewListener {
        void onPullDownRefresh();

        void onPullUpRefresh();
    }

    private OnKListViewListener onKListViewListener;

    public void setOnKListViewListener(OnKListViewListener onKListViewListener) {
        this.onKListViewListener = onKListViewListener;
    }

    public void autoRefresh() {
        isRefreshing = true;
    }
}
