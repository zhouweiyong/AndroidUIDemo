package com.vst.wchatphoto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.vst.wchatphoto.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2017/3/14
 * class description:请输入类描述
 */
public class GridViewAdapter extends BaseAdapter {
    private List<String> mList;
    private Context mContext;
    private String dirPath;
    //应用退到后台，静态变量有可能被清空，导致空指针BUG
    public static ArrayList<String> selectImages = new ArrayList<>();

    public GridViewAdapter(Context mContext, List<String> mList, String dirPath) {
        this.mList = mList;
        this.mContext = mContext;
        this.dirPath = dirPath;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gridview, null, false);
            vh = new ViewHolder();
            vh.ivImage = (ImageView) convertView.findViewById(R.id.iv_gv_item);
            vh.ibtn = (ImageButton) convertView.findViewById(R.id.ibtn_gv_item);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        String imageName = (String) getItem(position);
        vh.ivImage.setImageResource(R.mipmap.pictures_no);
        final String imagePath = dirPath + "/" + imageName;
        ImageLoader.getInstance().loadImage(imagePath, vh.ivImage);
        vh.ibtn.setTag(imagePath);
        if (selectImages.contains(imagePath)) {
            vh.ibtn.setImageResource(R.mipmap.pictures_selected);
        } else {
            vh.ibtn.setImageResource(R.mipmap.picture_unselected);
        }
        vh.ibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imagePath = (String) v.getTag();
                if (selectImages.contains(imagePath)){
                    selectImages.remove(imagePath);
                    ((ImageButton)v).setImageResource(R.mipmap.picture_unselected);
                }else {
                    selectImages.add(imagePath);
                    ((ImageButton)v).setImageResource(R.mipmap.pictures_selected);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView ivImage;
        ImageButton ibtn;
    }
}
