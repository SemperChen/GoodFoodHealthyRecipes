package com.semperchen.goodfoodhealthyrecipes.mobile.core.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by abc on 2015/10/1.
 */
public class PrefUtils {
    private static final String PREF_NAME = "joke_config";

    public static void putInt(Context context,String key,int value){
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).commit();
    }

    public static int getInt(Context context,String key,int defalueValue){
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key,defalueValue);
    }
}
