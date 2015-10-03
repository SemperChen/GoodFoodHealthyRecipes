package com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.api.APIConstants;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.RecipePreviewData;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.net.VolleyWrapper;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter.AllAdapter;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter.RecommendAdapter;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget.PullCallback;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget.PullToLoadView;

/**
 * Created by Semper on 2015/9/17.
 */
public class AllFragment extends BaseLazyFragment {

    private AllAdapter mAdapter;
    private PullToLoadView mPullToLoadView;

    private int currentPage=-1;
    private int nextPage=0;
    /**
     * 绑定数据
     */
    @Override
    protected void bindData() {
        initializeView();
        setupManager();
    }

    /**
     * 初始化视图
     */
    private void initializeView() {

        mPullToLoadView= (PullToLoadView) mView.findViewById(R.id.pulltoLoadview);
        mPullToLoadView.setColorSchemeResources(
                android.R.color.holo_green_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);
        mRecyclerView=mPullToLoadView.getRecyclerView();
        mPullToLoadView.setPullCallback(new PullLoadMoreListener());

    }

    /**
     * 刷新和加载监听
     */
    private class PullLoadMoreListener implements PullCallback {
        @Override
        public void onLoadMore() {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //当所接回来的数据是最后一页的时候，这组数据中的nextPage等于0，于是nextPage<=currentPage
                    if(nextPage<=currentPage){

                        Toast.makeText(getContext(), "已经没有更多了", Toast.LENGTH_SHORT).show();
                        mPullToLoadView.setComplete();
                    }else {
                        sendNetworkRequest();
                    }

                }
            },3000);
        }

        @Override
        public void onRefresh() {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendNetworkRequest();
                    currentPage=0;
                    nextPage=1;
                }
            },2000);

        }
    }

    /**
     * 请求监听，请求成功后执行onResponse
     */
    private class RequestSuccessListener implements Response.Listener{

        @Override
        public void onResponse(Object obj) {
            RecipePreviewData mRecipePreviewData= (RecipePreviewData) obj;
            if(mAdapter==null){
                mAdapter = new AllAdapter(mRecipePreviewData);
                mRecyclerView.setAdapter(mAdapter);
            }else{
                //如果是执行的操作是刷新，则清除适配器内容
                if(mPullToLoadView.isRefreshing()){
                    mAdapter.clear();
                }
                mAdapter.getRecipePreviews().addAll(mRecipePreviewData.getRecipePreviews());
                mAdapter.notifyDataSetChanged();
                if(mPullToLoadView.isLoadingMore()){
                    currentPage=mRecipePreviewData.getCurrentPage();
                    nextPage=mRecipePreviewData.getNextPage();
                }
            }

            mPullToLoadView.setComplete();
        }
    }

    /**
     * 请求失败监听
     */
    private class RequestErrorListener implements Response.ErrorListener{
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    }

    /**
     * 发送网络请求
     */
    private void sendNetworkRequest() {
        VolleyWrapper<RecipePreviewData> volleyWrapper = new VolleyWrapper<>(
                Request.Method.GET,
                APIConstants.Urls.RECIPE_PREVIEW_URL,
                RecipePreviewData.class,
                new RequestSuccessListener(),
                new RequestErrorListener());

        //添加链接参数
        volleyWrapper.addUrlParameter("page", nextPage);
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
     * 设置RecyclerView布局管理器
     */
    private void setupManager() {
        //列数为两列
        final int spanCount = 2;
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

            mPullToLoadView.initLoad();
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
