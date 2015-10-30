package com.semperchen.goodfoodhealthyrecipes.mobile.core;

import android.app.Application;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.api.APIConstants;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.net.RequestManager;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.repository.database.DatabaseManager;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.utils.ImageLoaderHelper;

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
        DatabaseManager.init(this);
        RequestManager.init(this);
        ImageLoader.getInstance().init(ImageLoaderHelper.getInstance(this)
                .getImageLoaderConfiguration(APIConstants.Paths.IMAGE_LOADER_CACHE_PATH));

    }

    @Override
    public void onLowMemory() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onLowMemory();
    }


}