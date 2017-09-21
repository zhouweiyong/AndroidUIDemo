package com.vst.moreimage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * 网络图片和本地图片共存版
 * 
 * @author HX1401169
 * 
 */
public class MainActivity extends ActionBarActivity implements OnClickListener {
	private GridView displayUploadPic_gv;// 显示上传图片的视图
	private LinearLayout addPic_layout;// 点击添加图片的按钮layout
	private ImageView addPic_img;// 添加图片的按钮
	private Dialog picDialog;
	public static ArrayList<String> imgUrlArray = new ArrayList<String>();
	private PublicShowOrderGridAdapter adapter;
	private File picFile;
	private int count = -1;// 需要上传图片的张数
	/** 从 拍照中选择不剪裁 */
	private static final int ACTIVITY_RESULT_NO_CROPCAMARA_WITH_DATA = 3;
	private Context mContext;
	private String[] imgs = { "http://img4.duitang.com/uploads/item/201408/17/20140817103921_y2iFA.jpeg",
			"http://img2.duitang.com/uploads/item/201211/17/20121117124726_Hk4SK.thumb.600_0.jpeg",
			"http://img4.duitang.com/uploads/item/201406/12/20140612132247_JnYxC.thumb.700_0.jpeg" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		displayUploadPic_gv = (GridView) findViewById(R.id.display_upload_pic);
		addPic_layout = (LinearLayout) findViewById(R.id.add_pic_layout);
		addPic_img = (ImageView) findViewById(R.id.add_pic);

		addPic_img.setOnClickListener(this);
		adapter = new PublicShowOrderGridAdapter(this);
		displayUploadPic_gv.setAdapter(adapter);
		displayUploadPic_gv.setOnItemClickListener(myClickListener);
		getData();
		onRestart();
	}

	private void getData() {
		for (int i = 0; i < imgs.length; i++) {
			imgUrlArray.add(imgs[i]);
			Bimp.drr.add(imgs[i]);
		}
	}

	private void addPic() {
		if (picDialog == null) {
			picDialog = MMAlert.createTwoChoicAlertNoTitle(this, R.string.common_camera, R.string.common_gallery, new MMAlert.DialogOnItemClickListener() {

				@Override
				public void onItemClickListener(View v, int position) {
					int id = v.getId();
					switch (id) {
					case R.id.item_first:
						if (!ImageTools.isSDCardExist()) {
							Toast.makeText(MainActivity.this, "sd卡不可用", Toast.LENGTH_SHORT).show();
							return;
						}
						Intent cameraIntent = null;
						picFile = ImageTools.initTempFile();
						cameraIntent = ImageTools.getTakeCameraIntent(Uri.fromFile(picFile));
						startActivityForResult(cameraIntent, ACTIVITY_RESULT_NO_CROPCAMARA_WITH_DATA);
						picDialog.dismiss();
						break;

					case R.id.item_second:
						Intent photoIntent = null;
						photoIntent = new Intent(mContext, PicSelectAct.class);
						mContext.startActivity(photoIntent);
						break;
					}
				}

			});
		}
		picDialog.show();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case ACTIVITY_RESULT_NO_CROPCAMARA_WITH_DATA:
			if (Bimp.drr.size() < 10 && picFile != null || picFile.exists()) {
				String cameraImgPath = picFile.getAbsolutePath();
				// 重新把bitmap型变成File；
				picFile = ImageTools.saveImgForUpload(cameraImgPath);
				Bimp.drr.add(picFile.getPath());
				imgUrlArray.add(picFile.getPath());
			}
			break;
		}
	}

	private OnItemClickListener myClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (arg0.getChildCount() - 1 == arg2 && (arg0.getChildCount() != 10 || count == 9)) {
				addPic();
			} else {
				Intent intent = new Intent(MainActivity.this, PhotoAct.class);
				intent.putExtra("ID", arg2);
				startActivity(intent);
			}
		}
	};

	@Override
	protected void onRestart() {
		super.onRestart();
		if (Bimp.drr != null) {
			Bimp.drr.remove("");
			count = Bimp.drr.size();
			if (Bimp.drr != null && Bimp.drr.size() > 0) {
				addPic_layout.setVisibility(View.GONE);
				displayUploadPic_gv.setVisibility(View.VISIBLE);
				if (Bimp.drr.size() < 10) {
					Bimp.drr.add("");
				}
				adapter.setGroup(Bimp.drr);
			} else {
				addPic_layout.setVisibility(View.VISIBLE);
				displayUploadPic_gv.setVisibility(View.GONE);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		imgUrlArray.clear();
		Bimp.drr.clear();
	}

	@Override
	public void onClick(View v) {
		addPic();
	}

}
