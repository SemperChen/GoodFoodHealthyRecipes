package com.semperchen.goodfoodhealthyrecipes.mobile.core.net;

import android.net.Uri;
import com.android.volley.Request;
import com.android.volley.Response;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.JokeData;

/**
 * Created by abc on 2015/10/1.
 */
public class JokeManager {
    private final static String JOKE_URL="http://api.1-blog.com/biz/bizserver/xiaohua/list.do";

    private static JokeManager mInstance;

    public static JokeManager getInstance(){
        if(mInstance==null){
            mInstance=new JokeManager();
        }
        return mInstance;
    }

    public <T> void sendNetworkRequestForJoke(Response.Listener<JokeData> listener,Response.ErrorListener errorListener, int size,int pagenum){
        setNetworkRequestData(listener, errorListener, size, pagenum);
    }

    private void setNetworkRequestData(Response.Listener listener, Response.ErrorListener errorListener, int size, int page) {
        VolleyWrapper<JokeData> volleyWrapper = new VolleyWrapper<>(Request.Method.GET,JOKE_URL,JokeData.class,listener,errorListener);
        volleyWrapper.addUrlParameter("size",size);
        volleyWrapper.addUrlParameter("page",page);
        volleyWrapper.sendRequest();

//        Uri.Builder uriBuilder = Uri.parse(JOKE_URL).buildUpon().
//                appendQueryParameter("size",String.valueOf(size)).appendQueryParameter("page",String.valueOf(page));
//
//        String uri = uriBuilder.build().toString();
//
//        GsonRequest<JokeData> request = new GsonRequest<JokeData>(Request.Method.GET,
//                uri,
//                JokeData.class,
//                listener,
//                errorListener);
//
//        RequestManager.getRequestQueue().add(request);
    }
}
