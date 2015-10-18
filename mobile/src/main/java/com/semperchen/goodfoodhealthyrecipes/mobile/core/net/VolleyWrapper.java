package com.semperchen.goodfoodhealthyrecipes.mobile.core.net;

import android.net.Uri;
import com.android.volley.Response;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Semper on 2015/10/2.
 */
public class VolleyWrapper<T> {

    private String url;
    private int method;
    private Class<T> mClass;

    private Response.Listener mListener;
    private Response.ErrorListener mErrorListener;

    public VolleyWrapper(int method,
                         String url,
                         Class<T> tClass,
                         Response.Listener mListener,
                         Response.ErrorListener mErrorListener) {
        this.method = method;
        this.url = url;
        this.mClass = tClass;
        this.mListener = mListener;
        this.mErrorListener = mErrorListener;
    }

    /**
     * 发送请求
     */
    public void sendRequest() {
        GsonRequest<T> request = new GsonRequest<>(method, url, mClass, mListener, mErrorListener);
        RequestManager.getRequestQueue().add(request);
    }

    /**
     * 发送请求并添加标记
     */
    public void sendRequest(String tag) {
        GsonRequest<T> request = new GsonRequest<>(method, url, mClass, mListener, mErrorListener);
        request.setTag(tag);
        RequestManager.getRequestQueue().add(request);
    }

    /**
     * 为url添加参数
     */
    public void addUrlParameter(String param,Object value){
        Uri.Builder builder = Uri.parse(url).buildUpon().appendQueryParameter(param,value.toString());
        url = builder.build().toString();
    }

    public void addUrlParameter(Map<String, Object> urlParams) {
        Uri.Builder builder = Uri.parse(url).buildUpon();
        for (int i = 0; i < urlParams.size(); i++) {
            Set keys = urlParams.keySet();
            Iterator iterator = keys.iterator();
            for (; iterator.hasNext(); ) {
                String key = (String) iterator.next();
                Object value = urlParams.get(key);
                builder.appendQueryParameter(key, value.toString());
            }
        }
        url = builder.build().toString();
    }
}