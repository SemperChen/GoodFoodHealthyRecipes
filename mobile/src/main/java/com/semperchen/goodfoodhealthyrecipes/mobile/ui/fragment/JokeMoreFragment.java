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
import android.view.animation.ScaleAnimation;
import android.widget.*;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.IntensionData;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.net.IntensionNetWorkManager;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.net.JokeNetWorkManager;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.JokeData;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.utils.AnimationUtils;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.utils.ImagePagerIndicatorUtils;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.activity.MainActivity;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter.*;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget.PullCallback;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget.PullToLoadView;

/**
 * Created by 卡你基巴 on 2015/9/29.
 */
public class JokeMoreFragment extends BaseToolbarFragment implements ImageMoreAdapter.OnItemCreate{
    private MainActivity activity;

    private View jokeView,intensionView,videoView,imageView;

    private RelativeLayout rlNoNet;
    private FrameLayout flContent;
    private RecyclerView intensionContent,jokeContent,imageContent,videoContent;

    private PullToLoadView jokePullLoad,intensionPullLoad,videoPullLoad,imagePullLoad;

    private JokeMoreAdapter jokeMoreAdapter;
    private IntensionMoreAdapter intensionMoreAdapter;
    private ImageMoreAdapter imageMoreAdapter;
    private VideoMoreAdapter videoMoreAdapter;
    private int mType;

    private ListView lvPopup;
    private RelativeLayout rlTop;
    private ImageView btnSelect;
    private boolean isOpen = false;

    private int jokeCuttentPage,jokeNextPage;
    private int intensionCuttentPage,intensionNextPage;
    private int imageCuttentPage,imageNextPage;
    private int videoCuttentPage,videoNextPage;

    private Animation mIn,mOut;
    private Handler handler;

    public JokeMoreFragment(int viewtype){
        mType = viewtype;
    }

    @Override
    protected void bindData() {
        setupToolbar(mView);

        rlNoNet = (RelativeLayout) mView.findViewById(R.id.rl_nonet);
        flContent = (FrameLayout) mView.findViewById(R.id.fl_content);
        lvPopup = (ListView) mView.findViewById(R.id.lv_popup);
        btnSelect = (ImageView) mView.findViewById(R.id.btn_select);
        rlTop = (RelativeLayout) mView.findViewById(R.id.rl_top);

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
                if(intensionMoreAdapter != null){
                    if(intensionContent.getAdapter() == null) {
                        intensionContent.setAdapter(intensionMoreAdapter);
                        rlNoNet.setVisibility(View.GONE);
                    }
                }else{
                    rlNoNet.setVisibility(View.VISIBLE);
                }
                break;
            case JokeAdapter.ITEMVIEW_IMAGE:
                //填充数据
                if(imageMoreAdapter != null){
                    if(imageContent.getAdapter() == null) {
                        imageContent.setAdapter(imageMoreAdapter);
                        rlNoNet.setVisibility(View.GONE);
                    }
                }else{
                    rlNoNet.setVisibility(View.VISIBLE);
                }
                break;
            case JokeAdapter.ITEMVIEW_JOKE:
                //填充数据
                if(jokeMoreAdapter != null){
                    if(jokeContent.getAdapter() == null) {
                        jokeContent.setAdapter(jokeMoreAdapter);
                        rlNoNet.setVisibility(View.GONE);
                    }
                }else{
                    rlNoNet.setVisibility(View.VISIBLE);
                }
                break;
            case JokeAdapter.ITEMVIEW_VIDEO:
                if(videoMoreAdapter != null){
                    if(videoContent.getAdapter() == null){
                        videoContent.setAdapter(videoMoreAdapter);
                        rlNoNet.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
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
        lvPopup.setAdapter(new ArrayAdapter<String>(activity, R.layout.joke_more_popup_item, R.id.tv_item, activity.getResources().getStringArray(R.array.joke_items)));
    }

    /**
     * 初始化选择监听
     */
    private void initListener() {
        lvPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                ((TextView) mView.findViewById(R.id.tv_tool_title)).setText(activity.getResources().getStringArray(R.array.joke_items)[i]);
                view.setSelected(true);
                onSelectColse();
                if (i == 0 && mType == JokeAdapter.ITEMVIEW_INTENSION) {
                    Toast.makeText(activity, "这是当前页...", Toast.LENGTH_SHORT).show();
                } else if (i == 1 && mType == JokeAdapter.ITEMVIEW_IMAGE) {
                    Toast.makeText(activity, "这是当前页...", Toast.LENGTH_SHORT).show();
                } else if (i == 2 && mType == JokeAdapter.ITEMVIEW_JOKE) {
                    Toast.makeText(activity, "这是当前页...", Toast.LENGTH_SHORT).show();
                } else if (i == 3 && mType == JokeAdapter.ITEMVIEW_VIDEO) {
                    Toast.makeText(activity, "这是当前页...", Toast.LENGTH_SHORT).show();
                } else {
                    changeContentView(i);
                }
            }
        });

        rlTop.setOnClickListener(new View.OnClickListener() {
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
        if(intensionView == null) {
           intensionView = View.inflate(activity, R.layout.fragment_joke_more_intension, null);
           intensionView.setTag("intension");
           intensionPullLoad = (PullToLoadView) intensionView.findViewById(R.id.pulltoLoadview);
           setPullLoadToLoad(intensionPullLoad);

           intensionContent = intensionPullLoad.getRecyclerView();
           setContentTouchListener(intensionContent);

           setupRecyclerManager(intensionContent);
        }
        startAnimWithInAndout(flContent, intensionView);

        initIntensionData();
        getIntensionDataFromService(1);
    }

    /**
     * 初始化Image视图和获取并绑定数据
     */
    private void setupImageViewAndData() {
        if(imageView == null) {
            imageView = View.inflate(activity, R.layout.fragment_joke_more_image, null);
            imageView.setTag("image");
            imagePullLoad = (PullToLoadView) imageView.findViewById(R.id.pulltoLoadview);
            setPullLoadToLoad(imagePullLoad);

            imageContent = imagePullLoad.getRecyclerView();
            setContentTouchListener(imageContent);

            setupRecyclerManager(imageContent);

        }

        startAnimWithInAndout(flContent, imageView);
        initImageData();
        getImageDateFromService(1);
    }

    /**
     * 初始化Joke视图和获取并绑定数据
     */
    private void setupJokeViewAndData() {
        if(jokeView == null) {
            jokeView = View.inflate(activity, R.layout.fragment_joke_more_joke, null);
            jokeView.setTag("joke");
            jokePullLoad= (PullToLoadView) jokeView.findViewById(R.id.pulltoLoadview);
            setPullLoadToLoad(jokePullLoad);

            jokeContent = jokePullLoad.getRecyclerView();
            setContentTouchListener(jokeContent);

            setupRecyclerManager(jokeContent);
        }
        startAnimWithInAndout(flContent, jokeView);
        initJokeData();
        //访问网络，获取数据
        getJokeDataFromService(0);

    }

    /**
     * 初始化Video视图和获取并绑定数据
     */
    private void setupVideoViewAndData() {
        if(videoView == null) {
            videoView = View.inflate(activity, R.layout.fragment_joke_more_video, null);
            videoView.setTag("video");
            videoPullLoad = (PullToLoadView) videoView.findViewById(R.id.pulltoLoadview);
            setPullLoadToLoad(videoPullLoad);

            videoContent = videoPullLoad.getRecyclerView();
            setContentTouchListener(videoContent);

            setupRecyclerManager(videoContent);
        }
        startAnimWithInAndout(flContent, videoView);
        initVideoData();
        getVideoDataFromService(1);
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
    private void getJokeDataFromService(final int nextPage) {
        jokePullLoad.setRefreshEnable(false);
        JokeNetWorkManager.getInstance().sendNetworkRequestForJoke(new Response.Listener<JokeData>() {
            @Override
            public void onResponse(JokeData jokeData) {
                if (jokeMoreAdapter == null || jokeMoreAdapter.getItemCount() <= 0) {
                    jokeMoreAdapter = new JokeMoreAdapter(activity, jokeData.detail);
                    buildDataInView(jokeMoreAdapter);
                    jokeCuttentPage = 0;
                    jokeNextPage = 1;
                } else {
                    if (jokePullLoad.isRefreshing()) {
                        jokeMoreAdapter.clear();
                        jokeCuttentPage = 0;
                        jokeNextPage = 1;
                    }
                    jokeMoreAdapter.getJokes().addAll(jokeData.detail);
                    jokeMoreAdapter.notifyDataSetChanged();
                    if (jokePullLoad.isLoadingMore()) {
                        jokeCuttentPage = jokeNextPage;
                        jokeNextPage = jokeNextPage + 1;
                    }
                }
                jokePullLoad.setComplete();
                jokePullLoad.setRefreshEnable(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                jokePullLoad.setRefreshEnable(true);
            }
        }, 20, nextPage);
    }

    /**
     * 从网络获取Instensino数据
     */
    private void getIntensionDataFromService(final int nextPage) {
        intensionPullLoad.setRefreshEnable(false);
        IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
            @Override
            public void onResponse(IntensionData intensionData) {
                if(intensionMoreAdapter == null || intensionMoreAdapter.getItemCount() <= 1) {
                    intensionMoreAdapter = new IntensionMoreAdapter(activity, intensionData.showapi_res_body.pagebean.contentlist,
                            intensionData.showapi_res_body.pagebean.allNum);
                    buildDataInView(intensionMoreAdapter);
                    intensionCuttentPage = 1;
                    intensionNextPage = 2;
                }else{
                    if (intensionPullLoad.isRefreshing()) {
                        intensionMoreAdapter.clear();
                        intensionCuttentPage = 1;
                        intensionNextPage = 2;
                    }
                    intensionMoreAdapter.addAll(intensionData.showapi_res_body.pagebean.contentlist);
                    intensionMoreAdapter.notifyDataSetChanged();
                    if (intensionPullLoad.isLoadingMore()) {
                        intensionCuttentPage = intensionNextPage;
                        intensionNextPage = intensionNextPage + 1;
                    }
                }
                intensionPullLoad.setComplete();
                intensionPullLoad.setRefreshEnable(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                intensionPullLoad.setRefreshEnable(true);
            }
        }, JokeAdapter.ITEMVIEW_INTENSION, nextPage);
    }

    /**
     * 从网络获取Image数据
     */
    private void getImageDateFromService(final int nextPage){
        imagePullLoad.setRefreshEnable(false);
        IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
            @Override
            public void onResponse(IntensionData intensionData) {
                if (imageMoreAdapter == null || imageMoreAdapter.getItemCount() <= 1) {
                    imageMoreAdapter = new ImageMoreAdapter(activity, intensionData.showapi_res_body.pagebean.contentlist, JokeMoreFragment.this);
                    buildDataInView(imageMoreAdapter);
                    imageCuttentPage = 1;
                    imageNextPage = 2;
                } else {
                    if (imagePullLoad.isRefreshing()) {
                        imageMoreAdapter.clear();
                        imageCuttentPage = 1;
                        imageNextPage = 2;
                    }
                    imageMoreAdapter.addAll(intensionData.showapi_res_body.pagebean.contentlist);
                    imageMoreAdapter.notifyDataSetChanged();
                    if (imagePullLoad.isLoadingMore()) {
                        imageCuttentPage = imageNextPage;
                        imageNextPage = imageNextPage + 1;
                    }
                }
                imagePullLoad.setComplete();
                imagePullLoad.setRefreshEnable(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imagePullLoad.setRefreshEnable(true);
            }
        }, JokeAdapter.ITEMVIEW_IMAGE, nextPage);
    }

    private void getVideoDataFromService(final int nextPage) {
        videoPullLoad.setRefreshEnable(false);
        IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
            @Override
            public void onResponse(IntensionData intensionData) {
                if(videoMoreAdapter == null || videoMoreAdapter.getItemCount() <= 0) {
                    videoMoreAdapter = new VideoMoreAdapter(activity, intensionData.showapi_res_body.pagebean.contentlist);
                    buildDataInView(videoMoreAdapter);
                    videoCuttentPage = 1;
                    videoNextPage = 2;
                }else{
                    if (videoPullLoad.isRefreshing()) {
                        videoMoreAdapter.clear();
                        videoCuttentPage = 1;
                        videoNextPage = 2;
                    }
                    videoMoreAdapter.getVideos().addAll(intensionData.showapi_res_body.pagebean.contentlist);
                    videoMoreAdapter.notifyDataSetChanged();
                    if (videoPullLoad.isLoadingMore()) {
                        videoCuttentPage = videoNextPage;
                        videoNextPage = videoNextPage + 1;
                    }
                }
                videoPullLoad.setComplete();
                videoPullLoad.setRefreshEnable(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                videoPullLoad.setRefreshEnable(true);
            }
        }, JokeAdapter.ITEMVIEW_VIDEO, nextPage);
    }

    /**
     * 绑定数据
     */
    private void buildDataInView(Object adapter) {
        if(mType == JokeAdapter.ITEMVIEW_JOKE){
            jokeContent.setAdapter((JokeMoreAdapter)adapter);
        }else if(mType == JokeAdapter.ITEMVIEW_INTENSION){
            intensionContent.setAdapter((IntensionMoreAdapter)adapter);
        }else if(mType == JokeAdapter.ITEMVIEW_IMAGE){
            imageContent.setAdapter((ImageMoreAdapter)adapter);
        }else if(mType == JokeAdapter.ITEMVIEW_VIDEO){
            videoContent.setAdapter((VideoMoreAdapter)adapter);
        }
        rlNoNet.setVisibility(View.GONE);
    }

    /**
     * 打开选择列表
     */
    private void onSelectOpen(){
        lvPopup.setVisibility(View.VISIBLE);
        AnimationUtils.openAnim(btnSelect, AnimationUtils.ANIM_ROTATE, 300);
        AnimationUtils.openAnim(lvPopup, AnimationUtils.ANIM_TRANSLATE, 600);
        isOpen = true;
    }

    /**
     * 关闭选择列表
     */
    private void onSelectColse(){
        AnimationUtils.closeAnim(btnSelect, AnimationUtils.ANIM_ROTATE, 300);
        AnimationUtils.closeAnim(lvPopup, AnimationUtils.ANIM_TRANSLATE, 600);
        isOpen = false;
    }

    /**
     * 替换主页面内容并初始化数据
     * @param position
     */
    private void changeContentView(int position) {
        setToolTitleChange(activity.getResources().getStringArray(R.array.joke_items)[position]);
        rlNoNet.setVisibility(View.VISIBLE);
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
                if(handler!=null){
                    handler.removeCallbacksAndMessages(null);
                    handler = null;
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
        if(intensionMoreAdapter != null) {
            intensionMoreAdapter.clear();
        }
        intensionCuttentPage = 0;
        intensionNextPage = 1;
    }

    /**
     * 初始化Image默认数据
     */
    private void initImageData(){
        if(imageMoreAdapter != null) {
            imageMoreAdapter.clear();
        }
        imageCuttentPage = 0;
        imageNextPage = 1;
    }

    /**
     * 初始化Joke默认数据
     */
    private void initJokeData() {
//        jokeMoreAdapter = null;
        if(jokeMoreAdapter != null) {
            jokeMoreAdapter.clear();
        }
        jokeCuttentPage = -1;
        jokeNextPage = 0;
    }

    /**
     * 初始化Video默认数据
     */
    private void initVideoData(){
        if(videoMoreAdapter != null){
            videoMoreAdapter.clear();
        }
        videoCuttentPage = 0;
        videoNextPage = 1;
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
     * 笑话页面viewpager完成时回调
     * @param pager
     * @param indicator
     */
    @Override
    public void onHeaderCreate(final ViewPager pager, RelativeLayout indicator) {
        pager.setAdapter(imageMoreAdapter.getPagerAdapter());
        if(handler == null){
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    handler.removeCallbacksAndMessages(null);
                    int currentItem = pager.getCurrentItem();
                    if(currentItem<pager.getAdapter().getCount()-1){
                        currentItem++;
                    }else{
                        currentItem=0;
                    }
                    pager.setCurrentItem(currentItem);
                    handler.sendEmptyMessageDelayed(0,3000);
                }
            };
        }

        handler.sendEmptyMessageDelayed(0,3000);
        ImagePagerIndicatorUtils.setOnPagerChangListener(pager, indicator);
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
                            getIntensionDataFromService(intensionNextPage);
                            break;
                        case JokeAdapter.ITEMVIEW_IMAGE:
                            getImageDateFromService(imageNextPage);
                            break;
                        case JokeAdapter.ITEMVIEW_JOKE:
                            getJokeDataFromService(jokeNextPage);
                            break;
                        case JokeAdapter.ITEMVIEW_VIDEO:
                            getVideoDataFromService(videoNextPage);
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
                            getIntensionDataFromService(1);
                            break;
                        case JokeAdapter.ITEMVIEW_IMAGE:
                            getImageDateFromService(1);
                            break;
                        case JokeAdapter.ITEMVIEW_JOKE:
                            getJokeDataFromService(0);
                            break;
                        case JokeAdapter.ITEMVIEW_VIDEO:
                            getVideoDataFromService(1);
                            break;
                    }
                }
            },2000);
        }
    }
}
