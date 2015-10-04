
package com.semperchen.goodfoodhealthyrecipes.mobile.core.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;

import java.io.File;


public class ImageLoaderHelper {

    private Context mContext = null;
    private static volatile ImageLoaderHelper instance = null;

    private ImageLoaderHelper(Context context) {
        mContext = context;
    }

    public static ImageLoaderHelper getInstance(Context context) {
        if (null == instance) {
            synchronized (ImageLoaderHelper.class) {
                if (null == instance) {
                    instance = new ImageLoaderHelper(context);
                }
            }
        }
        return instance;
    }

    public DisplayImageOptions getDisplayOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.loading_image)
                .showImageForEmptyUri(R.mipmap.ic_empty)
                .showImageOnFail(R.mipmap.ic_error)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    public DisplayImageOptions getDisplayOptions(Drawable drawable) {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(drawable)
                .showImageForEmptyUri(drawable)
                .showImageOnFail(drawable)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    public DisplayImageOptions getDisplayOptions(int round) {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(DensityUtils.dip2px(mContext, round)))
                .build();
    }

    public DisplayImageOptions getDisplayOptions(int round, Drawable drawable) {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(drawable)
                .showImageForEmptyUri(drawable)
                .showImageOnFail(drawable)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(DensityUtils.dip2px(mContext, round)))
                .build();
    }

    public DisplayImageOptions getDisplayOptions(boolean isCacheOnDisk) {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.color.default_image_background)
                .showImageForEmptyUri(R.color.default_image_background)
                .showImageOnFail(R.color.default_image_background)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheOnDisk(isCacheOnDisk)
                .considerExifParams(true)
                .build();
    }

    public ImageLoaderConfiguration getImageLoaderConfiguration(String filePath) {
        File cacheDir = null;
        if (!CommonUtils.isEmpty(filePath)) {
            cacheDir = StorageUtils.getOwnCacheDirectory(mContext, filePath);
        } else {
            cacheDir = StorageUtils.getCacheDirectory(mContext);
        }

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(mContext);
        builder.denyCacheImageMultipleSizesInMemory();

        builder.diskCacheSize(512 * 1024 * 1024);
        builder.diskCacheExtraOptions(720, 1280, null);
        builder.diskCache(new UnlimitedDiskCache(cacheDir));
        builder.diskCacheFileNameGenerator(new Md5FileNameGenerator());

        builder.memoryCacheSizePercentage(14);
        builder.memoryCacheSize(2 * 1024 * 1024);
        builder.memoryCacheExtraOptions(720, 1280);
        builder.memoryCache(new WeakMemoryCache());

        builder.threadPoolSize(3);
        builder.threadPriority(Thread.NORM_PRIORITY - 2);

        builder.defaultDisplayImageOptions(getDisplayOptions());

        return builder.build();
    }
}

