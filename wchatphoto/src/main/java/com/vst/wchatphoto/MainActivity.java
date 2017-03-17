package com.vst.wchatphoto;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.vst.wchatphoto.bean.ImageFloderBean;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private GridView mGridView;
    private GridViewAdapter gridViewAdapter;
    private TextView mImageCountTv;
    private Button mDirNameBtn;
    //加载图片时的进度条
    private ProgressDialog mProgressDialog;

    //选择包含图片最多的文件夹作为当前选择的文件夹
    private int mCurrentPicsSize;
    //当前选择的文件夹路径
    private File mCurrentDir;
    //当前选择的文件夹名称
    private String mCurrentDirName;
    //当前选择文件夹路径，使用来更新PopupWindow的文件夹选择
    private String mCurrentDirPath;
    //当前选择的文件夹的所有图片路径
    private List<String> mImgs;
    //辅助类，防止重复扫描同一个文件夹
    private HashSet<String> mDirPahts = new HashSet<>();
    //保存所有的图片文件夹信息
    private ArrayList<ImageFloderBean> mImageFloderBeens;
    //展示所有图片文件的PopupWindow
    private DirListPopupWindow mDirListPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initImages();
        initEvent();
    }


    private void initView() {
        mGridView = (GridView) findViewById(R.id.gv_main);
        mDirNameBtn = (Button) findViewById(R.id.btn_dirname_main);
        mImageCountTv = (TextView) findViewById(R.id.tv_img_count_main);
    }


    private void initImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "没有找到存储卡", Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
        mImageFloderBeens = new ArrayList<>();
        /**
         * 另开线程获取手机中的所有图片文件夹信息
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                //使用ContentProvider扫描手机中的所有图片和图片文件夹
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = MainActivity.this.getContentResolver();
                //只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
                if (mCursor == null) return;
                while (mCursor.moveToNext()) {
                    //获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    //获取该图片的父路径名 ，即图片文件夹路径
                    File parentFile = new File(path).getParentFile();
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloderBean imageFloderBean = null;
                    //防止重复扫描图片文件夹
                    if (mDirPahts.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPahts.add(dirPath);
                        imageFloderBean = new ImageFloderBean();
                        imageFloderBean.setDirPath(dirPath);
                        imageFloderBean.setFirstImagePath(path);
                    }
                    //文件夹下可能包含各种类型的文件，我们只读取图片文件的数量
                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png"))
                                return true;
                            return false;
                        }
                    }).length;
                    imageFloderBean.setImgCount(picSize);
                    mImageFloderBeens.add(imageFloderBean);
                    if (picSize > mCurrentPicsSize) {
                        mCurrentPicsSize = picSize;
                        mCurrentDir = parentFile;
                        mCurrentDirName = dirPath.substring(dirPath.lastIndexOf("/"));
                        mCurrentDirPath = dirPath;
                    }
                }
                mCursor.close();
                mDirPahts = null;
                //图片扫描完成后通知主线程进行UI更新，展示图片
                mHandler.sendEmptyMessage(0x110);
            }
        }).start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            refreshUi();

            initDirPopupWindow();
        }
    };

    /**
     * 图片扫描后更新UI，展示图片和相关信息
     */
    private void refreshUi() {
        mDirNameBtn.setText(mCurrentDirName);
        mImageCountTv.setText(String.valueOf(mCurrentPicsSize));
        gridViewAdapter = new GridViewAdapter(MainActivity.this, Arrays.asList(mCurrentDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png"))
                    return true;
                return false;
            }
        })), mCurrentDir.getAbsolutePath());
        mGridView.setAdapter(gridViewAdapter);
    }

    /**
     * 初始化文件夹列表的PopupWindow
     */
    private void initDirPopupWindow() {
        View popContentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_dir_popupwindow, null);
        //获取手机屏幕宽高
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mDirListPopupWindow = new DirListPopupWindow(popContentView, dm.widthPixels, (int) (dm.heightPixels * 0.6), mImageFloderBeens, mCurrentDirPath);
        mDirListPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //PopupWindow出现时背景变亮
                WindowManager.LayoutParams attributes = getWindow().getAttributes();
                attributes.alpha = 1f;
                getWindow().setAttributes(attributes);
            }
        });
        mDirListPopupWindow.setItemClickListener(new DirListPopupWindow.OnDirListItemClickListener() {
            @Override
            public void onItemClickListener(int position, ImageFloderBean floderBean) {
                mDirNameBtn.setText(floderBean.getDirName());
                mImageCountTv.setText(String.valueOf(floderBean.getImgCount()));
                gridViewAdapter = new GridViewAdapter(MainActivity.this, Arrays.asList(new File(floderBean.getDirPath()).list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png"))
                            return true;
                        return false;
                    }
                })), floderBean.getDirPath());
                mGridView.setAdapter(gridViewAdapter);
                mDirListPopupWindow.dismiss();
                mCurrentDirPath = floderBean.getDirPath();
            }
        });
    }

    private void initEvent() {
        mDirNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置当前选择的文件路径
                mDirListPopupWindow.setCurrentDir(mCurrentDirPath);
                mDirListPopupWindow.showAsDropDown(mDirNameBtn, 0, 0);
                //PopupWindow出现时背景变暗
                WindowManager.LayoutParams attributes = getWindow().getAttributes();
                attributes.alpha = 0.7f;
                getWindow().setAttributes(attributes);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GridViewAdapter.selectImages = null;
    }
}
