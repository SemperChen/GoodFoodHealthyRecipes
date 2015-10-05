package com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;

/**
 * Created by Semper on 2015/9/17.
 */
public abstract class BaseToolbarFragment extends BaseFragment {
    protected  ActionBar actionBar;

    protected void setupToolbar(View rootView) {

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
