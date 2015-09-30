package com.semperchen.goodfoodhealthyrecipes.mobile.core;

import android.app.Application;
import android.content.Context;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.net.RequestManager;

/**
 * <p>自定义Application</p>
 * <p/>
 * <p>Project HealthExpress.</p>
 * <p>Date 2015/8/29.</p>
 *
 * @author SemperChen
 * @version 1.0-SNAPSHOT
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        RequestManager.init(this);
        initImageLoader(getApplicationContext());
    }

    public static void initImageLoader(Context context) {
        //默认配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.loading_image) //加载图片时，默认显示图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true) //写入内存
                .cacheOnDisk(true) //写入储存卡
                .considerExifParams(true)
                        // .displayer(new CircleBitmapDisplayer(Color.WHITE, 2)) //圆形图片配置
                .build();

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.defaultDisplayImageOptions(options);
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator()); //图片写入内存卡时，用Md5方式取名
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
//        config.memoryCacheSize(5 * 1024 * 1024);
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // 打印日志

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }


}