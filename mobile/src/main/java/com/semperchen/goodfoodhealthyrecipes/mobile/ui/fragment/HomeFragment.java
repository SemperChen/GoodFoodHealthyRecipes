package com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter.HomeFragmentAdapter;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget.PagerSlidingTabStrip;

/**
 * Created by Semper on 2015/9/17.
 */
public class HomeFragment extends BaseToolbarFragment {

    private ViewPager mViewPager;
    private PagerSlidingTabStrip slidingTabStrip;

    /**
     * 绑定数据
     */
    @Override
    protected void bindData() {
        initializeViews();
        setupToolbar(mView);
        if(mViewPager != null){
            setupViewPager();
            slidingTabStrip.setViewPager(mViewPager);
        }
    }

    /**
     * 获取内容视图布局id
     *
     * @return id
     */
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_home;
    }

    /**
     * 初始化视图
     */
    private void initializeViews() {
        mViewPager= (ViewPager) mView.findViewById(R.id.viewpager);
        slidingTabStrip= (PagerSlidingTabStrip) mView.findViewById(R.id.tabs);
    }

    /**
     * 设置ViewPager
     */
    private void setupViewPager() {

        HomeFragmentAdapter homeFragmentAdapter =new HomeFragmentAdapter(getChildFragmentManager());
        homeFragmentAdapter.addFragment(new RecommendFragment(),getString(R.string.recommend));
        homeFragmentAdapter.addFragment(new AllFragment(),getString(R.string.all));
        homeFragmentAdapter.addFragment(new TopicFragment(),getString(R.string.topic));
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(homeFragmentAdapter);
    }

    /**
     *设置toolbar
     *
     * @param rootView 根视图
     */
    @Override
    protected void setupToolbar(View rootView) {
        super.setupToolbar(rootView);
        getActivity().setTitle(getString(R.string.home));
    }
}
