package com.semperchen.goodfoodhealthyrecipes.mobile.core.net;

import android.net.Uri;
import com.android.volley.Response;
import com.android.volley.VolleyError;

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

    private RequestSuccessListener mRequestSuccessListener;
    private RequestErrorListener mRequestErrorListener;
    private Response.Listener mListener;
    private Response.ErrorListener mErrorListener;

    public VolleyWrapper(int method,
                         String url,
                         Class<T> tClass,
                         RequestSuccessListener mRequestSuccessListener,
                         RequestErrorListener mRequestErrorListener) {
        this.method = method;
        this.url = url;
        this.mClass = tClass;
        this.mRequestSuccessListener = mRequestSuccessListener;
        this.mRequestErrorListener = mRequestErrorListener;
        init();
    }

    /**
     * 初始化
     */
    private void init() {

        mListener = new CreateReqDataSuccessListener();
        mErrorListener = new CreateReqDataErrorListener();

    }

    /**
     * 发送请求
     */
    public void sendRequest() {
        GsonRequest<T> request = new GsonRequest<>(method, url, mClass, mListener, mErrorListener);
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

    /**
     * 请求数据成功监听
     */
    public class CreateReqDataSuccessListener implements Response.Listener {

        @Override
        public void onResponse(Object obj) {
            if (mRequestSuccessListener != null) {
                mRequestSuccessListener.onLoadData(obj);
            } else {
                throw new IllegalStateException("mRequestSuccessListener为空");
            }
        }
    }

    /**
     * 请求数据错误监听
     */
    public class CreateReqDataErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if (mRequestErrorListener != null) {
                mRequestErrorListener.error();
            } else {
                throw new IllegalStateException("mRequestErrorListener为空");
            }
        }
    }

    public interface RequestSuccessListener {
        //请求成功后执行
        void onLoadData(Object obj);
    }

    public interface RequestErrorListener {
        //请求失败后执行
        void error();
    }
}