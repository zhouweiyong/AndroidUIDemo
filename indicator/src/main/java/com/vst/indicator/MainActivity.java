package com.vst.indicator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPagerIndicator mViewPagerIndicator;
    private ViewPager mViewPager;
    private List<String> mTabs = Arrays.asList("标题1", "标题2", "标题3", "标题4", "标题5", "标题6", "标题7", "标题8");
    private ArrayList<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDatas();
        initView();
    }

    private void initDatas() {
        mFragments = new ArrayList<>();
        mFragments.add(ContentFragment.newInstance("这是第一页"));
        mFragments.add(ContentFragment.newInstance("这是第二页"));
        mFragments.add(ContentFragment.newInstance("这是第三页"));
        mFragments.add(ContentFragment.newInstance("这是第四页"));
        mFragments.add(ContentFragment.newInstance("这是第五页"));
        mFragments.add(ContentFragment.newInstance("这是第六页"));
        mFragments.add(ContentFragment.newInstance("这是第七页"));
        mFragments.add(ContentFragment.newInstance("这是第八页"));
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragments));


        mViewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        mViewPagerIndicator.setTab(mTabs);
        mViewPagerIndicator.setViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mList;

        public ViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.mList = list;
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
