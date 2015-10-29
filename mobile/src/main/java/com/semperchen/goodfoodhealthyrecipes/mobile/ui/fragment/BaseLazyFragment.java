package com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.global.GlobalContants;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.listener.RecycleViewPauseOnScrollListener;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.activity.MainActivity;

/**
 * Created by Semper on 2015/9/30.
 */
public abstract class BaseLazyFragment extends BaseFragment {

    private boolean isFirstResume=true;
    private boolean isFirstVisible=true;
    private boolean isFirstInvisible=true;
    private boolean isPrepared;
    //滑动时是否暂停图片加载
    protected boolean pauseOnScroll = true;
    //往下沉淀时是否暂停图片加载
    protected boolean pauseOnFling = true;
    protected RecyclerView mRecyclerView;

    private BroadcastReceiver mBackReceiver;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
        initBackReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        applyScrollListener();
        if(isFirstResume){
            isFirstResume=false;
            return;
        }
        if (getUserVisibleHint()){
            onUserVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mBackReceiver != null){
            getActivity().unregisterReceiver(mBackReceiver);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(isFirstVisible){
                isFirstVisible=false;
                initPrepare();
            }else {
                onUserVisible();
            }
        }else {
            if (isFirstInvisible){
                isFirstInvisible=false;
                onFirstUserInvisible();
            }else {
                onUserInvisible();
            }
        }
    }

    /**
     * 初始化准备数据
     */
    private synchronized void initPrepare(){
        if(isPrepared){
            onFirstUserVisible();
        }else {
            isPrepared=true;
        }
    }

    /**
     * 获取点击返回键响应
     */
    private void initBackReceiver() {
        if(mBackReceiver == null) {
            mBackReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (GlobalContants.BACK_ACTION.equals(action)) {
                        ((MainActivity) getActivity()).showBackDialog();
                    }
                }
            };

            //注册广播
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(GlobalContants.BACK_ACTION);
            getActivity().registerReceiver(mBackReceiver, intentFilter);
        }
    }

    /**
     * 应用滚动监听
     */
    private void applyScrollListener() {
        mRecyclerView.setOnScrollListener(new RecycleViewPauseOnScrollListener(ImageLoader.getInstance(), pauseOnScroll, pauseOnFling));
    }


    protected abstract void onFirstUserVisible();
    protected abstract void onFirstUserInvisible();

    protected abstract void onUserVisible();
    protected abstract void onUserInvisible();

}
