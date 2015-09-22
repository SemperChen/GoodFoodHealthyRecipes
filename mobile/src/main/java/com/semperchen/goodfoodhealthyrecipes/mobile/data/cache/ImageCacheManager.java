package com.semperchen.goodfoodhealthyrecipes.mobile.data.cache;

import android.content.Context;
import android.graphics.Bitmap;
import com.android.volley.toolbox.ImageLoader;
import com.semperchen.goodfoodhealthyrecipes.mobile.data.net.RequestManager;

/**
 * <p>缓存管理器</p>
 * <p/>
 * <p>Project HealthExpress.</p>
 * <p>Date 2015/8/29.</p>
 *
 * @author SemperChen
 * @version 1.0-SNAPSHOT
 */
public class ImageCacheManager {

    /**
     * 写入缓存类型
     */
    public enum CacheType {
        DISK, MEMORY
    }

    private static ImageCacheManager mInstance;

    private ImageLoader mImageLoader;

    private ImageLoader.ImageCache mImageCache;

    public static ImageCacheManager getInstance() {
        if (mInstance == null) {
            mInstance = new ImageCacheManager();
        }
        return mInstance;
    }

    /**
     * 初始化缓存写入方式
     *
     * @param context 上下文
     * @param uniqueName 唯一名字
     * @param cacheSize 缓存大小
     * @param compressFormat 图片压缩格式
     * @param quality 压缩质量
     * @param cacheType 缓存类型
     */
    public void init(Context context, String uniqueName, int cacheSize, Bitmap.CompressFormat compressFormat, int quality, CacheType cacheType) {
        switch (cacheType) {
            case DISK:
                mImageCache = new DiskLruImageCache(context, uniqueName, cacheSize, compressFormat, quality);
                break;
            case MEMORY:
                mImageCache = new BitmapLruImageCache(cacheSize);
                break;
            default:
                mImageCache = new BitmapLruImageCache(cacheSize);
                break;
        }

        mImageLoader = new ImageLoader(RequestManager.getRequestQueue(), mImageCache);
    }

    public Bitmap getBitmap(String url) {
        try {
            return mImageCache.getBitmap(createKey(url));
        } catch (NullPointerException e) {
            throw new IllegalStateException("缓存没有被初始化");
        }
    }

    public void putBitmap(String url, Bitmap bitmap) {
        try {
            mImageCache.putBitmap(createKey(url), bitmap);
        } catch (NullPointerException e) {
            throw new IllegalStateException("缓存没有被初始化");
        }
    }

    private String createKey(String url) {
        return String.valueOf(url.hashCode());
    }

    public void getImage(String url, ImageLoader.ImageListener listener) {
        mImageLoader.get(url, listener);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
