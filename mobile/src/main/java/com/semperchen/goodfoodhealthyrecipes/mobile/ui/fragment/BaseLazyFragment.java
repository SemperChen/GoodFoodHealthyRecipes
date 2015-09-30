package com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.listener.RecycleViewPauseOnScrollListener;

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
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
