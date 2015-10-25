package com.semperchen.goodfoodhealthyrecipes.mobile.core.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by 卡你基巴 on 2015/10/21.
 */
public class NetUtils {

    /**
     * 判断是否有网络
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context){
        boolean netState = false;
        ConnectivityManager connectivity  = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity != null){
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if(info == null || !info.isAvailable() || !info.isConnected()){
                netState = false;
            }else{
                netState = true;
            }
        }
        return netState;
    }
}
