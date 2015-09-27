package com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter.RecommendAdapter;

import java.util.ArrayList;

/**
 * Created by Semper on 2015/9/17.
 */
public class RecommendFragment extends Fragment {

    private View mView;
    private RecyclerView mRecyclerView;
    private RecommendAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> items = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView=inflater.inflate(R.layout.fragment_recommend,container,false);
        initializeView();
        setupAdapter();
        return mView;
    }

    /**
     * 初始化视图
     */
    private void initializeView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recommend_recyclerView);

    }

    /**
     * 设置适配器
     */
    private void setupAdapter() {
        //列数为两列
        int spanCount = 2;
        mLayoutManager = new StaggeredGridLayoutManager(
                spanCount,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecommendAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
    }
}
