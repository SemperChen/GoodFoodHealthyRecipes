package com.semperchen.goodfoodhealthyrecipes.mobile.core;

import android.net.Uri;
import com.android.volley.Request;
import com.android.volley.Response;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.RecipePreviewData;
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
public class RecipePreViewManager {
    private final String BASE="http://1.oauthdeveloptest.sinaapp.com/index.php";
    private final String PAGE_NUM="page";

    private static RecipePreViewManager mInstance;

    public static RecipePreViewManager getInstance(){
        if(mInstance==null){
            mInstance=new RecipePreViewManager();
        }
        return mInstance;
    }

//    public <T> void getDefaultHashtag(Response.Listener<RecipePreviewData> listener, Response.ErrorListener errorListener, int pageNum){
//        getTweetForHashtag(listener, errorListener,  pageNum);
//    }

    public void getRecipePreviewForHashtag(Response.Listener<RecipePreviewData> listener, Response.ErrorListener errorListener, int pageNum) {

        //为链接添加参数
        Uri.Builder uriBuilder = Uri.parse(BASE).buildUpon()
                .appendQueryParameter(PAGE_NUM, "" + pageNum);

        String uri = uriBuilder.build().toString();

        GsonRequest<RecipePreviewData> request = new GsonRequest<>(Request.Method.GET
                , uri
                , RecipePreviewData.class
                , listener
                , errorListener);

        RequestManager.getRequestQueue().add(request);
    }
}
