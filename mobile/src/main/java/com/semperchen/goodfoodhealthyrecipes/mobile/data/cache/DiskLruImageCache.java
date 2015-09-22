package com.semperchen.goodfoodhealthyrecipes.mobile.data.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import com.android.volley.toolbox.ImageLoader;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.*;

/**
 * <p>图片硬存储类</p>
 * <p/>
 * <p>Project HealthExpress.</p>
 * <p>Date 2015/8/29.</p>
 *
 * @author SemperChen
 * @version 1.0-SNAPSHOT
 */
public class DiskLruImageCache implements ImageLoader.ImageCache {

    private final String TAG=DiskLruImageCache.class.getSimpleName();
    private final boolean DEBUG=true;

    private DiskLruCache mDiskLruCache;
    //图片压缩类型
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;
    //流大小
    private static int IO_BUFFER_SIZE = 8 * 1024;
    //压缩质量
    private int mCompressQuality = 70;
    //版本号
    private static final int APP_VERSION = 1;
    //每个key对应1个文件
    private static final int VALUE_COUNT = 1;

    public DiskLruImageCache(Context context, String uniqueName, int diskCacheSize,
                             Bitmap.CompressFormat compressFormat, int quality) {
        try {
            //Log.d(TAG,"uniqueName="+uniqueName);
            final File diskCacheDir = getDiskCacheDir(context, uniqueName);
            mDiskLruCache = DiskLruCache.open(diskCacheDir, APP_VERSION, VALUE_COUNT, diskCacheSize);
            mCompressFormat = compressFormat;
            mCompressQuality = quality;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取缓存目录
     *
     * @param context
     * @param uniqueName 唯一名称
     * @return
     */
    private File getDiskCacheDir(Context context, String uniqueName) {

        String cachePath;
        //判断是否存在内存卡，有则获取内存卡缓存目录，否则获取手机内存缓存目录
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                ||!Environment.isExternalStorageRemovable()){
            cachePath=context.getExternalCacheDir().getPath();
        }else{
            cachePath=context.getCacheDir().getPath();
        }
        File cacheDir= new File(cachePath+File.separator+uniqueName);

        //判断是否有cachePath路径，无则创建
        if(!cacheDir.exists()){
            cacheDir.mkdirs();
        }

        return cacheDir;
    }

    /**
     * 将图片压缩写入file
     *
     * @param bitmap 需要写入内存的图片
     * @param editor
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     */
    private boolean writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor)
            throws IOException {
        OutputStream out = null;
        try {

            out = new BufferedOutputStream(editor.newOutputStream(0), IO_BUFFER_SIZE);
            return bitmap.compress(mCompressFormat, mCompressQuality, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 根据key获取硬缓存
     *
     * @param key
     * @return
     */
    @Override
    public Bitmap getBitmap(String key) {
        key=createKey(key);
        Bitmap bitmap = null;
        DiskLruCache.Snapshot snapshot = null;
        try {

            snapshot = mDiskLruCache.get(key);
            if (snapshot == null) {
                return null;
            }
            final InputStream in = snapshot.getInputStream(0);
            if (in != null) {
                final BufferedInputStream buffIn =
                        new BufferedInputStream(in, IO_BUFFER_SIZE);
                bitmap = BitmapFactory.decodeStream(buffIn);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }
        return bitmap;
    }

    /**
     * 将缓存写入硬内存
     *
     * @param key
     * @param bitmap
     */
    @Override
    public void putBitmap(String key, Bitmap bitmap) {

        if(DEBUG){
            Log.d(TAG,"put_key="+key);
        }
        key=createKey(key);
        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskLruCache.edit(key);
            if (editor == null) {
                return;
            }

            if (writeBitmapToFile(bitmap, editor)) {
                mDiskLruCache.flush();
                editor.commit();
                if (DEBUG) {
                    Log.d("写入测试", "写入： " + key);
                }
            } else {
                editor.abort();
                if (DEBUG) {
                    Log.d("写入测试", "错误： " + key);
                }
            }
        } catch (IOException e) {
            if (DEBUG) {
                Log.d("写入测试", "错误： " + key);
            }
            try {
                if (editor != null) {
                    editor.abort();
                }
            } catch (IOException ignored) {
            }
        }

    }

    public boolean containsKey(String key) {

        boolean contained = false;
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = mDiskLruCache.get(key);
            contained = snapshot != null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }

        return contained;

    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        try {
            mDiskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取硬缓存目录
     *
     * @return
     */
    public File getCacheFolder() {
        return mDiskLruCache.getDirectory();
    }

    /**
     * 创建key值
     * @param url
     * @return
     */
    private String createKey(String url) {
        return String.valueOf(url.hashCode());
    }

}
