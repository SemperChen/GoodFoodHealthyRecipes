package com.semperchen.goodfoodhealthyrecipes.mobile.data;

import android.net.Uri;
import com.android.volley.Request;
import com.android.volley.Response;
import com.semperchen.goodfoodhealthyrecipes.mobile.data.entity.NewsData;
import com.semperchen.goodfoodhealthyrecipes.mobile.data.net.GsonRequest;
import com.semperchen.goodfoodhealthyrecipes.mobile.data.net.RequestManager;

/**
 * <p></p>
 * <p/>
 * <p>Project HealthExpress.</p>
 * <p>Date 2015/8/29.</p>
 *
 * @author SemperChen
 * @version 1.0-SNAPSHOT
 */
public class NewsManager {
    private final String BASE="http://1.jsontestapp.sinaapp.com/index.php";
    private final String PAGE_NUM="page";

    private static NewsManager mInstance;

    public static NewsManager getInstance(){
        if(mInstance==null){
            mInstance=new NewsManager();
        }
        return mInstance;
    }

    public <T> void getDefaultHashtag(Response.Listener<NewsData> listener, Response.ErrorListener errorListener, int pageNum){
        getTweetForHashtag(listener, errorListener,  pageNum);
    }

    private void getTweetForHashtag(Response.Listener<NewsData> listener, Response.ErrorListener errorListener, int pageNum) {

        //为链接添加参数
        Uri.Builder uriBuilder = Uri.parse(BASE).buildUpon()
                .appendQueryParameter(PAGE_NUM, "" + pageNum);

        String uri = uriBuilder.build().toString();

        GsonRequest<NewsData> request = new GsonRequest<NewsData>(Request.Method.GET
                , uri
                , NewsData.class
                , listener
                , errorListener);

        RequestManager.getRequestQueue().add(request);
    }
}
