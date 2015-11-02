package com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.RecipePreview;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.global.GlobalContants;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.repository.datadao.RecipePreviewDao;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.repository.dataservice.RecipePreviewService;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.utils.AnimationUtils;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.utils.DensityUtils;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.utils.RecipeUtils;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.activity.MainActivity;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.activity.RecipeActivity;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter.FavoritesMenuAdapter;

import java.util.List;

/**
 * Created by Semper on 2015/9/17.
 */
public class FavoritesFragment extends BaseToolbarFragment {
    private Activity mActivity;

    private FrameLayout mFlContent;
    private SwipeMenuListView mMenuList;
    private TextView mNoAllFavorites,mNoSearchFavorites;

    private FavoritesMenuAdapter mAdapter;
    private RecipePreviewService mRecipePreviewService;
    private List<RecipePreview> mRecipePreviews;

    private BroadcastReceiver mBackReceiver;
    private BroadcastReceiver mRecipeBackReceiver;
    /**
     * 绑定数据
     */
    @Override
    protected void bindData() {
        setupToolbar(mView);
        mFlContent = (FrameLayout) mView.findViewById(R.id.fl_content);

        initView();
        initBackReceiver();
    }

    private void initView() {
        View view = View.inflate(getActivity(),R.layout.fragment_favorites_menu,null);
        mMenuList = (SwipeMenuListView) view.findViewById(R.id.lv_content);
        mNoAllFavorites = (TextView) view.findViewById(R.id.tv_noallfavorites);
        mNoSearchFavorites = (TextView) view.findViewById(R.id.tv_nosearchfavorites);
        mFlContent.addView(view);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu swipeMenu) {
                SwipeMenuItem item = new SwipeMenuItem(getActivity());
                item.setBackground(new ColorDrawable(Color.rgb(0xFF, 0x98, 0x00)));
                item.setWidth(DensityUtils.dip2px(getActivity(), 90));
                item.setTitle("删除");
                item.setTitleSize(18);
                item.setTitleColor(Color.WHITE);
                swipeMenu.addMenuItem(item);
            }
        };
        mMenuList.setMenuCreator(creator);
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

        initData();
        initAdapter();
        initListener();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mBackReceiver != null){
            getActivity().unregisterReceiver(mBackReceiver);
        }
        if(mRecipeBackReceiver != null){
            getActivity().unregisterReceiver(mRecipeBackReceiver);
        }
    }

    private void initData() {
        if(mRecipePreviewService == null) {
            mRecipePreviewService = new RecipePreviewDao(getActivity());
        }
        mRecipePreviews = mRecipePreviewService.findAll();
    }

    private void initAdapter() {
        if(mRecipePreviews.size() <= 0 || mRecipePreviews == null){
            showNoAllFavorites();
        }else{
            showMenuList();
            mAdapter = new FavoritesMenuAdapter(getActivity(),mRecipePreviews);
            mMenuList.setAdapter(mAdapter);
        }
    }

    private void initListener() {
        //点击删除按钮
        mMenuList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu swipeMenu, int index) {
                RecipePreview mRecipePreview = mRecipePreviews.get(position);
                mRecipePreviewService.deleteByRecipeId(String.valueOf(mRecipePreview.getRecipeId()));
                RecipeUtils.clearRecipeIdInPreference(String.valueOf(mRecipePreview.getRecipeId()));
                AnimationUtils.deleteListViewItemAnim(mMenuList.getChildAt(position), mAdapter, position, mRecipePreviews, mMenuList, mNoAllFavorites);
                return false;
            }
        });

        //点击item
        mMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RecipeActivity.class);
                intent.putExtra("recipePreview", mRecipePreviews.get(position));
                getActivity().startActivity(intent);
            }
        });
    }

    /**
     * 搜索并显示结果
     */
    public void searchResult(String value){
        if("".equals(value) || value == null){
            noSearchToAll();
        }else{
            if(mRecipePreviewService != null) {
                mRecipePreviews = mRecipePreviewService.findByName(value);
                if(mRecipePreviews.size() <= 0 || mRecipePreviews == null) {
                    showNoSearchFavorites();
                }else {
                    if (mAdapter != null) {
                        showMenuList();
                        mAdapter.setData(mRecipePreviews);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    /**
     * 搜索为空时显示所有
     */
    public void noSearchToAll(){
        if(mRecipePreviewService == null) {
            mRecipePreviewService = new RecipePreviewDao(getActivity());
        }
        mRecipePreviews = mRecipePreviewService.findAll();
        if(mRecipePreviews.size() <= 0 || mRecipePreviews == null){
            showNoSearchFavorites();
        }else{
            if(mAdapter != null){
                showMenuList();
                mAdapter.setData(mRecipePreviews);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 获取点击返回键响应
     */
    private void initBackReceiver() {
        if(mBackReceiver == null) {
            mBackReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (GlobalContants.BACK_ACTION.equals(action)) {
                        ((MainActivity) getActivity()).showBackDialog();
                    }
                }
            };

            //注册广播
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(GlobalContants.BACK_ACTION);
            getActivity().registerReceiver(mBackReceiver, intentFilter);
        }

        if(mRecipeBackReceiver == null){
            mRecipeBackReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if(intent.getAction().equals(GlobalContants.RECIPE_FINISH)){
                        if(mAdapter!=null ){
                            mRecipePreviews = mRecipePreviewService.findAll();
                            mAdapter.setData(mRecipePreviews);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(GlobalContants.RECIPE_FINISH);
            getActivity().registerReceiver(mRecipeBackReceiver,intentFilter);
        }
    }

    private void showMenuList(){
        mMenuList.setVisibility(View.VISIBLE);
        mNoAllFavorites.setVisibility(View.GONE);
        mNoSearchFavorites.setVisibility(View.GONE);
    }

    private void showNoAllFavorites(){
        mMenuList.setVisibility(View.GONE);
        mNoAllFavorites.setVisibility(View.VISIBLE);
        mNoSearchFavorites.setVisibility(View.GONE);
    }

    private void showNoSearchFavorites(){
        mMenuList.setVisibility(View.GONE);
        mNoAllFavorites.setVisibility(View.GONE);
        mNoSearchFavorites.setVisibility(View.VISIBLE);
    }
}
