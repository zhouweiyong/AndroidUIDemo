package com.vst.addmoreimage;

import android.content.Context;
import android.net.Uri;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PublicShowOrderGridAdapter extends BaseAda<String> {
	
	private SparseArray<View> map = new SparseArray<View>();
	

	public PublicShowOrderGridAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(map.get(position) == null) {
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
		if("".equals(str)) {
			holder.img.setImageResource(R.drawable.icon_add_pic);
		} else {
			holder.img.setImageURI(Uri.parse(group.get(position)));
		}
		return convertView;
	}
	
	
	private static class ViewHolder {
		ImageView img;
	}

}
