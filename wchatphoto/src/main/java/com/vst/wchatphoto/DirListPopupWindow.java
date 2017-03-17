package com.vst.wchatphoto;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.vst.wchatphoto.bean.ImageFloderBean;

import java.util.List;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2017/3/14
 * class description:请输入类描述
 */
public class DirListPopupWindow extends BasePopupWindowForListView<ImageFloderBean> {
    private ListView mListView;
    private CommonAdapter<ImageFloderBean> mAdapter;
    private OnDirListItemClickListener mOnDirListItemClickListener;
    private String mCurrentDir;

    public DirListPopupWindow(View contentView, int width, int height, List<ImageFloderBean> data, String mCurrentDir) {
        super(contentView, width, height, true, data);
        this.mCurrentDir = mCurrentDir;
    }

    @Override
    public void initViews() {
        mListView = (ListView) findViewById(R.id.lv_pop);
        mAdapter = new DirListAdapter(context, mDatas, mCurrentDir);
        mListView.setAdapter(mAdapter);
        //设置PopupWindow加载动画
        setAnimationStyle(R.style.anim_dir_pop);
    }

    @Override
    public void initEvents() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mOnDirListItemClickListener.onItemClickListener(position, mDatas.get(position));
            }
        });
    }

    @Override
    public void init() {

    }

    @Override
    protected void beforeInitWeNeedSomeParams(Object... params) {

    }

    public void setCurrentDir(String currentDir) {
        mAdapter = new DirListAdapter(context, mDatas, currentDir);
        mListView.setAdapter(mAdapter);
    }

    public void setItemClickListener(OnDirListItemClickListener itemClickListener) {
        this.mOnDirListItemClickListener = itemClickListener;
    }

    public static interface OnDirListItemClickListener {
        void onItemClickListener(int position, ImageFloderBean floderBean);
    }

    private class DirListAdapter extends CommonAdapter<ImageFloderBean> {
        private String mDirName;

        public DirListAdapter(Context context, List<ImageFloderBean> mDatas, String dirName) {
            super(context, mDatas, R.layout.item_dir_popupwindow);
            this.mDirName = dirName;
        }

        @Override
        public void convert(ViewHolder helper, ImageFloderBean item) {
            helper.setImageByUrl(R.id.iv_pop, item.getFirstImagePath());
            helper.setText(R.id.tv_img_count_pop, String.valueOf(item.getImgCount()));
            ImageView dirChooseIv = helper.getView(R.id.iv_choose_pop);
            if (!item.getDirPath().equals(mDirName)) {
                dirChooseIv.setVisibility(View.GONE);
            } else {
                dirChooseIv.setVisibility(View.VISIBLE);
            }
        }
    }
}
