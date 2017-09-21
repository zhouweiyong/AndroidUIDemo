package com.vst.addmoreimage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ImageGridAdapter extends BaseAdapter {

	private TextCallback textcallback = null;
	final String TAG = getClass().getSimpleName();
	Activity act;
	List<ImageItem> dataList;
//	Map<String, String> map = new HashMap<String, String>();
	List<String> List = new ArrayList<String>();
	BitmapCache cache;
	private Handler mHandler;
	private int selectTotal = Bimp.drr.size();
	private int selectMax=10;//默认最多可以一次性选择3张图片
	BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
			if (null != imageView && null != bitmap) {
				String url = (String) params[0];
				if (url != null && url.equals((String) imageView.getTag())) {
					((ImageView) imageView).setImageBitmap(bitmap);
				}
				else {
					Log.e(TAG, "callback, bmp not match");
				}
			}
			else {
				Log.e(TAG, "callback, bmp null");
			}
		}
	};

	public static interface TextCallback {
		public void onListen(int count);
	}

	public void setTextCallback(TextCallback listener) {
		textcallback = listener;
	}

	public ImageGridAdapter(Activity act, List<ImageItem> list, Handler mHandler) {
		this.act = act;
		dataList = list;
		cache = new BitmapCache();
		this.mHandler = mHandler;
		getListSize();
	}
	public ImageGridAdapter(Activity act, List<ImageItem> list, Handler handle,int selectNum) {
		this.act = act;
		dataList = list;
		cache = new BitmapCache();
		this.selectMax=selectNum;
		this.mHandler = handle;
	}
	
	private ArrayList<String> lists = new ArrayList<String>();
	
	public void getListSize() {
		
		for(ImageItem imageItem : dataList) {
			if(imageItem.isSelected) {
				List.add(imageItem.imagePath);
				lists.add(imageItem.imagePath);
			}
		}
	}
	
	public ArrayList<String> removeList() {
		ArrayList<String> list = new ArrayList<String>();
		if(lists.size() > 0 && List.size() > 0) {
			if(List.size() >= lists.size()) {
				for(int i = 0; i < lists.size(); i++) {
					if(!List.contains(lists.get(i))) {
						list.add(lists.get(i));
					}
				}
			} else {
				System.out.println("test21");
				for(int i = 0; i < List.size(); i++) {
					if(lists.contains(List.get(i))) {
						lists.remove(List.get(i));
					}
				}
				list = lists;
			}
		} else if(lists.size() > 0 && List.size() == 0){
			list = lists;
		}
		return list;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (dataList != null) {
			count = dataList.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class Holder {
		private ImageView iv;
		private ImageView selected;
		private TextView text;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;

		if (null == convertView) {
			holder = new Holder();
			convertView = View.inflate(act, R.layout.item_image_grid, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.image);
			holder.selected = (ImageView) convertView.findViewById(R.id.isselected);
			holder.text = (TextView) convertView.findViewById(R.id.item_image_grid_text);
			convertView.setTag(holder);
		}
		else {
			holder = (Holder) convertView.getTag();
		}
		final ImageItem item = dataList.get(position);

		holder.iv.setTag(item.imagePath);
		cache.displayBmp(holder.iv, item.thumbnailPath, item.imagePath, callback);
		if (item.isSelected) {
			holder.selected.setImageResource(R.drawable.icon_data_select);
			holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
		}
		else {
			holder.selected.setImageResource(R.drawable.transparent);
			holder.text.setBackgroundColor(0x00000000);
		}

		// if (null == Bimp.zoomBitmap(dataList.get(position).imagePath))
		// {
		// holder.iv.setVisibility(View.GONE);
		// holder.iv.setClickable(false);
		// }
		holder.iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String path = dataList.get(position).imagePath;
				System.out.println("path:" + path);
				System.out.println("size:" + List.size());
				if (selectTotal < selectMax) {
					System.out.println("test1");
					item.isSelected = !item.isSelected;
					if (item.isSelected) {
						holder.selected.setImageResource(R.drawable.icon_data_select);
						holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
						selectTotal++;
						if (textcallback != null) {
							textcallback.onListen(selectTotal);
						}
//						List.put(path, path);
						List.add(path);
					}
					else if (!item.isSelected) {
						System.out.println("test2");
						holder.selected.setImageResource(R.drawable.transparent);
						holder.text.setBackgroundColor(0x00000000);
						selectTotal--;
						if(selectTotal<0){
							selectTotal=0;
						}
						if (textcallback != null)
							textcallback.onListen(selectTotal);
						List.remove(path);
					}
				}
				else if (selectTotal >= selectMax) {
					System.out.println("test3");
					if (item.isSelected == true) {
						item.isSelected = !item.isSelected;
						holder.selected.setImageResource(R.drawable.transparent);
						holder.text.setBackgroundColor(0x00000000);
						selectTotal--;
						if(selectTotal<0){
							selectTotal=0;
						}
						if (textcallback != null)
							textcallback.onListen(selectTotal);
						List.remove(path);
					}
					else {
						Message message = Message.obtain(mHandler, 0);
						message.sendToTarget();
					}
				}
			}

		});

		return convertView;
	}
}
