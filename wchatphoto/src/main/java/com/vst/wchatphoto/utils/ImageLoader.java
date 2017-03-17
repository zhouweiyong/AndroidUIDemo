package com.vst.wchatphoto.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2017/3/13
 * class description:请输入类描述
 */
public class ImageLoader {
    private static ImageLoader mInstance;
    //图片加载策略，FIFO先进先出，LIFO后进先出
    private Type mType;
    //线程数量
    private int mThreadCountp;
    //创建一个线程池管理线程
    private ExecutorService mThreadPool;
    //使用LruCache缓存
    private LruCache<String, Bitmap> mLruCache;
    //任务队列
    private LinkedList<Runnable> mTasks;
    //引入一个信号，管理线程的读取速度，信号量为线程池的大小
    private volatile Semaphore mPoolSemaphore;
    //引入一个信号，防止mPoolThreadHandler未初始化完成
    private volatile Semaphore mSemaphore = new Semaphore(0);
    //轮询线程
    private Thread mPoolThread;
    //轮询线程handler
    private Handler mPoolThreadHandler;
    //ui线程的Handler，用来设置图片
    private Handler mUiHandler;


    public enum Type {
        FIFO, LIFO
    }

    public static ImageLoader getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(1, Type.LIFO);
                }
            }
        }
        return mInstance;
    }

    public static ImageLoader getInstance(int threadCount, Type type) {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(threadCount, type);
                }
            }
        }
        return mInstance;
    }

    private ImageLoader(int threadCount, Type type) {
        init(threadCount, type);
    }

    private void init(int threadCount, Type type) {
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        //获取应用的最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //设置图片占用的内存为可用内存的八分之一
        int imageCacheSize = maxMemory / 8;
        mLruCache = new LruCache<String, Bitmap>(imageCacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
        mTasks = new LinkedList<>();
        mType = type == null ? Type.LIFO : type;
        mPoolSemaphore = new Semaphore(threadCount);
        //轮询线程，从线程池中获取加载图片的任务，然后执行
        mPoolThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        try {
                            mPoolSemaphore.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mThreadPool.execute(getTask());
                    }
                };
                mSemaphore.release();
                Looper.loop();
            }
        };
        mPoolThread.start();
    }

    public void loadImage(final String path, final ImageView imageView) {
        imageView.setTag(path);
        mUiHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ImageHolder holder = (ImageHolder) msg.obj;
                if (holder.imageView.getTag().equals(holder.path)) {
                    holder.imageView.setImageBitmap(holder.bitmap);
                }
            }
        };
        /**
         * 从内存中读取图片，如果没有则开线程从存储卡中读取
         * 线程使用线程池管理，同时根据加载策略（FIFO,LIFO）来执行
         */
        Bitmap bitmap = getBitmapFromLruCache(path);
        if (bitmap != null) {
            ImageHolder holder = new ImageHolder();
            holder.bitmap = bitmap;
            holder.path = path;
            holder.imageView = imageView;
            Message msg = mUiHandler.obtainMessage();
            msg.obj = holder;
            mUiHandler.sendMessage(msg);
        } else {
            addTask(new Runnable() {
                @Override
                public void run() {
                    ImageSize imageSize = getImageViewSize(imageView);
                    int reqWidth = imageSize.width;
                    int reqHeight = imageSize.height;
                    Bitmap bmp = decodeSampledBitmapFromResource(path, reqWidth, reqHeight);
                    addBitmapToLruCache(path, bmp);
                    ImageHolder holder = new ImageHolder();
                    holder.imageView = imageView;
                    holder.path = path;
                    holder.bitmap = bmp;
                    Message msg = mUiHandler.obtainMessage();
                    msg.obj = holder;
                    mUiHandler.sendMessage(msg);
                    mPoolSemaphore.release();
                }
            });
        }
    }

    //获取ImageView的宽高
    private ImageSize getImageViewSize(ImageView imageView) {
        ImageSize imageSize = new ImageSize();
        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        int width = imageView.getWidth();
        if (width <= 0) width = params.width;
        if (width <= 0) width = getImageViewFileValue(imageView, "mMaxWidth");
        if (width <= 0) width = displayMetrics.widthPixels;
        imageSize.width = width;

        int height = imageView.getHeight();
        if (height <= 0) height = params.height;
        if (height <= 0) height = getImageViewFileValue(imageView, "mMaxHeight");
        if (height <= 0) height = displayMetrics.heightPixels;
        imageSize.height = height;
        return imageSize;
    }

    //通过反射获取ImageView的最大宽高
    private int getImageViewFileValue(Object object, String fileName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fileName);
            field.setAccessible(true);
            int fieldValue = (int) field.get(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }

    //根据ImageView的宽高获取图片
    private Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {
        //第一次解析图片，设置inJustDecodeBounds为true，表示读取图片参数，但不把图片写入内存中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        //计算图片压缩比例，并设置
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        //再次解析图片，把inJustDecodeBounds设置为false，表示读取图片到内存中
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

    //计算图片压缩比例
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        //源图片的宽度
        int realWidth = options.outWidth;
        int realHeight = options.outHeight;
        int inSampleSize = 1;
        if (realWidth > reqWidth && realHeight > reqHeight) {
            //计算出实际宽度和目标宽度的比率
            int widthRatio = Math.round((float) realWidth / (float) reqWidth);
            int heightRatio = Math.round((float) realHeight / (float) reqHeight);
            inSampleSize = Math.max(widthRatio, heightRatio);
        }
        return inSampleSize;
    }

    //添加任务到任务队列中
    private synchronized void addTask(Runnable runnable) {
        try {
            //如果mPoolThreadHandler为null，则阻塞等待
            if (mPoolThreadHandler == null)
                mSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mTasks.add(runnable);
        mPoolThreadHandler.sendEmptyMessage(0x110);
    }

    //从任务队列中获取任务
    private synchronized Runnable getTask() {
        Runnable runnable = null;
        if (mType == Type.FIFO) {
            runnable = mTasks.removeFirst();
        } else {
            runnable = mTasks.removeLast();
        }
        return runnable;
    }

    /**
     * 把Bitmap添加到内存中
     */
    private void addBitmapToLruCache(String path, Bitmap bmp) {
        if (getBitmapFromLruCache(path) == null) {
            if (bmp != null) {
                mLruCache.put(path, bmp);
            }
        }
    }

    //从内存中获取Bitmap
    private Bitmap getBitmapFromLruCache(String path) {
        return mLruCache.get(path);
    }

    //用于帮助在UI线程中设置bitmap
    private class ImageHolder {
        String path;
        ImageView imageView;
        Bitmap bitmap;
    }

    //用于帮助获取ImageView的尺寸
    private class ImageSize {
        int width;
        int height;
    }
}
