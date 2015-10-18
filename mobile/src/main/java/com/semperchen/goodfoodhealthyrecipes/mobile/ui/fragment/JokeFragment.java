package com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.*;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
    private MainActivity activity;

    private CollectionView mCollectionView;
    private SingleMenuView mSingleMenuView;
    private NoScrollViewPager mViewPager;
    private OneViewPager mOnePager;
    private RelativeLayout rlGif;
    private GifImageView imgGif;
    private ProgressView pbGifLoading;

    private GifDrawable gifFromBytes = null;

    private NoClickLinearLayout mLinear;
    private FrameLayout mFrame,mContainer;
    private int mDisplayCols = 0;
    private int mItemCount = 0;
    private boolean isShowHeader = false;
    private String mHeaderLable = "";
    private List<Integer> mLayouList;
    private JokeAdapter adapter;

    private boolean isMenuOpen = false;
    private SingleMenuAdapter singleMenuAdapter;
    private boolean isVideo,isImage;
    private SingleMenuAdapter videoSingleMenuAdapter,imageSingleMenuAdapter;

    private boolean isIntensionDataError = true;
    private boolean isImageDataError = true;
    private boolean isJokeDataError = true;
    private boolean isVideoDataError = true;

    private int jokeId;
    private int maxId;

    private List<Detail> cacheIntensions;
    private int intensionAllPages;
    private int intensionNextPageInt = 0;
    private int intensionNextNumInt = 0;

    private List<Detail> cacheImages;
    private int imageAllPages;
    private int imageNextPageInt = 0;
    private int imageNextNumInt = 0;

    private List<Detail> cacheVideos;
    private int videoAllPages;
    private int videoNextPageInt = 0;
    private int videoNextNumInt = 0;

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
        rlGif = (RelativeLayout) mView.findViewById(R.id.rl_gif);
        imgGif = (GifImageView) mView.findViewById(R.id.img_gifview);
        pbGifLoading = (ProgressView) mView.findViewById(R.id.pb_gifloading);

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
        activity = (MainActivity) context;
        mContainer = (FrameLayout) activity.findViewById(R.id.container);
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        imgGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearGif();
            }
        });
        rlGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearGif();
            }
        });
    }

    /**
     * 释放Gif内存
     */
    private void clearGif(){
        if(gifFromBytes!=null && gifFromBytes.isRecycled()){
            gifFromBytes.recycle();
        }
        imageSingleMenuAdapter.cleanGifNet();
//        imageSingleMenuAdapter.cleanGifNet();
        imgGif.setBackground(null);
        rlGif.setVisibility(View.GONE);
//        pbGifLoading.setProgress(0);
    }

    /**
     * 初始化SingleMenu视图
     */
    private void initSingleMenuView() {
        mSingleMenuView = new SingleMenuView(activity, mLinear, mFrame);
        mSingleMenuView.setMenuView(R.layout.fragment_joke_singleview, 20, 100, 0);

        mViewPager = (NoScrollViewPager) mSingleMenuView.getMenuView().findViewById(R.id.vp_content);
        mOnePager = (OneViewPager)mSingleMenuView.getMenuView().findViewById(R.id.vp_content_video);
    }

    /**
     * 初始化SingleMenu视图监听
     */
    private void initSingleMenuViewListener() {
        ((ImageButton)(mSingleMenuView.getMenuView().findViewById(R.id.btn_close))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSingleMenuView.closeMenuView();
                isMenuOpen = false;
                if(isVideo) {
                    if (videoSingleMenuAdapter != null) {
                        videoSingleMenuAdapter.closeVideo(mOnePager.getCurrentItem());
                    }
                }
                if(isImage) {
                    if (imageSingleMenuAdapter != null) {
                        clearGif();
                    }
                }
            }
        });
        ((Button)(mSingleMenuView.getMenuView().findViewById(R.id.btn_pre))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isImage) {
                    if (imageSingleMenuAdapter != null) {
                        clearGif();
                    }
                }
                if(isVideo){
                    if (videoSingleMenuAdapter != null) {
                        videoSingleMenuAdapter.closeVideo(mOnePager.getCurrentItem());
                    }
                    if (mOnePager.getCurrentItem() <= 0) {
                        Toast.makeText(activity, "No Previous Page!", Toast.LENGTH_LONG).show();
                    }
                    mOnePager.setCurrentItem(mOnePager.getCurrentItem() - 1, false);
                }else {
                    if (mViewPager.getCurrentItem() <= 0) {
                        Toast.makeText(activity, "No Previous Page!", Toast.LENGTH_LONG).show();
                    }
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, false);
                }
            }
        });
        ((Button)(mSingleMenuView.getMenuView().findViewById(R.id.btn_checkmore))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startJokeReplaceAnim((int) view.getTag());
                mSingleMenuView.closeMenuView();
                if(isVideo) {
                    if (videoSingleMenuAdapter != null) {
                        videoSingleMenuAdapter.closeVideo(mOnePager.getCurrentItem());
                    }
                }
                if(isImage) {
                    if (imageSingleMenuAdapter != null) {
                        clearGif();
                    }
                }
            }
        });
        ((Button)(mSingleMenuView.getMenuView().findViewById(R.id.btn_next))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isImage) {
                    if (imageSingleMenuAdapter != null) {
                        clearGif();
                    }
                }
                if(isVideo){
                    if (videoSingleMenuAdapter != null) {
                        videoSingleMenuAdapter.closeVideo(mOnePager.getCurrentItem());
                    }
                    if (mOnePager.getCurrentItem() >= mOnePager.getAdapter().getCount() - 1) {
                        Toast.makeText(activity, "No Next Page,Put on the move button!", Toast.LENGTH_LONG).show();
                    }
                    mOnePager.setCurrentItem(mOnePager.getCurrentItem() + 1, false);
                }else{
                    if (mViewPager.getCurrentItem() >= mViewPager.getAdapter().getCount() - 1) {
                        Toast.makeText(activity, "No Next Page,Put on the move button!", Toast.LENGTH_LONG).show();
                    }
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, false);
                }
            }
        });
    }

    /**
     * 获取笑话数据并绑定
     */
    private void buildDataToJoke(final TextView tvValue, final ImageView imgValue, final ProgressBar pbLoading) {
        JokeNetWorkManager.getInstance().sendNetworkRequestForJoke(new Response.Listener<JokeData>() {
            @Override
            public void onResponse(JokeData jokeData) {
                //获取网络最新JokeId与本地最新JokeId对比
                int old_id = PreferenceManager.getInstance().getInt("joke_max_number", 0);
                int new_id = Integer.parseInt(jokeData.detail.get(0).id);
                maxId = jokeId = new_id > old_id ? new_id : old_id;
                PreferenceManager.getInstance().putInt("joke_max_number", maxId);
                tvValue.setText(jokeData.detail.get(0).content);
                isJokeDataError = false;
                tvValue.setVisibility(View.VISIBLE);
                imgValue.setVisibility(View.GONE);
                pbLoading.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                isJokeDataError = true;
            }
        }, 1, 0, GlobalContants.JOKE_CREATE_GET);
    }

    /**
     * 获取内涵段子数据并绑定
     * @param tvValue
     * @param imgValue
     * @param pbLoading
     */
    private void buildDataToIntension(final TextView tvValue, final ImageView imgValue, final ProgressBar pbLoading){
        IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
            @Override
            public void onResponse(IntensionData intensionData) {
                if (!intensionData.showapi_res_error.equals("")) {
                    tvValue.setText(intensionData.showapi_res_error);
                    isIntensionDataError = true;
                } else {
                    cacheIntensions = intensionData.showapi_res_body.pagebean.contentlist;
                    intensionAllPages = Integer.parseInt(intensionData.showapi_res_body.pagebean.allPages);
                    tvValue.setText(intensionData.showapi_res_body.pagebean.contentlist.get(0).text.trim());
                    isIntensionDataError = false;
                }
                tvValue.setVisibility(View.VISIBLE);
                imgValue.setVisibility(View.GONE);
                pbLoading.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isIntensionDataError = true;
            }
        }, JokeAdapter.ITEMVIEW_INTENSION, 1, GlobalContants.INTENSION_CREATE_GET);
    }

    /**
     * 获取囧图数据并绑定
     * @param tvValue
     * @param imgValue
     * @param pbLoading
     */
    private void buildDataToImage(final TextView tvValue, final ImageView imgValue, final ProgressBar pbLoading){
        IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
            @Override
            public void onResponse(IntensionData intensionData) {
                if (!intensionData.showapi_res_error.equals("")) {
                    tvValue.setText(intensionData.showapi_res_error);
                    isImageDataError = true;
                    tvValue.setVisibility(View.VISIBLE);
                    imgValue.setVisibility(View.GONE);
                } else {
                    isImageDataError = false;
                    cacheImages = intensionData.showapi_res_body.pagebean.contentlist;
                    imageAllPages = Integer.parseInt(intensionData.showapi_res_body.pagebean.allPages);
                    tvValue.setVisibility(View.GONE);
                    imgValue.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(intensionData.showapi_res_body.pagebean.contentlist.get(0).image0, imgValue);
                }
                pbLoading.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isImageDataError = true;
            }
        }, JokeAdapter.ITEMVIEW_IMAGE, 1, GlobalContants.IMAGE_CREATE_GET);
    }

    /**
     * 获取搞笑视频数据并绑定
     * @param tvValue
     * @param imgValue
     * @param pbLoading
     */
    private void buildDataToVideo(final TextView tvValue, final ImageView imgValue, final ProgressBar pbLoading){
        IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
            @Override
            public void onResponse(IntensionData intensionData) {
                if (!intensionData.showapi_res_error.equals("")) {
                    tvValue.setText(intensionData.showapi_res_error);
                    isVideoDataError = true;
                    tvValue.setVisibility(View.VISIBLE);
                    imgValue.setVisibility(View.GONE);
                } else {
                    isVideoDataError = false;
                    cacheVideos = intensionData.showapi_res_body.pagebean.contentlist;
                    videoAllPages = Integer.parseInt(intensionData.showapi_res_body.pagebean.allPages);
                    tvValue.setVisibility(View.GONE);
                    imgValue.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(intensionData.showapi_res_body.pagebean.contentlist.get(0).image3, imgValue);
                }
                pbLoading.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isVideoDataError = true;
            }
        }, JokeAdapter.ITEMVIEW_VIDEO, 1,GlobalContants.VIDEO_CREATE_GET);
    }

    /**
     * 填充CollectionView里的数据
     */
    private void initAdapter(){
        adapter = buildAdapter();
        mCollectionView.setCollctionAdapter(adapter);
        mCollectionView.updateInventory(adapter.getInventory());
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
                    Toast.makeText(activity,"未知错误,请长按刷新!",Toast.LENGTH_SHORT).show();
                }else {
                    openMenuSet(view,false,false);
                    //判断是否有缓存数据
                    if (cacheIntensions != null) {
                        List<Object> list = new ArrayList<Object>(cacheIntensions);
                        mViewPager.setAdapter(new SingleMenuAdapter(activity, list, (int) view.getTag(R.id.tag_second),null));
                        mViewPager.setCurrentItem(intensionNextNumInt);
                    } else {
                        IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
                            @Override
                            public void onResponse(IntensionData intensionData) {
                                if (!intensionData.showapi_res_error.equals("")) {
                                    mViewPager.setAdapter(new SingleMenuAdapter(activity, null, (int) view.getTag(R.id.tag_second),null));
                                } else {
                                    List<Object> list = new ArrayList<Object>(intensionData.showapi_res_body.pagebean.contentlist);
                                    mViewPager.setAdapter(new SingleMenuAdapter(activity, list, (int) view.getTag(R.id.tag_second),null));
                                    mViewPager.setCurrentItem(intensionNextNumInt);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }, JokeAdapter.ITEMVIEW_INTENSION, intensionNextPageInt==0?1:intensionNextPageInt,GlobalContants.INTENSION_OPEN_GET);
                    }
                }
                break;
            case JokeAdapter.ITEMVIEW_IMAGE:
                if(isImageDataError){
                    AnimationUtils.setShakeToView(view,3000,AnimationUtils.MIDDLE_SHAKE);
                    Toast.makeText(activity,"未知错误,请长按刷新!",Toast.LENGTH_SHORT).show();
                }else{
                    openMenuSet(view,false,true);
                    if(cacheImages!=null){
                        List<Object> list = new ArrayList<Object>(cacheImages);
                        imageSingleMenuAdapter = new SingleMenuAdapter(activity, list, (int) view.getTag(R.id.tag_second),this);
                        mViewPager.setAdapter(imageSingleMenuAdapter);
                        mViewPager.setCurrentItem(imageNextNumInt);
                    }else{
                        IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
                            @Override
                            public void onResponse(IntensionData intensionData) {
                                if (!intensionData.showapi_res_error.equals("")) {
                                    mViewPager.setAdapter(new SingleMenuAdapter(activity, null, (int) view.getTag(R.id.tag_second),JokeFragment.this));
                                } else {
                                    List<Object> list = new ArrayList<Object>(intensionData.showapi_res_body.pagebean.contentlist);
                                    imageSingleMenuAdapter = new SingleMenuAdapter(activity, list, (int) view.getTag(R.id.tag_second),JokeFragment.this);
                                    mViewPager.setAdapter(imageSingleMenuAdapter);
                                    mViewPager.setCurrentItem(imageNextNumInt);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }, JokeAdapter.ITEMVIEW_IMAGE, imageNextPageInt==0?1:imageNextPageInt,GlobalContants.IMAGE_OPEN_GET);
                    }
                }
                break;
            case JokeAdapter.ITEMVIEW_JOKE:
                if(isJokeDataError){
                    AnimationUtils.setShakeToView(view,3000,AnimationUtils.MIDDLE_SHAKE);
                    Toast.makeText(activity,"未知错误,请长按刷新!",Toast.LENGTH_SHORT).show();
                }else {
                    openMenuSet(view,false,false);
                    JokeNetWorkManager.getInstance().sendNetworkRequestForJoke(new Response.Listener<JokeData>() {
                        @Override
                        public void onResponse(JokeData jokeData) {
                            removeMoreItem(jokeData);
                            List<Object> list = new ArrayList<Object>(jokeData.detail);
                            mViewPager.setAdapter(new SingleMenuAdapter(activity, list, (int) view.getTag(R.id.tag_second),null));
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }, 20, (maxId - jokeId) / 20,GlobalContants.JOKE_OPEN_GET);
                }
                break;
            case JokeAdapter.ITEMVIEW_VIDEO:
                if(isVideoDataError){
                    AnimationUtils.setShakeToView(view,3000,AnimationUtils.MIDDLE_SHAKE);
                    Toast.makeText(activity,"未知错误,请长按刷新!",Toast.LENGTH_SHORT).show();
                }else{
                    openMenuSet(view,true,false);
                    if(cacheVideos!=null){
                        List<Object> list = new ArrayList<Object>(cacheVideos);
                        videoSingleMenuAdapter = new SingleMenuAdapter(activity, list, (int) view.getTag(R.id.tag_second),JokeFragment.this);
                        mViewPager.setAdapter(videoSingleMenuAdapter);
                        videoSingleMenuAdapter = new SingleMenuAdapter(activity, list, (int) view.getTag(R.id.tag_second),JokeFragment.this);
                        mOnePager.setAdapter(videoSingleMenuAdapter);
                        mOnePager.setCurrentItem(videoNextNumInt);
                        singleMenuAdapter = new SingleMenuAdapter(activity, list, (int) view.getTag(R.id.tag_second),JokeFragment.this);
                        mViewPager.setAdapter(singleMenuAdapter);
                        mViewPager.setCurrentItem(videoNextNumInt);
                    }else{
                        IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
                            @Override
                            public void onResponse(IntensionData intensionData) {
                                if (!intensionData.showapi_res_error.equals("")) {
                                    mOnePager.setAdapter(new SingleMenuAdapter(activity, null, (int) view.getTag(R.id.tag_second),JokeFragment.this));
                                } else {
                                    List<Object> list = new ArrayList<Object>(intensionData.showapi_res_body.pagebean.contentlist);
                                    videoSingleMenuAdapter = new SingleMenuAdapter(activity, list, (int) view.getTag(R.id.tag_second),JokeFragment.this);
                                    mViewPager.setAdapter(videoSingleMenuAdapter);
                                    videoSingleMenuAdapter = new SingleMenuAdapter(activity, list, (int) view.getTag(R.id.tag_second),JokeFragment.this);
                                    mOnePager.setAdapter(videoSingleMenuAdapter);
                                    mOnePager.setCurrentItem(videoNextNumInt);
                                    singleMenuAdapter = new SingleMenuAdapter(activity, list, (int) view.getTag(R.id.tag_second),JokeFragment.this);
                                    mViewPager.setAdapter(singleMenuAdapter);
                                    mViewPager.setCurrentItem(videoNextNumInt);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }, JokeAdapter.ITEMVIEW_VIDEO, videoNextPageInt==0?1:videoNextPageInt,GlobalContants.VIDEO_OPEN_GET);
                    }
                }
                break;
        }
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
            if(jokeId == Integer.parseInt(jokeData.detail.get(i).xhid)){
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
     * @param imgView
     * @param itemView
     */
    private void getDataFromService(final View imgView, final View itemView,final TextView tvValue,final ImageView imgValue) {
        if(((int)itemView.getTag(R.id.tag_second)) == JokeAdapter.ITEMVIEW_JOKE) {
            if(isJokeDataError){
                AnimationUtils.setShakeToView(itemView,3000,AnimationUtils.MIDDLE_SHAKE);
                Toast.makeText(activity,"未知错误,请长按刷新!",Toast.LENGTH_SHORT).show();
            }else {
                AnimationUtils.RotateAnimation(imgView, 0, 360, 500, -1, new LinearInterpolator());
                int old_id = PreferenceManager.getInstance().getInt("joke_max_number", 0);
                jokeId = (new Random()).nextInt(old_id);

                JokeNetWorkManager.getInstance().sendNetworkRequestForJoke(new Response.Listener<JokeData>() {
                    @Override
                    public void onResponse(JokeData jokeData) {
                        isJokeDataError = false;
                        successDataAndRefresh(imgView, itemView, tvValue, imgValue, jokeData.detail.get(0).content);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        isJokeDataError = true;
                    }
                }, 1, (maxId - jokeId),GlobalContants.JOKE_REFRESH_GET);
            }
        }else if(((int)itemView.getTag(R.id.tag_second)) == JokeAdapter.ITEMVIEW_INTENSION){
            if(isIntensionDataError){
                AnimationUtils.setShakeToView(itemView,3000,AnimationUtils.MIDDLE_SHAKE);
                Toast.makeText(activity,"未知错误,请长按刷新!",Toast.LENGTH_SHORT).show();
            }else {
                AnimationUtils.RotateAnimation(imgView, 0, 360, 500, -1, new LinearInterpolator());
                intensionNextPageInt = (new Random()).nextInt(intensionAllPages) + 1;
                IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
                    @Override
                    public void onResponse(IntensionData intensionData) {
                        if (!intensionData.showapi_res_error.equals("")) {
                            successDataAndRefresh(imgView, itemView, tvValue, imgValue, intensionData.showapi_res_error);
                            isIntensionDataError = true;
                        } else {
                            intensionNextNumInt = (new Random()).nextInt(20);
                            isIntensionDataError = false;
                            cacheIntensions = intensionData.showapi_res_body.pagebean.contentlist;
                            successDataAndRefresh(imgView, itemView, tvValue, imgValue, intensionData.showapi_res_body.pagebean.contentlist.get(intensionNextNumInt).text);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isIntensionDataError = true;
                    }
                }, JokeAdapter.ITEMVIEW_INTENSION, intensionNextPageInt,GlobalContants.INTENSION_REFRESH_GET);
            }
        }else if(((int)itemView.getTag(R.id.tag_second) == JokeAdapter.ITEMVIEW_IMAGE)){
            if(isImageDataError){
                AnimationUtils.setShakeToView(itemView,3000,AnimationUtils.MIDDLE_SHAKE);
                Toast.makeText(activity,"未知错误,请长按刷新!",Toast.LENGTH_SHORT).show();
            }else {
                AnimationUtils.RotateAnimation(imgView, 0, 360, 500, -1, new LinearInterpolator());
                imageNextPageInt = (new Random()).nextInt(imageAllPages) + 1;
                IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
                    @Override
                    public void onResponse(IntensionData intensionData) {
                        if (!intensionData.showapi_res_error.equals("")) {
                            successDataAndRefresh(imgView, itemView, tvValue, imgValue, intensionData.showapi_res_error);
                            isImageDataError = true;
                        } else {
                            imageNextNumInt = (new Random()).nextInt(20);
                            isImageDataError = false;
                            cacheImages = intensionData.showapi_res_body.pagebean.contentlist;
                            successDataAndRefresh(imgView, itemView, tvValue, imgValue, intensionData.showapi_res_body.pagebean.contentlist.get(imageNextNumInt).image0);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isImageDataError = true;
                    }
                }, JokeAdapter.ITEMVIEW_IMAGE, imageNextPageInt,GlobalContants.IMAGE_REFRESH_GET);
            }
        }else if(((int)itemView.getTag(R.id.tag_second) == JokeAdapter.ITEMVIEW_VIDEO)){
            if(isVideoDataError){
                AnimationUtils.setShakeToView(itemView,3000,AnimationUtils.MIDDLE_SHAKE);
                Toast.makeText(activity,"未知错误,请长按刷新!",Toast.LENGTH_SHORT).show();
            }else{
                AnimationUtils.RotateAnimation(imgView, 0, 360, 500, -1, new LinearInterpolator());
                videoNextPageInt = (new Random()).nextInt(videoAllPages) + 1;
                IntensionNetWorkManager.getInstance().sendNetworkRequestForIntension(new Response.Listener<IntensionData>() {
                    @Override
                    public void onResponse(IntensionData intensionData) {
                        if (!intensionData.showapi_res_error.equals("")) {
                            successDataAndRefresh(imgView, itemView, tvValue, imgValue, intensionData.showapi_res_error);
                            isImageDataError = true;
                        }else{
                            videoNextNumInt = (new Random()).nextInt(20);
                            isVideoDataError = false;
                            cacheVideos = intensionData.showapi_res_body.pagebean.contentlist;
                            successDataAndRefresh(imgView, itemView, tvValue, imgValue, intensionData.showapi_res_body.pagebean.contentlist.get(imageNextNumInt).image3);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isVideoDataError = true;
                    }
                },JokeAdapter.ITEMVIEW_VIDEO,videoNextPageInt,GlobalContants.VIDEO_OPEN_GET);
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
     * @param imgView
     * @param itemView
     * @param tvValue
     * @param imgValue
     * @param content
     */
    private void successDataAndRefresh(final View imgView,final View itemView,final TextView tvValue,final ImageView imgValue, final String content){
            AnimationUtils.ObjectAnimation(itemView,"rotationY", 0, 360,500, new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (((int) itemView.getTag(R.id.tag_second)) == JokeAdapter.ITEMVIEW_JOKE || ((int) itemView.getTag(R.id.tag_second)) == JokeAdapter.ITEMVIEW_INTENSION) {
                        tvValue.setText(content);
                        imgView.clearAnimation();
                    }else if(((int)itemView.getTag(R.id.tag_second)) == JokeAdapter.ITEMVIEW_IMAGE || ((int) itemView.getTag(R.id.tag_second)) == JokeAdapter.ITEMVIEW_VIDEO){
                        ImageLoader.getInstance().displayImage(content, imgValue,new SimpleImageLoadingListener(){
                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                if(loadedImage!=null){
                                    imgView.clearAnimation();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(activity,"该方法还没有实现...",Toast.LENGTH_SHORT).show();
                        imgView.clearAnimation();
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
    private void statAnimAndRefreshView(final View parent, final TextView tv, final ImageView img, final ProgressBar pb) {
        Toast.makeText(activity,"重新刷新",Toast.LENGTH_SHORT).show();
        AnimationUtils.ObjectAnimation(parent, "rotationY", 0, 360, 500, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                switch ((int)parent.getTag(R.id.tag_second)){
                    case JokeAdapter.ITEMVIEW_INTENSION:
                        RequestManager.getRequestQueue().cancelAll(GlobalContants.INTENSION_CREATE_GET);
                        buildDataToIntension(tv, img, pb);
                        break;
                    case JokeAdapter.ITEMVIEW_IMAGE:
                        RequestManager.getRequestQueue().cancelAll(GlobalContants.IMAGE_CREATE_GET);
                        buildDataToImage(tv, img, pb);
                        break;
                    case JokeAdapter.ITEMVIEW_JOKE:
                        RequestManager.getRequestQueue().cancelAll(GlobalContants.JOKE_CREATE_GET);
                        buildDataToJoke(tv, img, pb);
                        break;
                    case JokeAdapter.ITEMVIEW_VIDEO:
                        RequestManager.getRequestQueue().cancelAll(GlobalContants.VIDEO_CREATE_GET);
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
                activity.setupJokeMoreContent(viewtype);
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
     * @param view
     * @param parent
     * @param tv
     * @param img
     */
    @Override
    public void onRefreshDataFromService(View view, View parent, TextView tv, ImageView img) {
        this.getDataFromService(view, parent, tv, img);
    }

    /**
     * 长按刷新回调方法
     * @param parent
     */
    @Override
    public void onItemLongClick(View parent, TextView tv, ImageView img, ProgressBar pb) {
        this.statAnimAndRefreshView(parent, tv, img,pb);
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
        rlGif.setVisibility(View.VISIBLE);
        imgGif.setVisibility(View.GONE);
        pbGifLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGifStart(byte[] bytes) {
        if(bytes!=null){
            try {
                gifFromBytes = new GifDrawable(bytes);
                imgGif.setBackground(gifFromBytes);
                gifFromBytes.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            imgGif.setBackground(activity.getResources().getDrawable(R.mipmap.net_time_out));
        }
        pbGifLoading.setVisibility(View.GONE);
        imgGif.setVisibility(View.VISIBLE);
        gifFromBytes.start();
        imgGif.setVisibility(View.VISIBLE);
    }

    /**
     * gif下载中
     * @param value
     */
    @Override
    public void onGifUpdate(Integer value) {
        pbGifLoading.setProgress(value);
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
