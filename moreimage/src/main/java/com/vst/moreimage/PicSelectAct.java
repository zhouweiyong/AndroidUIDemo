package com.vst.moreimage;


import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class PicSelectAct extends Activity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	List<ImageBucket> dataList;
	GridView gridView;
	ImageBucketAdapter adapter;
	AlbumHelper helper;
	public static Bitmap bimap;
	private int number=10;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRootView();
		initDatas() ;
		initViews();
	}
	/**
	 * 初始化数据
	 */
	private void initDatas() {
		dataList = helper.getImagesBucketList(false);
		bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_add_pic);
		number= getIntent().getIntExtra(ImageGridAct.EXTRA_IMAGE_NUM, 10);
	}

	/**
	 * 初始化view视图
	 */
	public void initViews() {
//		setHeadTitle(getString(R.string.image_grid_head));
//		goBack();
		TitleBarHelper titleBarHelper = new TitleBarHelper(this, R.string.onepmall_photoes);
		titleBarHelper.setLeftMsg("");

		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new ImageBucketAdapter(PicSelectAct.this, dataList);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(PicSelectAct.this, ImageGridAct.class);
				intent.putExtra(PicSelectAct.EXTRA_IMAGE_LIST, (Serializable) dataList.get(position).imageList);
				intent.putExtra(ImageGridAct.EXTRA_IMAGE_NUM, number);
				startActivity(intent);
				finish();
			}
		});
	}

	public void setRootView() {
		setContentView(R.layout.layout_image_bucket);
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		initDatas();
		initViews();
	}
}
