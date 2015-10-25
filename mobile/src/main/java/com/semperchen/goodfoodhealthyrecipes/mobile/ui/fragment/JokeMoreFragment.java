package com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.*;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.IntensionData;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.IntensionData.Body.Bean.Detail;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.global.GlobalContants;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.net.IntensionNetWorkManager;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.net.JokeNetWorkManager;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.JokeData;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.utils.AnimationUtils;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.utils.ImagePagerIndicatorUtils;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.activity.MainActivity;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter.*;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget.PullCallback;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget.PullToLoadView;

import java.util.List;

/**
 * Created by 卡你基巴 on 2015/9/29.
 */
public class JokeMoreFragment extends BaseToolbarFragment implements ImageMoreAdapter.OnItemCreate,ImagePagerAdapter.OnImagePagerCallbacks{
    private MainActivity mActivity;
    private Toolbar mToolbar;

    private View mJokeView,mIntensionView,mVideoView,mImageView;

    private RelativeLayout mRlNoNet;
    private FrameLayout mFlContent;
    private RecyclerView mIntensionContent,mJokeContent,mImageContent,mVideoContent;

    private PullToLoadView mJokePullLoad,mIntensionPullLoad,mVideoPullLoad,mImagePullLoad;
    private ViewPager mImagePager;
    private ImagePagerAdapter mPagerAdapter;

    private JokeMoreAdapter mJokeMoreAdapter;
    private IntensionMoreAdapter mIntensionMoreAdapter;
    private ImageMoreAdapter mImageMoreAdapter;
    private VideoMoreAdapter mVideoMoreAdapter;
    private int mType;

    private ListView mPopup;
    private RelativeLayout mRlTop;
    private ImageView mBtnSelect;
    private boolean isOpen = false;

    private int mJokeCuttentPage,mJokeNextPage;
    private int mIntensionCuttentPage,mIntensionNextPage;
    private int mImageCuttentPage,mImageNextPage;
    private int mVideoCuttentPage,mVideoNextPage;

    private Animation mIn,mOut;
    private Handler mHandler;

    public JokeMoreFragment(int viewtype){
        mType = viewtype;
    }

    @Override
    protected void bindData() {
        setupToolbar(mView);

        mRlNoNet = (RelativeLayout) mView.findViewById(R.id.rl_nonet);
        mFlContent = (FrameLayout) mView.findViewById(R.id.fl_content);
        mPopup = (ListView) mView.findViewById(R.id.lv_popup);
        mBtnSelect = (ImageView) mView.findViewById(R.id.btn_select);
        mRlTop = (RelativeLayout) mView.findViewById(R.id.rl_top);

        switch (mType){
            case JokeAdapter.ITEMVIEW_INTENSION:
                setupIntensionViewAndData();
                break;
            case JokeAdapter.ITEMVIEW_IMAGE:
                setupImageViewAndData();
                break;
            case JokeAdapter.ITEMVIEW_JOKE:
                setupJokeViewAndData();
                break;
            case JokeAdapter.ITEMVIEW_VIDEO:
                setupVideoViewAndData();
                break;
        }

        initPopupData();
        initListener();
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_joke_more;
    }

    @Nullable
    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        switch (mType){
            case JokeAdapter.ITEMVIEW_INTENSION:
                //填充数据
                if(mIntensionMoreAdapter != null){
                    if(mIntensionContent.getAdapter() == null) {
                        mIntensionContent.setAdapter(mIntensionMoreAdapter);
                        mRlNoNet.setVisibility(View.GONE);
                    }
                }else{
                    mRlNoNet.setVisibility(View.VISIBLE);
                }
                break;
            case JokeAdapter.ITEMVIEW_IMAGE:
                //填充数据
                if(mImageMoreAdapter != null){
                    if(mImageContent.getAdapter() == null) {
                        mImageContent.setAdapter(mImageMoreAdapter);
                        mRlNoNet.setVisibility(View.GONE);
                    }
                }else{
                    mRlNoNet.setVisibility(View.VISIBLE);
                }
                break;
            case JokeAdapter.ITEMVIEW_JOKE:
                //填充数据
                if(mJokeMoreAdapter != null){
                    if(mJokeContent.getAdapter() == null) {
                        mJokeContent.setAdapter(mJokeMoreAdapter);
                        mRlNoNet.setVisibility(View.GONE);
                    }
                }else{
                    mRlNoNet.setVisibility(View.VISIBLE);
                }
                break;
            case JokeAdapter.ITEMVIEW_VIDEO:
                if(mVideoMoreAdapter != null){
                    if(mVideoContent.getAdapter() == null){
                        mVideoContent.setAdapter(mVideoMoreAdapter);
                        mRlNoNet.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    private void setToolTitleChange(String value){
        ((TextView) mView.findViewById(R.id.tv_tool_title)).setText(value);
    }

    @Override
    protected void setupToolbar(View rootView) {
        super.setupToolbar(rootView);
        if(actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.bg_joke_back);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        switch (mType){
            case JokeAdapter.ITEMVIEW_INTENSION:
                setToolTitleChange(getActivity().getString(R.string.neihanduanzi));
                break;
            case JokeAdapter.ITEMVIEW_IMAGE:
                setToolTitleChange(getActivity().getString(R.string.gaoxiaojiongtu));
                break;
            case JokeAdapter.ITEMVIEW_JOKE:
                setToolTitleChange(getActivity().getString(R.string.meirinxiao));
                break;
            case JokeAdapter.ITEMVIEW_VIDEO:
                setToolTitleChange(getActivity().getString(R.string.gaoxiaoshipin));
                break;
        }
    }

    /**
     * 初始化选择器数据
     */
    private void initPopupData() {
        mPopup.setAdapter(new ArrayAdapter<String>(mActivity, R.layout.joke_more_popup_item, R.id.tv_item, mActivity.getResources().getStringArray(R.array.joke_items)));
    }

    /**
     * 初始化选择监听
     */
    private void initListener() {
        mPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                ((TextView) mView.findViewById(R.id.tv_tool_title)).setText(activity.getResources().getStringArray(R.array.joke_items)[i]);
                view.setSelected(true);
                onSelectColse();
                if (i == 0 && mType == JokeAdapter.ITEMVIEW_INTENSION) {
                    Toast.makeText(mActivity, "这是当前页...", Toast.LENGTH_SHORT).show();
                } else if (i == 1 && mType == JokeAdapter.ITEMVIEW_IMAGE) {
                    Toast.makeText(mActivity, "这是当前页...", Toast.LENGTH_SHORT).show();
                } else if (i == 2 && mType == JokeAdapter.ITEMVIEW_JOKE) {
                    Toast.makeText(mActivity, "这是当前页...", Toast.LENGTH_SHORT).show();
                } else if (i == 3 && mType == JokeAdapter.ITEMVIEW_VIDEO) {
                    Toast.makeText(mActivity, "这是当前页...", Toast.LENGTH_SHORT).show();
                } else {
                    changeContentView(i);
                }
            }
        });

        mRlTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen) {
                    onSelectColse();
                } else {
                    onSelectOpen();
                }
            }
        });
    }

    /**
     * 初始化Intension视图和获取并绑定数据
     */
    private void setupIntensionViewAndData() {
        if(mIntensionView == null) {
            mIntensionView = View.inflate(mActivity, R.layout.fragment_joke_more_intension, null);
            mIntensionView.setTag("intension");
           mIntensionPullLoad = (PullToLoadView) mIntensionView.findViewById(R.id.pulltoLoadview);
           setPullLoadToLoad(mIntensionPullLoad);

           mIntensionContent = mIntensionPullLoad.getRecyclerView();
           setContentTouchListener(mIntensionContent);

           setupRecyclerManager(mIntensionContent);
        }
        startAnimWithInAndout(mFlContent, mIntensionView);

        initIntensionData();
        getIntensionDataFromService(1, GlobalContants.INTENSION_MORE_CREATE_GET);
    }

    /**
     * 初始化Image视图和获取并绑定数据
     */
    private void setupImageViewAndData() {
        if(mImageView == null) {
            mImageView = View.inflate(mActivity, R.layout.fragment_joke_more_image, null);
            mImageView.setTag("image");
            mImagePullLoad = (PullToLoadView) mImageView.findViewById(R.id.pulltoLoadview);
            mImagePager = (ViewPager) mImageView.findViewById(R.id.vp_content);
            setPullLoadToLoad(mImagePullLoad);

            mImageContent = mImagePullLoad.getRecyclerView();
            setContentTouchListener(mImageContent);

            setupRecyclerManager(mImageContent);

        }

        startAnimWithInAndout(mFlContent, mImageView);
        initImageData();
        getImageDateFromService(1, GlobalContants.IMAGE_MORE_CREATE_GET);
    }

    /**
     * 初始化Joke视图和获取并绑定数据
     */
    private void setupJokeViewAndData() {
        if(mJokeView == null) {
            mJokeView = View.inflate(mActivity, R.layout.fragment_joke_more_joke, null);
            mJokeView.setTag("joke");
            mJokePullLoad= (PullToLoadView) mJokeView.findViewById(R.id.pulltoLoadview);
            setPullLoadToLoad(mJokePullLoad);

            mJokeContent = mJokePullLoad.getRecyclerView();
            setContentTouchListener(mJokeContent);

            setupRecyclerManager(mJokeContent);
        }
        startAnimWithInAndout(mFlContent, mJokeView);
        initJokeData();
        //访问网络，获取数据
        getJokeDataFromService(0, GlobalContants.JOKE_MORE_CREATE_GET);

    }

    /**
     * 初始化Video视图和获取并绑定数据
     */
    private void setupVideoViewAndData() {
        if(mVideoView == null) {
            mVideoView = View.inflate(mActivity, R.layout.fragment_joke_more_video, null);
            mVideoView.setTag("video");
            mVideoPullLoad = (PullToLoadView) mVideoView.findViewById(R.id.pulltoLoadview);
            setPullLoadToLoad(mVideoPullLoad);

            mVideoContent = mVideoPullLoad.getRecyclerView();
            setContentTouchListener(mVideoContent);

            setupRecyclerManager(mVideoContent);
        }
        startAnimWithInAndout(mFlContent, mVideoView);
        initVideoData();
        getVideoDataFromService(1, GlobalContants.VIDEO_MORE_CREATE_GET);
    }

    /**
     * 设置RecyclerView布局器
     */
    private void setupRecyclerManager(RecyclerView content) {
        //列数为一列
        final int spanCount = 1;
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(
                spanCount,
                StaggeredGridLayoutManager.VERTICAL);
        content.setLayoutManager(mLayoutManager);
    }

    /**
     * 从网络获取Joke数据
     */
    private void getJokeDataFromService(final int nextPage,String tag) {
        mJokePullLoad.setRefreshEnable(false);
        JokeNetWorkManager.getInstance().sendNetworkRequestForJoke(new Response.Listener<JokeData>() {
            @Override
            public void onResponse(JokeData jokeData) {
                if (mJokeMoreAdapter == null || mJokeMoreAdapter.getItemCount() <= 0) {
                    mJokeMoreAdapter = new JokeMoreAdapter(mActivity, jokeData.detail);
                    buildDataInView(mJokeMoreAdapter);
                    mJokeCuttentPage = 0;
                    mJokeNextPage = 1;
                } else {
                    if (mJokePullLoad.isRefreshing()) {
                        mJokeMoreAdapter.clear();
                        mJokeCuttentPage = 0;
                        mJokeNextPage = 1;
                    }
                    mJokeMoreAdapter.getJokes().addAll(jokeData.detail);
                    mJokeMoreAdapter.notifyDataSetChanged();
                    if (mJokePullLoad.isLoadingMore()) {
                        mJokeCuttentPage = mJokeNextPage;
                        mJokeNextPage = mJokeNextPage + 1;
                    }
                }
                mJokePullLoad.setComplete();
                mJokePullLoad.setRefreshEnable(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mJokePullLoad.setRefreshEnable(true);
            }
        }, 20, nextPage, tag);
    }

    /**
     * 从网络获取Instensino数据
     */
    private void getIntensionDataFromService(final int nextPage,String tag) {
        mIntensionPullLoad.setRefreshEnable(false);
        IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
            @Override
            public void onResponse(IntensionData intensionData) {
                if (mIntensionMoreAdapter == null || mIntensionMoreAdapter.getItemCount() <= 1) {
                    mIntensionMoreAdapter = new IntensionMoreAdapter(mActivity, intensionData.showapi_res_body.pagebean.contentlist,
                            intensionData.showapi_res_body.pagebean.allNum);
                    buildDataInView(mIntensionMoreAdapter);
                    mIntensionCuttentPage = 1;
                    mIntensionNextPage = 2;
                } else {
                    if (mIntensionPullLoad.isRefreshing()) {
                        mIntensionMoreAdapter.clear();
                        mIntensionCuttentPage = 1;
                        mIntensionNextPage = 2;
                    }
                    mIntensionMoreAdapter.addAll(intensionData.showapi_res_body.pagebean.contentlist);
                    mIntensionMoreAdapter.notifyDataSetChanged();
                    if (mIntensionPullLoad.isLoadingMore()) {
                        mIntensionCuttentPage = mIntensionNextPage;
                        mIntensionNextPage = mIntensionNextPage + 1;
                    }
                }
                mIntensionPullLoad.setComplete();
                mIntensionPullLoad.setRefreshEnable(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mIntensionPullLoad.setRefreshEnable(true);
            }
        }, JokeAdapter.ITEMVIEW_INTENSION, nextPage, tag);
    }

    /**
     * 从网络获取Image数据
     */
    private void getImageDateFromService(final int nextPage,String tag){
        mImagePullLoad.setRefreshEnable(false);
        IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
            @Override
            public void onResponse(IntensionData intensionData) {
                if (mImageMoreAdapter == null || mImageMoreAdapter.getItemCount() <= 1) {
                    mImageMoreAdapter = new ImageMoreAdapter(mActivity, intensionData.showapi_res_body.pagebean.contentlist, JokeMoreFragment.this);
                    buildDataInView(mImageMoreAdapter);
                    mImageCuttentPage = 1;
                    mImageNextPage = 2;
                } else {
                    if (mImagePullLoad.isRefreshing()) {
                        mImageMoreAdapter.clear();
                        mImageCuttentPage = 1;
                        mImageNextPage = 2;
                    }
                    mImageMoreAdapter.addAll(intensionData.showapi_res_body.pagebean.contentlist);
                    mImageMoreAdapter.notifyDataSetChanged();
                    mPagerAdapter.notifyDataSetChanged();
                    if (mImagePullLoad.isLoadingMore()) {
                        mImageCuttentPage = mImageNextPage;
                        mImageNextPage = mImageNextPage + 1;
                    }
                }
                mImagePullLoad.setComplete();
                mImagePullLoad.setRefreshEnable(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mImagePullLoad.setRefreshEnable(true);
            }
        }, JokeAdapter.ITEMVIEW_IMAGE, nextPage,tag);
    }

    private void getVideoDataFromService(final int nextPage,String tag) {
        mVideoPullLoad.setRefreshEnable(false);
        IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
            @Override
            public void onResponse(IntensionData intensionData) {
                if(mVideoMoreAdapter == null || mVideoMoreAdapter.getItemCount() <= 0) {
                    mVideoMoreAdapter = new VideoMoreAdapter(mActivity, intensionData.showapi_res_body.pagebean.contentlist);
                    buildDataInView(mVideoMoreAdapter);
                    mVideoCuttentPage = 1;
                    mVideoNextPage = 2;
                }else{
                    if (mVideoPullLoad.isRefreshing()) {
                        mVideoMoreAdapter.clear();
                        mVideoCuttentPage = 1;
                        mVideoNextPage = 2;
                    }
                    mVideoMoreAdapter.getVideos().addAll(intensionData.showapi_res_body.pagebean.contentlist);
                    mVideoMoreAdapter.notifyDataSetChanged();
                    if (mVideoPullLoad.isLoadingMore()) {
                        mVideoCuttentPage = mVideoNextPage;
                        mVideoNextPage = mVideoNextPage + 1;
                    }
                }
                mVideoPullLoad.setComplete();
                mVideoPullLoad.setRefreshEnable(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mVideoPullLoad.setRefreshEnable(true);
            }
        }, JokeAdapter.ITEMVIEW_VIDEO, nextPage,tag);
    }

    /**
     * 绑定数据
     */
    private void buildDataInView(Object adapter) {
        if(mType == JokeAdapter.ITEMVIEW_JOKE){
            mJokeContent.setAdapter((JokeMoreAdapter)adapter);
        }else if(mType == JokeAdapter.ITEMVIEW_INTENSION){
            mIntensionContent.setAdapter((IntensionMoreAdapter)adapter);
        }else if(mType == JokeAdapter.ITEMVIEW_IMAGE){
            mImageContent.setAdapter((ImageMoreAdapter)adapter);
        }else if(mType == JokeAdapter.ITEMVIEW_VIDEO){
           mVideoContent.setAdapter((VideoMoreAdapter)adapter);
        }
        mRlNoNet.setVisibility(View.GONE);
    }

    /**
     * 打开选择列表
     */
    private void onSelectOpen(){
        mPopup.setVisibility(View.VISIBLE);
        AnimationUtils.openAnim(mBtnSelect, AnimationUtils.ANIM_ROTATE, 300);
        AnimationUtils.openAnim(mPopup, AnimationUtils.ANIM_TRANSLATE, 600);
        isOpen = true;
    }

    /**
     * 关闭选择列表
     */
    private void onSelectColse(){
        AnimationUtils.closeAnim(mBtnSelect, AnimationUtils.ANIM_ROTATE, 300);
        AnimationUtils.closeAnim(mPopup, AnimationUtils.ANIM_TRANSLATE, 600);
        isOpen = false;
    }

    /**
     * 替换主页面内容并初始化数据
     * @param position
     */
    private void changeContentView(int position) {
        setToolTitleChange(mActivity.getResources().getStringArray(R.array.joke_items)[position]);
        mRlNoNet.setVisibility(View.VISIBLE);
        switch (position){
            case 0:
                mType = JokeAdapter.ITEMVIEW_INTENSION;
                setupIntensionViewAndData();
                break;
            case 1:
                mType = JokeAdapter.ITEMVIEW_IMAGE;
                setupImageViewAndData();
                break;
            case 2:
                mType = JokeAdapter.ITEMVIEW_JOKE;
                setupJokeViewAndData();
                break;
            case 3:
                mType = JokeAdapter.ITEMVIEW_VIDEO;
                setupVideoViewAndData();
                break;

        }
    }

    /**
     * 设置页面监听关闭选择列表
     * @param recyclerView
     */
    private void setContentTouchListener(RecyclerView recyclerView){
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (isOpen) {
                    onSelectColse();
                }
                return false;
            }
        });
    }

    /**
     * 开启页面进入和退出动画
     * @param parent
     * @param view
     */
    private void startAnimWithInAndout(FrameLayout parent,View view){
        if(parent.getChildAt(0) != null){
            if(parent.getChildAt(0).getTag().equals("image")){
                if(mHandler!=null){
                    mHandler.removeCallbacksAndMessages(null);
                    mHandler = null;
                }
            }
            if(mOut != null) {
                parent.getChildAt(0).startAnimation(mOut);
            }
        }
        parent.removeAllViews();
        parent.addView(view);
        if(mIn != null){
            view.startAnimation(mIn);
        }
    }

    /**
     * 设置上拉和下拉刷新
     */
    private void setPullLoadToLoad(PullToLoadView pullToLoadView) {
        pullToLoadView.setColorSchemeResources(
                android.R.color.holo_green_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);
        pullToLoadView.setPullCallback(new PullLoadMoreListener());
    }

    /**
     * 初始化Intension默认数据
     */
    private void initIntensionData(){
        if(mIntensionMoreAdapter != null) {
            mIntensionMoreAdapter.clear();
        }
        mIntensionCuttentPage = 0;
        mIntensionNextPage = 1;
    }

    /**
     * 初始化Image默认数据
     */
    private void initImageData(){
        if(mImageMoreAdapter != null) {
            mImageMoreAdapter.clear();
        }
        mImageCuttentPage = 0;
        mImageNextPage = 1;
    }

    /**
     * 初始化Joke默认数据
     */
    private void initJokeData() {
        if(mJokeMoreAdapter != null) {
            mJokeMoreAdapter.clear();
        }
        mJokeCuttentPage = -1;
        mJokeNextPage = 0;
    }

    /**
     * 初始化Video默认数据
     */
    private void initVideoData(){
        if(mVideoMoreAdapter != null){
            mVideoMoreAdapter.clear();
        }
        mVideoCuttentPage = 0;
        mVideoNextPage = 1;
    }

    /**
     * 设置主页面切换动画
     * @param in
     * @param out
     */
    public void setChangeViewAnimation(Animation in,Animation out){
        mIn = in;
        mOut = out;
    }

    /**
     * 清除页面切换动画
     */
    public void clearViewAnimation(){
        mIn = null;
        mOut = null;
    }

    /**
     * 笑话里图片点击时回调
     * @param position
     */
    @Override
    public void onImageClick(int position) {
        if(mImagePullLoad!=null && mImagePager!=null){
            if(mPagerAdapter == null) {
                mPagerAdapter = new ImagePagerAdapter(mActivity, mImageMoreAdapter.getImages(),this);
                mImagePager.setAdapter(mPagerAdapter);
            }
            actionBar.hide();
            mImagePullLoad.setVisibility(View.INVISIBLE);
            mImagePager.setVisibility(View.VISIBLE);
            setImagePagerData(position);
        }
    }

    private void setImagePagerData(int position) {
        mImagePager.setCurrentItem(position,false);
    }


    /**
     * 笑话页面viewpager完成时回调
     * @param pager
     * @param indicator
     */
    @Override
    public void onHeaderCreate(final ViewPager pager, RelativeLayout indicator) {
        pager.setAdapter(mImageMoreAdapter.getPagerAdapter());
        if(mHandler == null){
            mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    mHandler.removeCallbacksAndMessages(null);
                    int currentItem = pager.getCurrentItem();
                    if(currentItem<pager.getAdapter().getCount()-1){
                        currentItem++;
                    }else{
                        currentItem=0;
                    }
                    pager.setCurrentItem(currentItem);
                    mHandler.sendEmptyMessageDelayed(0,3000);
                }
            };
        }

        mHandler.sendEmptyMessageDelayed(0,3000);
        ImagePagerIndicatorUtils.setOnPagerChangListener(pager, indicator);
    }

    /**
     * 图片模式点击回调
     */
    @Override
    public void onImageClick() {
        actionBar.show();
        mImagePullLoad.setVisibility(View.VISIBLE);
        mImagePager.setVisibility(View.INVISIBLE);
        mPagerAdapter.destory();
        mPagerAdapter = null;
    }

    /**
     * 刷新和加载监听
     */
    private class PullLoadMoreListener implements PullCallback {

        @Override
        public void onLoadMore() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    switch (mType){
                        case JokeAdapter.ITEMVIEW_INTENSION:
                            getIntensionDataFromService(mIntensionNextPage, GlobalContants.INTENSION_MORE_ONLOADMORE_GET);
                            break;
                        case JokeAdapter.ITEMVIEW_IMAGE:
                            getImageDateFromService(mImageNextPage, GlobalContants.IMAGE_MORE_ONLOADMORE_GET);
                            break;
                        case JokeAdapter.ITEMVIEW_JOKE:
                            getJokeDataFromService(mJokeNextPage, GlobalContants.JOKE_MORE_ONLOADMORE_GET);
                            break;
                        case JokeAdapter.ITEMVIEW_VIDEO:
                            getVideoDataFromService(mVideoNextPage, GlobalContants.VIDEO_MORE_ONLOADMORE_GET);
                            break;
                    }
                }
            },3000);
        }

        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    switch (mType){
                        case JokeAdapter.ITEMVIEW_INTENSION:
                            getIntensionDataFromService(1, GlobalContants.INTENSION_MORE_ONREFRESH_GET);
                            break;
                        case JokeAdapter.ITEMVIEW_IMAGE:
                            getImageDateFromService(1, GlobalContants.IMAGE_MORE_ONREFRESH_GET);
                            break;
                        case JokeAdapter.ITEMVIEW_JOKE:
                            getJokeDataFromService(0, GlobalContants.JOKE_MORE_ONREFRESH_GET);
                            break;
                        case JokeAdapter.ITEMVIEW_VIDEO:
                            getVideoDataFromService(1, GlobalContants.VIDEO_MORE_ONREFRESH_GET);
                            break;
                    }
                }
            },2000);
        }
    }
}
