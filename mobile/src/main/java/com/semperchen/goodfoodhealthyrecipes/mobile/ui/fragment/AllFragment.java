package com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.api.APIConstants;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.RecipePreviewData;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.net.VolleyWrapper;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter.AllAdapter;

/**
 * Created by Semper on 2015/9/17.
 */
public class AllFragment extends BaseLazyFragment {

    private AllAdapter mAdapter;

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
    private void sendNetworkRequest() {
        VolleyWrapper<RecipePreviewData> volleyWrapper = new VolleyWrapper<>(
                Request.Method.GET,
                APIConstants.Urls.RECIPE_PREVIEW_URL,
                RecipePreviewData.class,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object obj) {
                        mAdapter = new AllAdapter((RecipePreviewData) obj);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        volleyWrapper.addUrlParameter("pageIndex", 1);
        volleyWrapper.sendRequest();
    }

    /**
     * 获取内容视图布局id
     *
     * @return id
     */
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_all;
    }

    /**
     * 初始化视图
     */
    private void initializeViews() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.all_recyclerView);

    }

    /**
     * 设置适配器
     */
    private void setupAdapter() {
        //列数为两列
        int spanCount = 2;
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
        if (mRecyclerView.getAdapter() == null) {
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
