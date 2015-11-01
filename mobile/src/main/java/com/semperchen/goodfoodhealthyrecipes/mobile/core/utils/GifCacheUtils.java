package com.semperchen.goodfoodhealthyrecipes.mobile.core.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import pl.droidsonroids.gif.GifDrawable;

import java.util.LinkedHashMap;

/**
 * Created by 卡你基巴 on 2015/11/1.
 */
public class GifCacheUtils{
    private LruCache<String,GifDrawable> mGifCache;

    public GifCacheUtils(){
        long maxMemory = Runtime.getRuntime().maxMemory();

        mGifCache = new LruCache<String,GifDrawable>((int)(maxMemory/8)){
            @Override
            protected int sizeOf(String key, GifDrawable value) {
                return (int)value.getInputSourceByteCount();
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, GifDrawable oldValue, GifDrawable newValue) {
                if(evicted){
                    if(oldValue!=null && oldValue.isRecycled()){
                        oldValue.isRecycled();
                    }
                }
            }
        };
    }


    /**
     * 从内存获取
     * @param url
     */
    public GifDrawable getGifFromMemory(String url){
        GifDrawable gifDrawable = mGifCache.get(url);
        return gifDrawable;
    };

    /**
     * 往内存里存储
     * @param url
     * @param gifDrawable
     */
    public void setGifToMemory(String url,GifDrawable gifDrawable){
        mGifCache.put(url, gifDrawable);
    }

    public void clearGifFromMemory(){
        mGifCache.evictAll();
        System.out.println("clear-->");
    }
}
