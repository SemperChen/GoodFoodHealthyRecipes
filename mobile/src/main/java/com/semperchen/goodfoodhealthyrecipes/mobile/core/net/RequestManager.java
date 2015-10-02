package com.semperchen.goodfoodhealthyrecipes.mobile.core.net;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * <p>请求队列管理器</p>
 * <p/>
 * <p>Project HealthExpress.</p>
 * <p>Date 2015/8/29.</p>
 *
 * @author SemperChen
 * @version 1.0-SNAPSHOT
 */
public class RequestManager {

    private static RequestQueue mRequestQueue;

    private RequestManager() {
    }

    public static void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    /**
     *
     * @return RequestQueue实例
     */
    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestManager没有被初始化");
        }
    }
}
