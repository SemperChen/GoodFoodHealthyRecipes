package com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Semper on 2015/9/17.
 */
public class FavoritesFragment extends BaseToolbarFragment {
    private Activity mActivity;

    private FrameLayout mFlContent;
    private ListView mMenuList;

    /**
     * 绑定数据
     */
    @Override
    protected void bindData() {
        setupToolbar(mView);
        mFlContent = (FrameLayout) mView.findViewById(R.id.fl_content);

        initView();
    }

    private void initView() {
        View view = View.inflate(getActivity(),R.layout.fragment_favorites_menu,null);
        mMenuList = (ListView) view.findViewById(R.id.lv_content);
        mFlContent.addView(view);
    }

    /**
     * 获取内容视图布局id
     *
     * @return id
     */
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_favorites;
    }

    /**
     *设置toolbar
     *
     * @param rootView 根视图
     */
    @Override
    protected void setupToolbar(View rootView) {
        super.setupToolbar(rootView);
        getActivity().setTitle(getString(R.string.favorites));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initAdapter();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    private void initAdapter() {
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,initData());
        mMenuList.setAdapter(mAdapter);
    }

    private List<String> initData() {
        List<String> data = new ArrayList<String>();
        for(int i=0;i<50;i++){
            data.add("第"+(i+1)+"条菜谱。");
        }
        return data;
    }
}
