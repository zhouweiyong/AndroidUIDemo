package com.vst.moreimage;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

public class PhotoAct extends Activity {

	private ArrayList<View> listViews = null;
	private ViewPager pager;
	private MyPageAdapter adapter;
	private int count;

	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
	public List<String> del = new ArrayList<String>();
	public int max;
	private int imageUrlNum;
	private ImageLoader imageLoader;
	// private ImageLoadingListener animateFirstListener;
	RelativeLayout photo_relativeLayout;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bimp.drr.remove("");
		setContentView(R.layout.layout_onepmall_activity_photo);
		imageUrlNum = Bimp.drr.size();
		imageLoader = ImageLoader.getInstance();
		// animateFirstListener = new AnimateFirstDisplayListener();
		photo_relativeLayout = (RelativeLayout) findViewById(R.id.photo_relativeLayout);
		photo_relativeLayout.setBackgroundColor(0x70000000);

		for (int i = 0; i < Bimp.bmp.size(); i++) {
			bmp.add(imageLoader.loadImageSync((Bimp.drr.get(i))));
		}
		for (int i = 0; i < Bimp.drr.size(); i++) {
			drr.add(Bimp.drr.get(i));
		}
		max = Bimp.max;

		Button photo_bt_exit = (Button) findViewById(R.id.photo_bt_exit);
		photo_bt_exit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		Button photo_bt_del = (Button) findViewById(R.id.photo_bt_del);
		photo_bt_del.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (listViews.size() == 1) {
					if (imageUrlNum > 0) {
						Bimp.drr.clear();
						MainActivity.imgUrlArray.clear();
					} else {
						Bimp.bmp.clear();
						Bimp.drr.clear();
						MainActivity.imgUrlArray.clear();
						Bimp.max = 0;
						// FileUtils.deleteDir();
					}
					finish();
				} else {
					if (imageUrlNum > 0) {
						if (count < imageUrlNum) {
							Bimp.drr.remove(count);
							MainActivity.imgUrlArray.remove(count);
						} else {
							String newStr = drr.get(count - imageUrlNum)
									.substring(drr.get(count - imageUrlNum).lastIndexOf("/") + 1, drr.get(count - imageUrlNum).lastIndexOf("."));
							Bimp.bmp.remove(count - imageUrlNum);
							Bimp.drr.remove(count - imageUrlNum);
							MainActivity.imgUrlArray.remove(count - imageUrlNum);
							Bimp.max--;
							bmp.remove(count - imageUrlNum);
							drr.remove(count - imageUrlNum);
							del.add(newStr);
							max--;
						}

					} else {
						String newStr = drr.get(count).substring(drr.get(count).lastIndexOf("/") + 1, drr.get(count).lastIndexOf("."));
						Bimp.bmp.remove(count);
						Bimp.drr.remove(count);
						MainActivity.imgUrlArray.remove(count);
						Bimp.max--;
						bmp.remove(count);
						drr.remove(count);
						del.add(newStr);
						max--;
					}
					pager.removeAllViews();
					listViews.remove(count);
					adapter.setListViews(listViews);
					adapter.notifyDataSetChanged();
				}
			}
		});
		Button photo_bt_enter = (Button) findViewById(R.id.photo_bt_enter);
		photo_bt_enter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Bimp.bmp = bmp;
				Bimp.drr = drr;
				Bimp.max = max;
				for (int i = 0; i < del.size(); i++) {
					// FileUtils.delFile(del.get(i) + ".JPEG");
				}
				finish();
			}
		});

		pager = (ViewPager) findViewById(R.id.viewpager);
		pager.setOnPageChangeListener(pageChangeListener);
		if (listViews == null)
			listViews = new ArrayList<View>();
		for (int i = 0; i < imageUrlNum + bmp.size(); i++) {
			// initListViews(bmp.get(i));//
			ImageView img = new ImageView(this);// 构造textView对象
			img.setBackgroundColor(0xff000000);
			if (imageUrlNum > 0) {
				if (i < imageUrlNum) {
					displayImage(Bimp.drr.get(i), img);
				} else {
					img.setImageBitmap(bmp.get(i - imageUrlNum));
				}
			} else {
				img.setImageBitmap(bmp.get(i));
			}
			img.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			listViews.add(img);// 添加view

		}

		adapter = new MyPageAdapter(listViews);// 构造adapter
		pager.setAdapter(adapter);// 设置适配器
		Intent intent = getIntent();
		int id = intent.getIntExtra("ID", 0);
		pager.setCurrentItem(id);
	}

	// private void delFile(String fileName) {
	// File file = new File(SDPATH + fileName);
	// if (file.isFile()) {
	// file.delete();
	// }
	// file.exists();
	// }

	private void initListViews(Bitmap bm) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		ImageView img = new ImageView(this);// 构造textView对象
		img.setBackgroundColor(0xff000000);
		img.setImageBitmap(bm);
		img.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		listViews.add(img);// 添加view
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {// 页面选择响应函数
			count = arg0;
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {// 滑动中。。。

		}

		public void onPageScrollStateChanged(int arg0) {// 滑动状态改变

		}
	};

	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;// content

		private int size;// 页数

		public MyPageAdapter(ArrayList<View> listViews) {// 构造函数
															// 初始化viewpager的时候给的一个页面
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {// 自己写的一个方法用来添加数据
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {// 返回数量
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {// 销毁view对象
			((ViewPager) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {// 返回view对象
			try {
				((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);
			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

	public void displayImage(String uri, ImageView imageView) {
		// imageLoader.displayImage(uri, imageView, null);
		if (uri.startsWith("http")) {
			 imageView.setImageBitmap(imageLoader.loadImageSync(uri));
		}else{
			imageView.setImageURI(Uri.parse(uri));
		}
	}
}
