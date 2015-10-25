package com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.*;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.IntensionData;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.IntensionData.Body.Bean.Detail;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.JokeData;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.JokeData.Joke;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.global.GlobalContants;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.net.IntensionNetWorkManager;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.net.JokeNetWorkManager;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.net.RequestManager;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.repository.PreferenceManager;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.utils.AnimationUtils;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.utils.DensityUtils;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.utils.NetUtils;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.activity.MainActivity;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter.JokeAdapter;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter.SingleMenuAdapter;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget.*;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import java.io.IOException;
import java.util.*;

/**
 * Created by Semper on 2015/9/17.
 */
public class JokeFragment extends BaseToolbarFragment implements JokeAdapter.JokeAdapterCallbacks,SingleMenuAdapter.OnGifListener{
    private MainActivity mActivity;

    private CollectionView mCollectionView;
    private SingleMenuView mSingleMenuView;
    private NoScrollViewPager mViewPager;
    private OneViewPager mOnePager;
    private RelativeLayout mRlGif;
    private GifImageView mImgGif;
    private HorizontalProgressBarWithNumber mPbGifLoading;

    private Button mPreBtn,mNextBtn,mMoreBtn;
    private GifDrawable mGifFromBytes = null;

    private FrameLayout mNotNetView;
    private int mNotNetViewHeight,mNotNetViewWidth;

    private NoClickLinearLayout mLinear;
    private FrameLayout mFrame,mContainer;
    private int mDisplayCols = 0;
    private int mItemCount = 0;
    private boolean isShowHeader = false;
    private String mHeaderLable = "";
    private List<Integer> mLayouList;
    private JokeAdapter mAdapter;

    private boolean isMenuOpen = false;
    private boolean isVideo,isImage;
    private SingleMenuAdapter mVideoSingleMenuAdapter,mImageSingleMenuAdapter;

    private boolean mNetState;
    private BroadcastReceiver mNetReceiver;
    private Gson mGson;

    private boolean isIntensionDataError = true;
    private boolean isImageDataError = true;
    private boolean isJokeDataError = true;
    private boolean isVideoDataError = true;

    private int mJokeId;
    private int mMaxId;

    private List<Detail> mCacheIntensions;
    private int mIntensionAllPages;
    private int mIntensionNextPageInt = 0;
    private int mIntensionNextNumInt = 0;

    private List<Detail> mCacheImages;
    private int mImageAllPages;
    private int mImageNextPageInt = 0;
    private int mImageNextNumInt = 0;

    private List<Detail> mCacheVideos;
    private int mVideoAllPages;
    private int mVideoNextPageInt = 0;
    private int mVideoNextNumInt = 0;

    public static JokeFragment getInstance(){
        JokeFragment jokeFragment = new JokeFragment();
        Bundle extraArguments = new Bundle();
        jokeFragment.setArguments(extraArguments);
        return jokeFragment;
    }

    /**
     * 绑定数据
     */
    @Override
    protected void bindData() {
        setupToolbar(mView);

        mCollectionView = (CollectionView) mView.findViewById(R.id.joke_content_collection);
        mFrame = (FrameLayout) mView.findViewById(R.id.joke_frame);
        mLinear = (NoClickLinearLayout) mView.findViewById(R.id.joke_ll_conent);
        mRlGif = (RelativeLayout) mView.findViewById(R.id.rl_gif);
        mImgGif = (GifImageView) mView.findViewById(R.id.img_gifview);
        mPbGifLoading = (HorizontalProgressBarWithNumber) mView.findViewById(R.id.pb_gifloading);
        mNotNetView = (FrameLayout) mView.findViewById(R.id.fl_notnet);

        mNotNetViewWidth = DensityUtils.getDisplayWidth(getActivity());
        mNotNetViewHeight = DensityUtils.dip2px(getActivity(),48);

        mNetState = NetUtils.isNetworkAvailable(getActivity());
        mGson = new Gson();

        initNetReceiver();
        initListener();
        initSingleMenuView();
        initSingleMenuViewListener();

    }

    /**
     * 获取内容视图布局id
     *
     * @return id
     */
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_joke;
    }

    /**
     *设置toolbar
     *
     * @param rootView 跟布局
     */
    @Override
    protected void setupToolbar(View rootView) {
        super.setupToolbar(rootView);
        getActivity().setTitle(getString(R.string.joke));
    }

    //设置显示Item的模式
    public  void setItemDefalueSetting(int displayCols,boolean isShowHeader,String headerLable,List<Integer> layoutList){
        this.mDisplayCols = displayCols;
        this.isShowHeader = isShowHeader;
        this.mHeaderLable = headerLable;
        this.mItemCount = layoutList.size();
        this.mLayouList = layoutList;
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
        mContainer = (FrameLayout) mActivity.findViewById(R.id.container);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mNetReceiver != null){
            mActivity.unregisterReceiver(mNetReceiver);
        }
    }

    /**
     * 初始化网络监听
     */
    private void initNetReceiver() {
        if(mNetReceiver == null){
            mNetReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                        mNetState = NetUtils.isNetworkAvailable(getActivity());
                        updateHeaderShow(!mNetState);
                    }
                }
            };

            //注册广播
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            getActivity().registerReceiver(mNetReceiver, intentFilter);
        }
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        mImgGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearGif();
            }
        });
        mRlGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearGif();
            }
        });
    }

    /**
     * 是否显示无网络
     * @param isShow
     */
    private void updateHeaderShow(boolean isShow) {
        if(mLinear!=null && mNotNetView!=null){
            if(isShow){
                AnimationUtils.startShowNotNetAnim(mCollectionView,mNotNetView,mNotNetViewWidth,mNotNetViewHeight);
            }else{
                AnimationUtils.closeShowNotNetAnim(mCollectionView,mNotNetView,mNotNetViewWidth,mNotNetViewHeight);
            }
        }
    }

    /**
     * 释放Gif内存
     */
    private void clearGif(){
        if(mGifFromBytes!=null && mGifFromBytes.isRecycled()){
            mGifFromBytes.recycle();
        }
        mImageSingleMenuAdapter.cleanGifNet();
        mImgGif.setBackground(null);
        mRlGif.setVisibility(View.GONE);
        mPbGifLoading.setProgress(0);
    }

    /**
     * 初始化SingleMenu视图
     */
    private void initSingleMenuView() {
        mSingleMenuView = new SingleMenuView(mActivity, mLinear, mFrame);
        mSingleMenuView.setMenuView(R.layout.fragment_joke_singleview, 20, 100, 0);

        mViewPager = (NoScrollViewPager) mSingleMenuView.getMenuView().findViewById(R.id.vp_content);
        mOnePager = (OneViewPager)mSingleMenuView.getMenuView().findViewById(R.id.vp_content_video);
    }

    /**
     * 初始化SingleMenu视图监听
     */
    private void initSingleMenuViewListener() {
        mPreBtn = (Button)(mSingleMenuView.getMenuView().findViewById(R.id.btn_pre));
        mNextBtn = (Button)(mSingleMenuView.getMenuView().findViewById(R.id.btn_next));
        mMoreBtn = (Button)(mSingleMenuView.getMenuView().findViewById(R.id.btn_checkmore));

        ((ImageButton)(mSingleMenuView.getMenuView().findViewById(R.id.btn_close))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSingleMenu();
            }
        });
        mPreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prePager(mPreBtn, mNextBtn);
            }
        });
        mMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkMore(view);
            }
        });
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPager(mNextBtn, mPreBtn);
            }
        });
    }

    /**
     * 关闭singlemenu
     */
    private void closeSingleMenu() {
        mSingleMenuView.closeMenuView();
        isMenuOpen = false;
        if(isVideo) {
            if (mVideoSingleMenuAdapter != null) {
                mVideoSingleMenuAdapter.closeVideo(mOnePager.getCurrentItem());
            }
        }
        if(isImage) {
            if (mImageSingleMenuAdapter != null) {
                clearGif();
            }
        }
    }

    /**
     * 上一页
     */
    private void prePager(Button preBtn,Button nextBtn) {
        nextBtn.setClickable(true);
        nextBtn.setFocusable(true);
        nextBtn.setEnabled(true);

        if (isImage) {
            if (mImageSingleMenuAdapter != null) {
                clearGif();
            }
        }
        if (isVideo) {
            if (mVideoSingleMenuAdapter != null) {
                mVideoSingleMenuAdapter.closeVideo(mOnePager.getCurrentItem());
            }
            if (mOnePager.getCurrentItem() <= 1) {
                Toast.makeText(mActivity, "No Previous Page!", Toast.LENGTH_LONG).show();
                preBtn.setClickable(false);
                preBtn.setFocusable(false);
                preBtn.setEnabled(false);
            }
            mOnePager.setCurrentItem(mOnePager.getCurrentItem() - 1, false);
        } else {
            if (mViewPager.getCurrentItem() <= 1) {
                Toast.makeText(mActivity, "No Previous Page!", Toast.LENGTH_LONG).show();
                preBtn.setClickable(false);
                preBtn.setFocusable(false);
                preBtn.setEnabled(false);
            }
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, false);
        }
    }

    /**
     * 查看更多
     */
    private void checkMore(View view){
        startJokeReplaceAnim((int) view.getTag());
        mSingleMenuView.closeMenuView();
        if(isVideo) {
            if (mVideoSingleMenuAdapter != null) {
                mVideoSingleMenuAdapter.closeVideo(mOnePager.getCurrentItem());
            }
        }
        if(isImage) {
            if (mImageSingleMenuAdapter != null) {
                clearGif();
            }
        }
    }

    /**
     * 下一页
     */
    private void nextPager(Button nextBtn,Button preBtn) {
        preBtn.setClickable(true);
        preBtn.setFocusable(true);
        preBtn.setEnabled(true);

        if(isImage) {
            if (mImageSingleMenuAdapter != null) {
                clearGif();
            }
        }
        if(isVideo){
            if (mVideoSingleMenuAdapter != null) {
                mVideoSingleMenuAdapter.closeVideo(mOnePager.getCurrentItem());
            }
            if (mOnePager.getCurrentItem() >= mOnePager.getAdapter().getCount() - 2) {
                Toast.makeText(mActivity, "No Next Page,Put on the move button!", Toast.LENGTH_LONG).show();
                nextBtn.setClickable(false);
                nextBtn.setFocusable(false);
                nextBtn.setEnabled(false);
            }
            mOnePager.setCurrentItem(mOnePager.getCurrentItem() + 1, false);
        }else{
            if (mViewPager.getCurrentItem() >= mViewPager.getAdapter().getCount() - 2) {
                Toast.makeText(mActivity, "No Next Page,Put on the move button!", Toast.LENGTH_LONG).show();
                nextBtn.setClickable(false);
                nextBtn.setFocusable(false);
                nextBtn.setEnabled(false);
            }
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, false);
        }
    }

    /**
     * 获取笑话数据
     */
    private void buildDataToJoke(final TextView tvValue, final ImageView imgValue, final ProgressBar pbLoading) {
        if(mNetState) {
            JokeNetWorkManager.getInstance().sendNetworkRequestForJoke(new Response.Listener<JokeData>() {
                @Override
                public void onResponse(JokeData jokeData) {
                    //获取网络最新JokeId与本地最新JokeId对比
                    int old_id = PreferenceManager.getInstance().getInt("joke_max_number", 0);
                    int new_id = Integer.parseInt(jokeData.detail.get(0).id);
                    mMaxId = mJokeId = new_id > old_id ? new_id : old_id;
                    PreferenceManager.getInstance().putInt("joke_max_number", mMaxId);
                    PreferenceManager.getInstance().putString(GlobalContants.JOKE_PREFERENCE_FIRST, mGson.toJson(jokeData));
                    setJokeData(tvValue, imgValue, pbLoading, jokeData.detail.get(0).content, false);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    isJokeDataError = true;
                }
            }, 1, 0, GlobalContants.JOKE_CREATE_GET);
        }else{
            mJokeId = PreferenceManager.getInstance().getInt("joke_max_number", 0);
            String json = PreferenceManager.getInstance().getString(GlobalContants.JOKE_PREFERENCE_FIRST);
            if("".equals(json)){
                setJokeData(tvValue,imgValue,pbLoading,"没有网络...",true);
            }else{
                JokeData jokeData = mGson.fromJson(json,JokeData.class);
                setJokeData(tvValue, imgValue, pbLoading, jokeData.detail.get(0).content, false);
            }
        }
    }

    /**
     * 绑定笑话数据
     * @param tvValue
     * @param imgValue
     * @param pbLoading
     * @param value
     * @param isError
     */
    private void setJokeData(TextView tvValue, ImageView imgValue, ProgressBar pbLoading, String value, boolean isError){
        tvValue.setText(value);
        isJokeDataError = isError;
        tvValue.setVisibility(View.VISIBLE);
        imgValue.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
    }

    /**
     * 获取内涵段子数据
     * @param tvValue
     * @param imgValue
     * @param pbLoading
     */
    private void buildDataToIntension(final TextView tvValue, final ImageView imgValue, final ProgressBar pbLoading){
        if(mNetState) {
            IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
                @Override
                public void onResponse(IntensionData intensionData) {
                    if (!intensionData.showapi_res_error.equals("")) {
                        setIntensionData(tvValue, imgValue, pbLoading, intensionData.showapi_res_error, true);
                    } else {
                        PreferenceManager.getInstance().putString(GlobalContants.INTENSION_PREFERENCE_FIRST, mGson.toJson(intensionData));
                        mCacheIntensions = intensionData.showapi_res_body.pagebean.contentlist;
                        mIntensionAllPages = Integer.parseInt(intensionData.showapi_res_body.pagebean.allPages);
                        setIntensionData(tvValue, imgValue, pbLoading, intensionData.showapi_res_body.pagebean.contentlist.get(0).text.trim(), false);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    isIntensionDataError = true;
                }
            }, JokeAdapter.ITEMVIEW_INTENSION, 1, GlobalContants.INTENSION_CREATE_GET);
        }else{
            String json = PreferenceManager.getInstance().getString(GlobalContants.INTENSION_PREFERENCE_FIRST);
            if("".equals(json)){
                setIntensionData(tvValue,imgValue,pbLoading,"没有网络...",true);
            }else{
                IntensionData intensionData = mGson.fromJson(json,IntensionData.class);
                setIntensionData(tvValue, imgValue, pbLoading, intensionData.showapi_res_body.pagebean.contentlist.get(0).text.trim(), false);
            }
        }
    }

    /**
     * 绑定内涵段子数据
     * @param tvValue
     * @param imgValue
     * @param pbLoading
     * @param value
     * @param isError
     */
    private void setIntensionData(TextView tvValue, ImageView imgValue, ProgressBar pbLoading,String value,boolean isError){
        tvValue.setText(value);
        isIntensionDataError = isError;
        tvValue.setVisibility(View.VISIBLE);
        imgValue.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
    }

    /**
     * 获取囧图数据并绑定
     * @param tvValue
     * @param imgValue
     * @param pbLoading
     */
    private void buildDataToImage(final TextView tvValue, final ImageView imgValue, final ProgressBar pbLoading){
        if(mNetState) {
            IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
                @Override
                public void onResponse(IntensionData intensionData) {
                    if (!intensionData.showapi_res_error.equals("")) {
                        setImageData(tvValue, imgValue, pbLoading, intensionData.showapi_res_error, true, false);
                    } else {
                        PreferenceManager.getInstance().putString(GlobalContants.IMAGE_PREFERENCE_FIRST, mGson.toJson(intensionData));
                        mCacheImages = intensionData.showapi_res_body.pagebean.contentlist;
                        mImageAllPages = Integer.parseInt(intensionData.showapi_res_body.pagebean.allPages);
                        setImageData(tvValue, imgValue, pbLoading, intensionData.showapi_res_body.pagebean.contentlist.get(0).image0, false, true);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    isImageDataError = true;
                }
            }, JokeAdapter.ITEMVIEW_IMAGE, 1, GlobalContants.IMAGE_CREATE_GET);
        }else{
            String json = PreferenceManager.getInstance().getString(GlobalContants.IMAGE_PREFERENCE_FIRST);
            if("".equals(json)){
                setImageData(tvValue, imgValue, pbLoading, "没有网络...", true, false);
            }else{
                IntensionData intensionData = mGson.fromJson(json,IntensionData.class);
                setImageData(tvValue,imgValue,pbLoading,intensionData.showapi_res_body.pagebean.contentlist.get(0).image0,false,true);
            }
        }
    }

    /**
     * 绑定囧图数据
     * @param tvValue
     * @param imgValue
     * @param pbLoading
     * @param value
     * @param isError
     * @param isShowImage
     */
    private void setImageData(TextView tvValue, ImageView imgValue, ProgressBar pbLoading,String value,boolean isError, boolean isShowImage){
        if(!isShowImage){
            tvValue.setText(value);
            tvValue.setVisibility(View.VISIBLE);
            imgValue.setVisibility(View.GONE);
        }else{
            ImageLoader.getInstance().displayImage(value, imgValue);
            tvValue.setVisibility(View.GONE);
            imgValue.setVisibility(View.VISIBLE);
        }
        isImageDataError = isError;
        pbLoading.setVisibility(View.GONE);
    }

    /**
     * 获取搞笑视频数据
     * @param tvValue
     * @param imgValue
     * @param pbLoading
     */
    private void buildDataToVideo(final TextView tvValue, final ImageView imgValue, final ProgressBar pbLoading){
        if(mNetState) {
            IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
                @Override
                public void onResponse(IntensionData intensionData) {
                    if (!intensionData.showapi_res_error.equals("")) {
                        setVideoData(tvValue, imgValue, pbLoading, intensionData.showapi_res_error, true, false);
                    } else {
                        PreferenceManager.getInstance().putString(GlobalContants.VIDEO_PREFERENCE_FIRST, mGson.toJson(intensionData));
                        mCacheVideos = intensionData.showapi_res_body.pagebean.contentlist;
                        mVideoAllPages = Integer.parseInt(intensionData.showapi_res_body.pagebean.allPages);
                        setVideoData(tvValue, imgValue, pbLoading, intensionData.showapi_res_body.pagebean.contentlist.get(0).image3, false, true);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    isVideoDataError = true;
                }
            }, JokeAdapter.ITEMVIEW_VIDEO, 1, GlobalContants.VIDEO_CREATE_GET);
        }else{
            String json = PreferenceManager.getInstance().getString(GlobalContants.VIDEO_PREFERENCE_FIRST);
            if("".equals(json)){
                setVideoData(tvValue, imgValue, pbLoading, "没有网络...", true, false);
            }else{
                IntensionData intensionData = mGson.fromJson(json,IntensionData.class);
                setVideoData(tvValue,imgValue,pbLoading,intensionData.showapi_res_body.pagebean.contentlist.get(0).image3,false,true);
            }
        }
    }

    /**
     * 绑定搞笑视频数据
     * @param tvValue
     * @param imgValue
     * @param pbLoading
     * @param value
     * @param isError
     * @param isShowVideo
     */
    private void setVideoData(TextView tvValue, ImageView imgValue, ProgressBar pbLoading,String value,boolean isError, boolean isShowVideo){
        if(!isShowVideo){
            tvValue.setText(value);
            tvValue.setVisibility(View.VISIBLE);
            imgValue.setVisibility(View.GONE);
        }else{
            ImageLoader.getInstance().displayImage(value, imgValue);
            tvValue.setVisibility(View.GONE);
            imgValue.setVisibility(View.VISIBLE);
        }
        isVideoDataError = isError;
        pbLoading.setVisibility(View.GONE);
    }

    /**
     * 填充CollectionView里的数据
     */
    private void initAdapter(){
        mAdapter = buildAdapter();
        mCollectionView.setCollctionAdapter(mAdapter);
        mCollectionView.updateInventory(mAdapter.getInventory());
//        mAdapter.getHeaderView().setVisibility(View.GONE);
    }

    /**
     * 构建Joke的适配器,即每个Item内容
     * @return
     */
    protected JokeAdapter buildAdapter(){
        return new JokeAdapter(getActivity(),buildMenuList(),mDisplayCols,mItemCount,isShowHeader,mHeaderLable,mLayouList,this);
    }

    /**
     * 打开SingleMenu并访问网络
     * @param view
     */
    private void openSingleMenuViewAndNetWork(final View view) {
        switch ((int)view.getTag(R.id.tag_second)){
            case JokeAdapter.ITEMVIEW_INTENSION:
                if(isIntensionDataError){
                    AnimationUtils.setShakeToView(view,3000,AnimationUtils.MIDDLE_SHAKE);
                    Toast.makeText(mActivity,"未知错误,请长按刷新!",Toast.LENGTH_SHORT).show();
                }else {
                    openMenuSet(view,false,false);
                    //判断是否有网络和缓存数据
                    if (!mNetState) {
                        if(mCacheIntensions!=null) {
                            setIntensionOpenData(mCacheIntensions, (int) view.getTag(R.id.tag_second), mIntensionNextNumInt);
                        }else{
                            String json = PreferenceManager.getInstance().getString(GlobalContants.INTENSION_PREFERENCE_FIRST);
                            IntensionData intensionData = mGson.fromJson(json, IntensionData.class);
                            setIntensionOpenData(intensionData.showapi_res_body.pagebean.contentlist,
                                    (int) view.getTag(R.id.tag_second), mIntensionNextNumInt);
                        }
                    } else {
                        IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
                            @Override
                            public void onResponse(IntensionData intensionData) {
                                if (!intensionData.showapi_res_error.equals("")) {
                                    mViewPager.setAdapter(new SingleMenuAdapter(mActivity, null, (int) view.getTag(R.id.tag_second),null));
                                } else {
                                    setIntensionOpenData(intensionData.showapi_res_body.pagebean.contentlist,
                                            (int)view.getTag(R.id.tag_second),mIntensionNextNumInt);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }, JokeAdapter.ITEMVIEW_INTENSION, mIntensionNextPageInt==0?1:mIntensionNextPageInt,GlobalContants.INTENSION_OPEN_GET);
                    }
                }
                break;
            case JokeAdapter.ITEMVIEW_IMAGE:
                if(isImageDataError){
                    AnimationUtils.setShakeToView(view,3000,AnimationUtils.MIDDLE_SHAKE);
                    Toast.makeText(mActivity,"未知错误,请长按刷新!",Toast.LENGTH_SHORT).show();
                }else{
                    openMenuSet(view,false,true);
                    if(!mNetState){
                        if(mCacheImages!=null) {
                            setImageOpenData(mCacheImages, (int) view.getTag(R.id.tag_second), mImageNextNumInt);
                        }else{
                            String json = PreferenceManager.getInstance().getString(GlobalContants.IMAGE_PREFERENCE_FIRST);
                            IntensionData intensionData = mGson.fromJson(json, IntensionData.class);
                            setImageOpenData(intensionData.showapi_res_body.pagebean.contentlist,
                                    (int) view.getTag(R.id.tag_second), mImageNextNumInt);
                        }
                    }else{
                        IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
                            @Override
                            public void onResponse(IntensionData intensionData) {
                                if (!intensionData.showapi_res_error.equals("")) {
                                    mViewPager.setAdapter(new SingleMenuAdapter(mActivity, null, (int) view.getTag(R.id.tag_second),JokeFragment.this));
                                } else {
                                    setImageOpenData(intensionData.showapi_res_body.pagebean.contentlist,
                                            (int) view.getTag(R.id.tag_second), mImageNextNumInt);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }, JokeAdapter.ITEMVIEW_IMAGE, mImageNextPageInt==0?1:mImageNextPageInt,GlobalContants.IMAGE_OPEN_GET);
                    }
                }
                break;
            case JokeAdapter.ITEMVIEW_JOKE:
                if(isJokeDataError){
                    AnimationUtils.setShakeToView(view,3000,AnimationUtils.MIDDLE_SHAKE);
                    Toast.makeText(mActivity,"未知错误,请长按刷新!",Toast.LENGTH_SHORT).show();
                }else {
                    openMenuSet(view,false,false);
                    mMoreBtn.setClickable(mNetState);
                    mMoreBtn.setFocusable(mNetState);
                    mMoreBtn.setEnabled(mNetState);
                    if(!mNetState) {
                        String json = PreferenceManager.getInstance().getString(GlobalContants.JOKE_PREFERENCE_FIRST);
                        JokeData jokeData = mGson.fromJson(json, JokeData.class);
                        removeMoreItem(jokeData);
                        List<Object> list = new ArrayList<Object>(jokeData.detail);
                        mViewPager.setAdapter(new SingleMenuAdapter(mActivity, list, (int) view.getTag(R.id.tag_second), null));
                    }else{
                        JokeNetWorkManager.getInstance().sendNetworkRequestForJoke(new Response.Listener<JokeData>() {
                            @Override
                            public void onResponse(JokeData jokeData) {
                                removeMoreItem(jokeData);
                                List<Object> list = new ArrayList<Object>(jokeData.detail);
                                mViewPager.setAdapter(new SingleMenuAdapter(mActivity, list, (int) view.getTag(R.id.tag_second), null));
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        }, 20, (mMaxId - mJokeId) / 20, GlobalContants.JOKE_OPEN_GET);
                    }
                }
                break;
            case JokeAdapter.ITEMVIEW_VIDEO:
                if(isVideoDataError){
                    AnimationUtils.setShakeToView(view,3000,AnimationUtils.MIDDLE_SHAKE);
                    Toast.makeText(mActivity,"未知错误,请长按刷新!",Toast.LENGTH_SHORT).show();
                }else{
                    openMenuSet(view,true,false);
                    if(!mNetState){
                        if(mCacheVideos!=null) {
                            setVideoOpenData(mCacheVideos,(int) view.getTag(R.id.tag_second),mVideoNextNumInt);
                        }else{
                            String json = PreferenceManager.getInstance().getString(GlobalContants.VIDEO_PREFERENCE_FIRST);
                            IntensionData intensionData = mGson.fromJson(json, IntensionData.class);
                            setVideoOpenData(intensionData.showapi_res_body.pagebean.contentlist,
                                    (int) view.getTag(R.id.tag_second), mVideoNextNumInt);
                        }
                    }else{
                        IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
                            @Override
                            public void onResponse(IntensionData intensionData) {
                                if (!intensionData.showapi_res_error.equals("")) {
                                    mOnePager.setAdapter(new SingleMenuAdapter(mActivity, null, (int) view.getTag(R.id.tag_second), JokeFragment.this));
                                } else {
                                    setVideoOpenData(intensionData.showapi_res_body.pagebean.contentlist,
                                            (int) view.getTag(R.id.tag_second), mVideoNextNumInt);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }, JokeAdapter.ITEMVIEW_VIDEO, mVideoNextPageInt == 0 ? 1 : mVideoNextPageInt, GlobalContants.VIDEO_OPEN_GET);
                    }
                }
                break;
        }
    }

    private void setIntensionOpenData(List<Detail> details,int type,int nextNumInt){
        List<Object> list = new ArrayList<Object>(details);
        mViewPager.setAdapter(new SingleMenuAdapter(mActivity, list, type, null));
        mViewPager.setCurrentItem(nextNumInt);
        mMoreBtn.setClickable(mNetState);
        mMoreBtn.setFocusable(mNetState);
        mMoreBtn.setEnabled(mNetState);
    }

    private void setImageOpenData(List<Detail> details,int type,int nextNumInt){
        List<Object> list = new ArrayList<Object>(details);
        mImageSingleMenuAdapter = new SingleMenuAdapter(mActivity, list, type, this);
        mViewPager.setAdapter(mImageSingleMenuAdapter);
        mViewPager.setCurrentItem(nextNumInt);
        mMoreBtn.setClickable(mNetState);
        mMoreBtn.setFocusable(mNetState);
        mMoreBtn.setEnabled(mNetState);
    }

    private void setVideoOpenData(List<Detail> details,int type,int nextNumInt){
        List<Object> list = new ArrayList<Object>(details);
        mVideoSingleMenuAdapter = new SingleMenuAdapter(mActivity, list, type, null);
        mOnePager.setAdapter(mVideoSingleMenuAdapter);
        mOnePager.setCurrentItem(mVideoNextNumInt);
        mMoreBtn.setClickable(mNetState);
        mMoreBtn.setFocusable(mNetState);
        mMoreBtn.setEnabled(mNetState);
    }

    /**
     * 打开SingleMenu
     * @param view
     * @param isOnePager
     */
    private void openMenuSet(View view,boolean isOnePager,boolean isGifImage){
        mSingleMenuView.openMenuView(view);
        if(isOnePager){
            mViewPager.setVisibility(View.GONE);
            mOnePager.setVisibility(View.VISIBLE);
        }else {
            mViewPager.setVisibility(View.VISIBLE);
            mOnePager.setVisibility(View.GONE);
        }
        isVideo = isOnePager;
        isImage = isGifImage;
        isMenuOpen = true;
    }

    /**
     * 删除多余的数据
     * @param jokeData
     */
    private void removeMoreItem(JokeData jokeData) {
        List<Joke> removeList = new ArrayList<Joke>();

        for(int i=0;i<jokeData.detail.size();i++){
            if(mJokeId == Integer.parseInt(jokeData.detail.get(i).xhid)){
                for(Joke joke:removeList){
                    jokeData.detail.remove(joke);
                }
                removeList.clear();
                return ;
            }
            removeList.add(jokeData.detail.get(i));

        }
    }

    /**
     * 开始刷新图片的动画和获取网络数据
     * @param description
     * @param itemView
     */
    private void getDataFromService(final View description, final View itemView,final TextView tvValue,final ImageView imgValue) {
        if(((int)itemView.getTag(R.id.tag_second)) == JokeAdapter.ITEMVIEW_JOKE) {
            if(isJokeDataError || mNetState == false){
                AnimationUtils.setShakeToView(itemView,3000,AnimationUtils.MIDDLE_SHAKE);
                Toast.makeText(mActivity,"未知错误,请长按刷新!",Toast.LENGTH_SHORT).show();
            }else {
                isJokeDataError = true;
                AnimationUtils.RotateAnimation(description, 0, 360, 500, -1, new LinearInterpolator());
                int old_id = PreferenceManager.getInstance().getInt("joke_max_number", 0);
                mJokeId = (new Random()).nextInt(old_id);

                JokeNetWorkManager.getInstance().sendNetworkRequestForJoke(new Response.Listener<JokeData>() {
                    @Override
                    public void onResponse(JokeData jokeData) {
                        isJokeDataError = false;
                        successDataAndRefresh(description, itemView, tvValue, imgValue, jokeData.detail.get(0).content);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        isJokeDataError = true;
                    }
                }, 1, (mMaxId - mJokeId),GlobalContants.JOKE_REFRESH_GET);
            }
        }else if(((int)itemView.getTag(R.id.tag_second)) == JokeAdapter.ITEMVIEW_INTENSION){
            if(isIntensionDataError || mNetState == false || mIntensionAllPages == 0){
                AnimationUtils.setShakeToView(itemView,3000,AnimationUtils.MIDDLE_SHAKE);
                Toast.makeText(mActivity,"未知错误,请长按刷新!",Toast.LENGTH_SHORT).show();
            }else {
                isIntensionDataError = true;
                AnimationUtils.RotateAnimation(description, 0, 360, 500, -1, new LinearInterpolator());
                mIntensionNextPageInt = (new Random()).nextInt(mIntensionAllPages) + 1;
                IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
                    @Override
                    public void onResponse(IntensionData intensionData) {
                        if (!intensionData.showapi_res_error.equals("")) {
                            successDataAndRefresh(description, itemView, tvValue, imgValue, intensionData.showapi_res_error);
                            isIntensionDataError = true;
                        } else {
                            mIntensionNextNumInt = (new Random()).nextInt(20);
                            isIntensionDataError = false;
                            mCacheIntensions = intensionData.showapi_res_body.pagebean.contentlist;
                            successDataAndRefresh(description, itemView, tvValue, imgValue, intensionData.showapi_res_body.pagebean.contentlist.get(mIntensionNextNumInt).text);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isIntensionDataError = true;
                    }
                }, JokeAdapter.ITEMVIEW_INTENSION, mIntensionNextPageInt,GlobalContants.INTENSION_REFRESH_GET);
            }
        }else if(((int)itemView.getTag(R.id.tag_second) == JokeAdapter.ITEMVIEW_IMAGE)){
            if(isImageDataError || mNetState == false || mImageAllPages == 0){
                AnimationUtils.setShakeToView(itemView,3000,AnimationUtils.MIDDLE_SHAKE);
                Toast.makeText(mActivity,"未知错误,请长按刷新!",Toast.LENGTH_SHORT).show();
            }else {
                isImageDataError = true;
                AnimationUtils.RotateAnimation(description, 0, 360, 500, -1, new LinearInterpolator());
                mImageNextPageInt = (new Random()).nextInt(mImageAllPages) + 1;
                IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
                    @Override
                    public void onResponse(IntensionData intensionData) {
                        if (!intensionData.showapi_res_error.equals("")) {
                            successDataAndRefresh(description, itemView, tvValue, imgValue, intensionData.showapi_res_error);
                            isImageDataError = true;
                        } else {
                            mImageNextNumInt = (new Random()).nextInt(20);
                            isImageDataError = false;
                            mCacheImages = intensionData.showapi_res_body.pagebean.contentlist;
                            successDataAndRefresh(description, itemView, tvValue, imgValue, intensionData.showapi_res_body.pagebean.contentlist.get(mImageNextNumInt).image0);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isImageDataError = true;
                    }
                }, JokeAdapter.ITEMVIEW_IMAGE, mImageNextPageInt,GlobalContants.IMAGE_REFRESH_GET);
            }
        }else if(((int)itemView.getTag(R.id.tag_second) == JokeAdapter.ITEMVIEW_VIDEO)){
            if(isVideoDataError || mNetState == false || mVideoAllPages == 0){
                AnimationUtils.setShakeToView(itemView,3000,AnimationUtils.MIDDLE_SHAKE);
                Toast.makeText(mActivity,"未知错误,请长按刷新!",Toast.LENGTH_SHORT).show();
            }else{
                isVideoDataError = true;
                AnimationUtils.RotateAnimation(description, 0, 360, 500, -1, new LinearInterpolator());
                mVideoNextPageInt = (new Random()).nextInt(mVideoAllPages) + 1;
                IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
                    @Override
                    public void onResponse(IntensionData intensionData) {
                        if (!intensionData.showapi_res_error.equals("")) {
                            successDataAndRefresh(description, itemView, tvValue, imgValue, intensionData.showapi_res_error);
                            isImageDataError = true;
                        }else{
                            mVideoNextNumInt = (new Random()).nextInt(20);
                            isVideoDataError = false;
                            mCacheVideos = intensionData.showapi_res_body.pagebean.contentlist;
                            successDataAndRefresh(description, itemView, tvValue, imgValue, intensionData.showapi_res_body.pagebean.contentlist.get(mImageNextNumInt).image3);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isVideoDataError = true;
                    }
                },JokeAdapter.ITEMVIEW_VIDEO,mVideoNextPageInt,GlobalContants.VIDEO_REFRESH_GET);
            }
        } else {
            return;
//            AnimationUtils.RotateAnimation(imgView, 0, 360, 500, -1, new LinearInterpolator());
            //模拟从网上获取资源-->未实现
//            successDataAndRefresh(imgView, itemView, tvValue, imgValue, null);
        }
    }

    /**
     * 成功获取数据并设置数据，开启设置数据动画，关闭开始获取数据动画
     * @param description
     * @param itemView
     * @param tvValue
     * @param imgValue
     * @param content
     */
    private void successDataAndRefresh(final View description,final View itemView,final TextView tvValue,final ImageView imgValue, final String content){
            AnimationUtils.ObjectAnimation(itemView,"rotationY", 0, 360,500, new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (((int) itemView.getTag(R.id.tag_second)) == JokeAdapter.ITEMVIEW_JOKE || ((int) itemView.getTag(R.id.tag_second)) == JokeAdapter.ITEMVIEW_INTENSION) {
                        tvValue.setText(content);
                        description.clearAnimation();
                    }else if(((int)itemView.getTag(R.id.tag_second)) == JokeAdapter.ITEMVIEW_IMAGE || ((int) itemView.getTag(R.id.tag_second)) == JokeAdapter.ITEMVIEW_VIDEO){
                        ImageLoader.getInstance().displayImage(content, imgValue,new SimpleImageLoadingListener(){
                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                if(loadedImage!=null){
                                    description.clearAnimation();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(mActivity,"该方法还没有实现...",Toast.LENGTH_SHORT).show();
                        description.clearAnimation();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
    }

    /**
     * 开始刷新动画并每个Item重新刷新数据
     * @param parent
     */
    private void statAnimAndRefreshView(final View parent, final TextView tv, final ImageView img, ImageButton description, final ProgressBar pb) {
        if(mNetState) {
            Toast.makeText(mActivity, "重新刷新", Toast.LENGTH_SHORT).show();
            description.clearAnimation();
            AnimationUtils.ObjectAnimation(parent, "rotationY", 0, 360, 500, new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    switch ((int) parent.getTag(R.id.tag_second)) {
                        case JokeAdapter.ITEMVIEW_INTENSION:
                            RequestManager.getRequestQueue().cancelAll(GlobalContants.INTENSION_CREATE_GET);
                            RequestManager.getRequestQueue().cancelAll(GlobalContants.INTENSION_REFRESH_GET);
                            buildDataToIntension(tv, img, pb);
                            break;
                        case JokeAdapter.ITEMVIEW_IMAGE:
                            RequestManager.getRequestQueue().cancelAll(GlobalContants.IMAGE_CREATE_GET);
                            RequestManager.getRequestQueue().cancelAll(GlobalContants.IMAGE_REFRESH_GET);
                            buildDataToImage(tv, img, pb);
                            break;
                        case JokeAdapter.ITEMVIEW_JOKE:
                            RequestManager.getRequestQueue().cancelAll(GlobalContants.JOKE_CREATE_GET);
                            RequestManager.getRequestQueue().cancelAll(GlobalContants.JOKE_REFRESH_GET);
                            buildDataToJoke(tv, img, pb);
                            break;
                        case JokeAdapter.ITEMVIEW_VIDEO:
                            RequestManager.getRequestQueue().cancelAll(GlobalContants.VIDEO_CREATE_GET);
                            RequestManager.getRequestQueue().cancelAll(GlobalContants.VIDEO_REFRESH_GET);
                            buildDataToVideo(tv, img, pb);
                            break;
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }else{
            Toast.makeText(mActivity,"没有网络,不能刷新..",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 替换Joke页面并开启动画
     * @param viewtype
     */
    private void startJokeReplaceAnim(final int viewtype){
        ScaleAnimation mainAnim = new ScaleAnimation(1.0f,0,1.0f,0, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        mainAnim.setDuration(500);
        mainAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mActivity.setupJokeMoreContent(viewtype);
                ObjectAnimator mainAnimX = ObjectAnimator.ofFloat(mContainer, "scaleX", 1.5f, 1.0f);
                ObjectAnimator mainAnimY = ObjectAnimator.ofFloat(mContainer, "scaleY", 1.5f, 1.0f);
                AnimatorSet set = new AnimatorSet();
                set.setDuration(300);
                set.playTogether(mainAnimX, mainAnimY);
                set.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mContainer.startAnimation(mainAnim);
    }

    /**
     * 打开SingleMenu回调方法
     * @param view
     */
    @Override
    public void onOpenSingleView(View view) {
        this.openSingleMenuViewAndNetWork(view);
    }

    /**
     * 点击刷新回调方法
     * @param description
     * @param parent
     * @param tv
     * @param img
     */
    @Override
    public void onRefreshDataFromService(View description, View parent, TextView tv, ImageView img) {
        this.getDataFromService(description, parent, tv, img);
    }

    /**
     * 长按刷新回调方法
     * @param parent
     */
    @Override
    public void onItemLongClick(View parent, TextView tv, ImageView img, ImageButton description, ProgressBar pb) {
        this.statAnimAndRefreshView(parent, tv, img, description,pb);
    }

    /**
     * 构建好视图请求数据回调方法
     * @param parent
     * @param tv
     * @param img
     * @param pb
     */
    @Override
    public void onItemGetData(View parent, TextView tv, ImageView img, ProgressBar pb) {
        switch ((int)parent.getTag(R.id.tag_second)) {
            case JokeAdapter.ITEMVIEW_INTENSION:
                buildDataToIntension(tv,img,pb);
                break;
            case JokeAdapter.ITEMVIEW_IMAGE:
                buildDataToImage(tv,img,pb);
                break;
            case JokeAdapter.ITEMVIEW_JOKE:
                buildDataToJoke(tv,img,pb);
                break;
            case JokeAdapter.ITEMVIEW_VIDEO:
                buildDataToVideo(tv, img, pb);
                break;
        }
    }

    /**
     * 图片被点击,gif显示之前
     */
    @Override
    public void onGifVisibility() {
        mRlGif.setVisibility(View.VISIBLE);
        mImgGif.setVisibility(View.GONE);
        mPbGifLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGifStart(byte[] bytes) {
        if(bytes!=null){
            try {
                mGifFromBytes = new GifDrawable(bytes);
                mImgGif.setBackground(mGifFromBytes);
                mGifFromBytes.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            mImgGif.setBackground(mActivity.getResources().getDrawable(R.mipmap.net_time_out));
        }
        mPbGifLoading.setVisibility(View.GONE);
        mImgGif.setVisibility(View.VISIBLE);
        mImgGif.setVisibility(View.VISIBLE);
    }

    /**
     * gif下载中
     * @param value
     */
    @Override
    public void onGifUpdate(Integer value) {
        mPbGifLoading.setProgress(value);
    }

    /**
     * 点击actionBar的刷新
     */
    public void onRefresh(){
        if(mCollectionView != null){
            mCollectionView.updateInventory(mAdapter.getInventory());
        }
    }

    /**
     * 构建一个Item的数据
     * @return
     */
    private List<MenuEntry> buildMenuList(){
        List<MenuEntry> list = new ArrayList<MenuEntry>();
        list.add(new MenuEntry(R.string.joke_item_title_neihan));
        list.add(new MenuEntry(R.string.joke_item_title_jiongtu));
        list.add(new MenuEntry(R.string.joke_item_title_xiaohua));
        list.add(new MenuEntry(R.string.joke_item_title_shipin));
        return list;
    }

    public class MenuEntry{
        public int titleId;
        public MenuEntry(int titleId) {
            this.titleId = titleId;
        }
    }
}
