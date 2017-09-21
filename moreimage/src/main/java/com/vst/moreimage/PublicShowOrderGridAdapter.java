package com.vst.moreimage;

import android.content.Context;
import android.net.Uri;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class PublicShowOrderGridAdapter extends BaseAda<String> {

	private SparseArray<View> map = new SparseArray<View>();
	private DisplayImageOptions options;

	public PublicShowOrderGridAdapter(Context context) {
		super(context);
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_launcher) // resource
																								// or
																								// drawable
				.showImageForEmptyUri(R.drawable.ic_launcher) // resource or
																// drawable
				.showImageOnFail(R.drawable.ic_launcher) // resource or drawable
				.cacheInMemory(true)// 开启内存缓存
				.cacheOnDisk(true) // 开启硬盘缓存
				.resetViewBeforeLoading(false).build();

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (map.get(position) == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_onepmall_public_show_order, null);
			holder.img = (ImageView) convertView.findViewById(R.id.upload_img);
			convertView.setTag(holder);
			final int p = position;
			map.put(p, convertView);
		} else {
			convertView = map.get(position);
			holder = (ViewHolder) convertView.getTag();
		}
		String str = group.get(position);
		if ("".equals(str)) {
			holder.img.setImageResource(R.drawable.icon_add_pic);
		} else if (str.startsWith("http")) {
			loadImage(holder.img, str);
		} else {
			holder.img.setImageURI(Uri.parse(group.get(position)));
		}
		return convertView;
	}

	private void loadImage(ImageView iv, String url) {
		// 使用ImageLoader加载图片
		ImageLoader imageLoader = ImageLoader.getInstance();
		// ImageLoader初始化
		imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
		ImageLoader.getInstance().displayImage(url, iv, options);
	}

	private static class ViewHolder {
		ImageView img;
	}

}
