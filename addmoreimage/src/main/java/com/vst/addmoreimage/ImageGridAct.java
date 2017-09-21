package com.vst.addmoreimage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @description 详细描述：图片选择适配类
 * @author 刘成伟（wwwlllll@126.com）
 * @date 2014-4-18 下午6:34:26
 */
public class ImageGridAct extends Activity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static final String EXTRA_IMAGE_NUM = "imageNum";

	// ArrayList<Entity> dataList;
	List<ImageItem> dataList;
	GridView gridView;
	ImageGridAdapter adapter;
	AlbumHelper helper;
	Button bt;
	private int mImageCount = 10;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(ImageGridAct.this, "最多选择" + mImageCount + "张图片", 400).show();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRootView();
		initViews();
	}


	public void initViews() {
		// setHeadTitle(getString(R.string.image_grid_head));
		// goBack();
		TitleBarHelper titleBarHelper = new TitleBarHelper(this, R.string.onepmall_photoes);
		titleBarHelper.setLeftMsg("");
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		if (mImageCount > 10) {
			adapter = new ImageGridAdapter(ImageGridAct.this, dataList, mHandler, mImageCount);
		} else {
			adapter = new ImageGridAdapter(ImageGridAct.this, dataList, mHandler);
		}
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new ImageGridAdapter.TextCallback() {
			public void onListen(int count) {
				bt.setText("完成" + "(" + count + ")");
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// if(dataList.get(position).isSelected()){
				// dataList.get(position).setSelected(false);
				// }else{
				// dataList.get(position).setSelected(true);
				// }
				// adapter.notifyDataSetChanged();
			}
		});

	}

	public int getmImageCount() {
		return mImageCount;
	}

	public void setmImageCount(int mImageCount) {
		this.mImageCount = mImageCount;
	}

	public void setRootView() {
		setContentView(R.layout.activity_image_grid);
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		Bimp.drr.remove("");
		dataList = (List<ImageItem>) getIntent().getSerializableExtra(EXTRA_IMAGE_LIST);
		for (ImageItem item : dataList) {
			for (int i = 0; i < Bimp.drr.size(); i++) {
				if (MainActivity.imgUrlArray.get(i).equals(item.imagePath)) {
					item.isSelected = true;
				}
			}
		}
		mImageCount = getIntent().getIntExtra(EXTRA_IMAGE_NUM, 3);

		initViews();
		bt = (Button) findViewById(R.id.bt);
		if (Bimp.drr.size() > 0) {
			bt.setText("完成" + "(" + Bimp.drr.size() + ")");
		}
		bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean needAdd = false;
				ArrayList<String> list = new ArrayList<String>();
				List<String> strList = adapter.List;
				// Iterator<String> it = c.iterator();
				for (int i = 0; i < adapter.removeList().size(); i++) {
					String path = adapter.removeList().get(i);
					Bimp.drr.remove(MainActivity.imgUrlArray.indexOf(path));
					MainActivity.imgUrlArray.remove(path);
				}
				for (int i = 0; i < strList.size(); i++) {
					String path = strList.get(i);
					if (!MainActivity.imgUrlArray.contains(path)) {
						MainActivity.imgUrlArray.add(path);
						needAdd = true;
					} else {
						needAdd = false;
					}
					File uploadFile = ImageTools.saveImgForUpload(path,ImageGridAct.this);
					if (uploadFile != null && uploadFile.exists() && needAdd) {
						list.add(uploadFile.getAbsolutePath());
					}
				}
				for (int i = 0; i < list.size(); i++) {
					// if (Bimp.drr.size() > 0) {
					// for (int j = 0; j < Bimp.drr.size(); j++) {
					// if (Bimp.drr.get(j).equals(list.get(i))) {
					// Bimp.drr.remove(j);
					// }
					// }
					// }
					if (Bimp.drr.size() < mImageCount) {
						Bimp.drr.add(list.get(i));
					}
				}

				if (Bimp.act_bool) {
					Intent intent = null;
					if (mImageCount > 10) {
						// intent = new Intent(ImageGridActivity.this,
						// EshopPublishMessageActivity.class);
					} else {
						// intent = new Intent(ImageGridAct.this,
						// PublicShowOrderAct.class);
					}
					// startActivity(intent);
					Bimp.act_bool = false;
				}
				finish();

			}
		});
		initViews();
	}

}
