package com.vst.moreimage;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 公共标题栏帮助类。不适用与fragment
 * 如果不需要显示，则对应的strid = -1
 * 
 * @author fanxing
 */
public class TitleBarHelper {
	private final Activity mActivity;
	private TextView left;
	private TextView right;
	private TextView title;
	private ImageView rightImg;

	/**
	 * 只有title
	 * @param activity
	 * @param titleStrId
	 */
	public TitleBarHelper(Activity activity, int titleStrId) {
		this(activity, -1, -1, titleStrId);
	}

	/**
	 * 显示左侧返回图标、标题和右侧标题
	 */
	public TitleBarHelper(Activity activity, int rightStrId, int titleStrId) {
		this(activity, -1, rightStrId, titleStrId);
		left.setVisibility(View.VISIBLE);
		left.setText("");
	}
	/**
	 * 左侧优先显示上一个activity的title
	 */
	public TitleBarHelper(Activity activity, int leftStrId, int rightStrId, int titleStrId) {
		mActivity = activity;
		mActivity.setTitle(titleStrId);
		left = (TextView) activity.findViewById(R.id.left);
		left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mActivity.onBackPressed();
			}
		});

		setLeftMsg(leftStrId);
		if (mActivity.getIntent() != null) {//优先显示intent传进来的左侧标题
			String backStr = mActivity.getIntent().getStringExtra("leftTitle");
			if (!TextUtils.isEmpty(backStr)) {
				setLeftMsg(backStr);
			}
		}
		right = (TextView) activity.findViewById(R.id.right);
		setRightMsg(rightStrId);
		rightImg = (ImageView) activity.findViewById(R.id.title_right_iv);
		title = (TextView) activity.findViewById(R.id.title);
		setTitleMsg(titleStrId);
	}

	/** 右侧 begin *********************/
	public void setRightMsg(int rightStrId) {
		if (rightStrId == -1) {
			right.setVisibility(View.GONE);
		}
		else {
			right.setVisibility(View.VISIBLE);
			right.setText(rightStrId);
		}
	}

	public void setRightMsg(String msg) {
		right.setVisibility(View.VISIBLE);
		right.setText(msg);
	}
	
	public void setOnRightClickListener(View.OnClickListener listener) {
		right.setOnClickListener(listener);
	}
	
	// 添加右边图标显示；
	public void setRightImg(int rightSrc) {
		rightImg.setVisibility(View.VISIBLE);
		rightImg.setImageResource(rightSrc);
	}

	public void setOnRightImgClickListener(View.OnClickListener listener) {
		rightImg.setOnClickListener(listener);
	}

	public void hideRightImg() {
		rightImg.setVisibility(View.GONE);
	}

	/** 右侧 end *********************/

	/** 左侧 begin *********************/
	public void setLeftMsg(int leftStrId) {
		if (leftStrId == -1) {
			left.setVisibility(View.GONE);
		}
		else {
			left.setVisibility(View.VISIBLE);
			left.setText(leftStrId);
		}
	}

	public void setLeftMsg(String msg) {
		left.setVisibility(View.VISIBLE);
		left.setText(msg);
	}

	public void setOnLeftClickListener(View.OnClickListener listener) {
		left.setOnClickListener(listener);
	}

	/** 左侧 end *********************/

	/** 标题 begin *********************/
	public void setTitleMsg(int titleStrId) {
		if (titleStrId == -1) {
			title.setVisibility(View.GONE);
			mActivity.setTitle("");
		}
		else {
			title.setVisibility(View.VISIBLE);
			title.setText(titleStrId);
			mActivity.setTitle(titleStrId);
		}
	}

	public void setTitleMsg(String msg) {
		title.setVisibility(View.VISIBLE);
		title.setText(msg);
		mActivity.setTitle(msg);
	}
	/** 标题 end *********************/
}
