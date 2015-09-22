package com.semperchen.goodfoodhealthyrecipes.mobile.data.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import com.android.volley.toolbox.ImageLoader;

/**
 * <p>图片缓存类</p>
 * <p/>
 * <p>Project HealthExpress.</p>
 * <p>Date 2015/8/29.</p>
 *
 * @author SemperChen
 * @version 1.0-SNAPSHOT
 */
public class BitmapLruImageCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

    private final String TAG = BitmapLruImageCache.class.getSimpleName();
    private final boolean DEBUG=true;

    /**
     *
     * @param maxSize 最大缓存值
     */
    public BitmapLruImageCache(int maxSize) {
        super(maxSize);
    }

    /**
     * 返回图片所占的内存字节数
     *
     * @param key key值
     * @param value 位图
     * @return
     */
    protected int sizeOf(String key, Bitmap value) {
        return value.getByteCount() * value.getRowBytes();
    }

    /**
     * 获取缓存
     *
     * @param key key值
     * @return
     */
    @Override
    public Bitmap getBitmap(String key) {
        if(DEBUG){
            Log.v(TAG, "获取缓存");
        }
        return get(key);
    }

    /**
     * 写入缓存
     *
     * @param key key值
     * @param bitmap 位图
     */
    @Override
    public void putBitmap(String key, Bitmap bitmap) {
        if(DEBUG){
            Log.v(TAG, "添加缓存");
        }
        put(key, bitmap);
    }
}
