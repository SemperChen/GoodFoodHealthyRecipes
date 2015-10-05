package com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.net.JokeManager;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.JokeData;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.activity.MainActivity;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter.JokeMoreAdapter;

/**
 * Created by abc on 2015/9/29.
 */
public class JokeMoreFragment extends BaseToolbarFragment{
    private MainActivity activity;

    private RelativeLayout rlNoNet;
    private RecyclerView rvContent;

    private JokeMoreAdapter jokeMoreAdapter;
    public static JokeMoreFragment getInstance(){
        JokeMoreFragment jokeMoreFragment = new JokeMoreFragment();
        Bundle extraArguments = new Bundle();
        jokeMoreFragment.setArguments(extraArguments);
        return jokeMoreFragment;
    }

    @Override
    protected void bindData() {
        setupToolbar(mView);

        rlNoNet = (RelativeLayout) mView.findViewById(R.id.rl_nonet);
        rvContent = (RecyclerView) mView.findViewById(R.id.rv_content);

        setupRecyclerManager();
        //�������磬��ȡ����
        getDataFromService();
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_joke_more;
    }

    @Nullable
    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //�������
        if(jokeMoreAdapter != null){
            if(rvContent.getAdapter() == null) {
                rvContent.setAdapter(jokeMoreAdapter);
                rlNoNet.setVisibility(View.GONE);
            }
        }else{
            rlNoNet.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    protected void setupToolbar(View rootView) {
        super.setupToolbar(rootView);
        getActivity().setTitle(getString(R.string.meirinxiao));
        if(actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.bg_joke_back);
        }
    }

    private void setupRecyclerManager() {
        //����Ϊһ��
        final int spanCount = 1;
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(
                spanCount,
                StaggeredGridLayoutManager.VERTICAL);
        rvContent.setLayoutManager(mLayoutManager);

    }

    private void getDataFromService() {
        JokeManager.getInstance().sendNetworkRequestForJoke(new Response.Listener<JokeData>() {
            @Override
            public void onResponse(JokeData jokeData) {
                jokeMoreAdapter = new JokeMoreAdapter(activity, jokeData.detail);
                putDataInView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, 20, 0);
    }

    /**
     * �������
     */
    private void putDataInView() {
        rvContent.setAdapter(jokeMoreAdapter);
        rlNoNet.setVisibility(View.GONE);
    }
}
