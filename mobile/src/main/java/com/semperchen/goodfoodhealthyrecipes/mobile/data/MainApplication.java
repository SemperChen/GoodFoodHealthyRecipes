package com.semperchen.goodfoodhealthyrecipes.mobile.data;

import android.app.Application;
import android.graphics.Bitmap.CompressFormat;
import com.semperchen.goodfoodhealthyrecipes.mobile.data.cache.ImageCacheManager;
import com.semperchen.goodfoodhealthyrecipes.mobile.data.net.RequestManager;

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

	private static int DISK_IMAGECACHE_SIZE = 1024*1024*10;
	private static CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
	private static int DISK_IMAGECACHE_QUALITY = 100;

	@Override
	public void onCreate() {
		super.onCreate();

		init();
	}

	private void init() {
		RequestManager.init(this);
		createImageCache();
	}

	private void createImageCache(){
		ImageCacheManager.getInstance().init(this,
				this.getPackageCodePath()
				, DISK_IMAGECACHE_SIZE
				, DISK_IMAGECACHE_COMPRESS_FORMAT
				, DISK_IMAGECACHE_QUALITY
				, ImageCacheManager.CacheType.DISK);
	}
}