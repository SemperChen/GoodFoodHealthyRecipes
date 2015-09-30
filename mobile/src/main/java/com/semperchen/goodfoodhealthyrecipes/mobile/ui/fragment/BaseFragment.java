package com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Semper on 2015/9/30.
 */
public abstract class BaseFragment extends Fragment {

    protected View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getContentViewLayoutId() != 0) {
            mView =inflater.inflate(getContentViewLayoutId(), container,false);
            bindData();
            return mView;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    protected abstract void bindData();

    protected abstract int getContentViewLayoutId();
}
