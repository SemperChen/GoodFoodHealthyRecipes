package com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment;

import android.view.View;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;

/**
 * Created by Semper on 2015/9/17.
 */
public class FavoritesFragment extends BaseToolbarFragment {

    /**
     * 绑定数据
     */
    @Override
    protected void bindData() {
        setupToolbar(mView);
    }

    /**
     * 获取内容视图布局id
     *
     * @return id
     */
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_favorutes;
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
}
