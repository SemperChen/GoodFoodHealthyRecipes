package com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;

/**
 * Created by Semper on 2015/9/17.
 */
public class FavoritesFragment extends BaseFragment{

    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView=inflater.inflate(R.layout.fragment_favorutes,container,false);
        setupToolbar(mView);
        return mView;
    }

    @Override
    protected void setupToolbar(View rootView) {
        super.setupToolbar(rootView);
    }
}
