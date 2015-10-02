package com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import com.android.volley.Request;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.api.APIConstants;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.TopicData;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.net.VolleyWrapper;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter.TopicAdapter;

/**
 * Created by Semper on 2015/9/17.
 */
public class TopicFragment extends BaseLazyFragment {

    private TopicAdapter mAdapter;

    /**
     * 绑定数据
     */
    @Override
    protected void bindData() {
        initializeViews();
        setupAdapter();
    }

    /**
     * 发送网络请求
     */
    private void sendNetworkRequest(){
        VolleyWrapper<TopicData> volleyWrapper=new VolleyWrapper<>(
                Request.Method.GET,
                APIConstants.Urls.TOPIC_DATA_URL,
                TopicData.class,
                new VolleyWrapper.RequestSuccessListener() {
                    @Override
                    public void onLoadData(Object obj) {
                        mAdapter = new TopicAdapter((TopicData) obj);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                },
                new VolleyWrapper.RequestErrorListener() {
                    @Override
                    public void error() {

                    }
                });
        volleyWrapper.addUrlParameter("pageIndex",1);
        volleyWrapper.sendRequest();
    }

    /**
     * 获取内容视图布局id
     *
     * @return id
     */
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_topic;
    }

    /**
     * 初始化视图
     */
    private void initializeViews() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.topic_recyclerView);

    }

    /**
     * 设置适配器
     */
    private void setupAdapter() {
        //列数为一列
        final int spanCount = 1;
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(
                spanCount,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    /**
     * 碎片第一次可见时调用此方法
     */
    @Override
    protected void onFirstUserVisible() {
        if(mRecyclerView.getAdapter()==null){
            sendNetworkRequest();
        }
    }

    @Override
    protected void onFirstUserInvisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }
}
