package com.semperchen.goodfoodhealthyrecipes.mobile.core.net;

import com.android.volley.Request;
import com.android.volley.Response;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.IntensionData;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.JokeData;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 卡你基巴 on 2015/10/5.
 */
public class IntensionNetWorkManager {
    private final static String INTENSION_URL="https://route.showapi.com/255-1?showapi_appid=10509&showapi_sign=9a3c0e0a988741238bf303cfdb298ea9";

    private static IntensionNetWorkManager mInstance;

    public static IntensionNetWorkManager getInstance(){
        if(mInstance==null){
            mInstance=new IntensionNetWorkManager();
        }
        return mInstance;
    }

    public <T> void sendNetworkRequestForIntension(Response.Listener<IntensionData> listener,Response.ErrorListener errorListener, int type,int page,String tag){
        setNetworkRequestData(listener, errorListener,type, page,"",tag);
    }

    private void setNetworkRequestData(Response.Listener<IntensionData> listener, Response.ErrorListener errorListener, int type, int page,String title,String tag) {
        VolleyWrapper<IntensionData> volleyWrapper = new VolleyWrapper<>(Request.Method.GET,INTENSION_URL,IntensionData.class,listener,errorListener);
        volleyWrapper.addUrlParameter("showapi_timestamp",getTimeStamp());
        volleyWrapper.addUrlParameter("type",type);
        volleyWrapper.addUrlParameter("title",title);
        volleyWrapper.addUrlParameter("page", page);
        volleyWrapper.sendRequest(tag);
    }

    public String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }
}
