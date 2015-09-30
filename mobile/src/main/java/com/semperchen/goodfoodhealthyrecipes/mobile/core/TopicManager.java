package com.semperchen.goodfoodhealthyrecipes.mobile.core;

import android.net.Uri;
import com.android.volley.Request;
import com.android.volley.Response;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.TopicData;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.net.GsonRequest;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.net.RequestManager;

/**
 * <p></p>
 * <p/>
 * <p>Project HealthExpress.</p>
 * <p>Date 2015/8/29.</p>
 *
 * @author SemperChen
 * @version 1.0-SNAPSHOT
 */
public class TopicManager {
    private final String BASE="http://1.oauthdeveloptest.sinaapp.com/test.php";
    private final String PAGE_NUM="page";

    private static TopicManager mInstance;

    public static TopicManager getInstance(){
        if(mInstance==null){
            mInstance=new TopicManager();
        }
        return mInstance;
    }

    public void getRecipePreviewForHashtag(Response.Listener<TopicData> listener, Response.ErrorListener errorListener, int pageNum) {

        //为链接添加参数
        Uri.Builder uriBuilder = Uri.parse(BASE).buildUpon()
                .appendQueryParameter(PAGE_NUM, "" + pageNum);

        String uri = uriBuilder.build().toString();

        GsonRequest<TopicData> request = new GsonRequest<>(Request.Method.GET
                , uri
                , TopicData.class
                , listener
                , errorListener);

        RequestManager.getRequestQueue().add(request);
    }
}
