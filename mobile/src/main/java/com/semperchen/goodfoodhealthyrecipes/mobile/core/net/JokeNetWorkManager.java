package com.semperchen.goodfoodhealthyrecipes.mobile.core.net;

import com.android.volley.Request;
import com.android.volley.Response;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.JokeData;

/**
 * Created by abc on 2015/10/1.
 */
public class JokeNetWorkManager {
    private final static String JOKE_URL="http://api.1-blog.com/biz/bizserver/xiaohua/list.do";

    private static JokeNetWorkManager mInstance;

    public static JokeNetWorkManager getInstance(){
        if(mInstance==null){
            mInstance=new JokeNetWorkManager();
        }
        return mInstance;
    }

    public <T> void sendNetworkRequestForJoke(Response.Listener<JokeData> listener,Response.ErrorListener errorListener, int size,int pagenum,String tag){
        setNetworkRequestData(listener, errorListener, size, pagenum,tag);
    }

    private void setNetworkRequestData(Response.Listener listener, Response.ErrorListener errorListener, int size, int page,String tag) {
        VolleyWrapper<JokeData> volleyWrapper = new VolleyWrapper<>(Request.Method.GET,JOKE_URL,JokeData.class,listener,errorListener);
        volleyWrapper.addUrlParameter("size",size);
        volleyWrapper.addUrlParameter("page",page);
        volleyWrapper.sendRequest(tag);

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
