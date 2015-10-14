package com.semperchen.goodfoodhealthyrecipes.mobile.core.repository;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static final String TAG = "PreferenceManager";
    private static final boolean DEBUG = true;
    private static final String SHARE_PREFERENCE_NAME = "global.preference";
    private static PreferenceManager instance = null;
    private SharedPreferences preferences;

    private PreferenceManager(Context context) {
        this.preferences = context.getSharedPreferences(SHARE_PREFERENCE_NAME, Activity.MODE_PRIVATE);
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    public static void initialize(Context context) {
        if (instance == null) {
            instance = new PreferenceManager(context);
        }
    }

    /**
     * 获取唯一实例
     *
     * @return 数据源实例
     */
    public static PreferenceManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(TAG + " is not initialized, it should call initialize(..) method first.");
        }
        return instance;
    }

    public void putInt(String key,int value){
        preferences.edit().putInt(key,value).commit();
    }

    public int getInt(String key,int defalueValue){
        return preferences.getInt(key,defalueValue);
    }
}

