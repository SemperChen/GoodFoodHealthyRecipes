package com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.RecipePreViewManager;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.RecipePreviewData;
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
     * 设置监听请求，请求成功设置adapter
     *
     * @return
     */
    private Response.Listener<RecipePreviewData> createMyReqSuccessListener() {
        return new Response.Listener<RecipePreviewData>() {
            @Override
            public void onResponse(RecipePreviewData response) {
                mAdapter = new AllAdapter(getContext(),response);
                mRecyclerView.setAdapter(mAdapter);
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RecommendFragment", "RecipePreview Data failed to load");
            }
        };
    }

    /**
     * 碎片第一次可见时调用此方法
     */
    @Override
    protected void onFirstUserVisible() {
        if(mRecyclerView.getAdapter()==null){
            RecipePreViewManager.getInstance().getRecipePreviewForHashtag(createMyReqSuccessListener(),createMyReqErrorListener(),1);
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
