package com.vst.wchatmainui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView mChatTv, mDiscoverTv, mContactTv;
    private View mIndicator;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragmentList;
    private int mIndicatorWidth;
    private ArrayList<TextView> mTabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvnet();
    }

    private void initView() {
        mChatTv = (TextView) findViewById(R.id.tv_chat);
        mDiscoverTv = (TextView) findViewById(R.id.tv_discover);
        mContactTv = (TextView) findViewById(R.id.tv_contact);
        mTabs = new ArrayList<>();
        mTabs.add(mChatTv);
        mTabs.add(mDiscoverTv);
        mTabs.add(mContactTv);

        mViewPager = (ViewPager) findViewById(R.id.vp_main);
        mIndicator = findViewById(R.id.view_indicator);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mIndicatorWidth = mIndicator.getLayoutParams().width = dm.widthPixels / 3;

        BadgeView badgeView = new BadgeView(this);
        badgeView.setTargetView(mChatTv);
        badgeView.setBadgeCount(30);
        badgeView.setBadgeMargin(0, 7, 30, 0);
        badgeView.setBackground(9, Color.parseColor("#033056"));

        mFragmentList = new ArrayList<>();
        mFragmentList.add(ContentFragment.newInstance("这是聊天界面"));
        mFragmentList.add(ContentFragment.newInstance("这是发现界面"));
        mFragmentList.add(ContentFragment.newInstance("这是通讯录界面"));

        MainViewPagerAdapter pagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(0);
        setTabColor(0);

    }

    private void initEvnet() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("zwy", "position:" + position + "   positionOffset:" + positionOffset + "  positionOffsetPixels:" + positionOffsetPixels);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mIndicator.getLayoutParams();
                params.leftMargin = (int) (positionOffset * mIndicatorWidth + position * mIndicatorWidth);
                mIndicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                setTabColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (TextView mTab : mTabs) {
            mTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.tv_chat:
                            mViewPager.setCurrentItem(0);
                            break;
                        case R.id.tv_discover:
                            mViewPager.setCurrentItem(1);
                            break;
                        case R.id.tv_contact:
                            mViewPager.setCurrentItem(2);
                            break;
                    }
                }
            });
        }
    }

    private void setTabColor(int position) {
        int tabCount = mTabs.size();
        for (int i = 0; i < tabCount; i++) {
            TextView tv = mTabs.get(i);
            if (i != position) {
                tv.setTextColor(Color.parseColor("#333333"));
            } else {
                tv.setTextColor(Color.parseColor("#993333"));
            }
        }
    }

    private class MainViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mList;

        public MainViewPagerAdapter(FragmentManager fm, List<Fragment> mList) {
            super(fm);
            this.mList = mList;
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }
}
