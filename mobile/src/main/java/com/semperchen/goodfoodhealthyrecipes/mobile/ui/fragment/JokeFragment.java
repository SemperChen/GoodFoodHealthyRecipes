package com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.*;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.JokeData;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.JokeData.Joke;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.net.JokeNetWorkManager;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.utils.AnimationUtils;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.utils.PrefUtils;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.activity.MainActivity;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter.JokeAdapter;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter.SingleMenuAdapter;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget.CollectionView;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget.NoScrollViewPager;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget.SingleMenuView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Semper on 2015/9/17.
 */
public class JokeFragment extends BaseToolbarFragment implements JokeAdapter.JokeAdapterCallbacks{
    private MainActivity activity;

    private CollectionView mCollectionView;
    private SingleMenuView mSingleMenuView;
    private NoScrollViewPager mViewPager;

    private LinearLayout mLinear;
    private FrameLayout mFrame,mContainer;
    private int mDisplayCols = 0;
    private int mItemCount = 0;
    private boolean isShowHeader = false;
    private String mHeaderLable = "";
    private List<Integer> mLayouList;

    private int jokeId;
    private int maxId;

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
        mLinear = (LinearLayout) mView.findViewById(R.id.joke_ll_conent);

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
     * @param rootView 鏍硅鍥�
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

        buildAdapterFromService();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
        mContainer = (FrameLayout) activity.findViewById(R.id.container);
    }

    /**
     * 初始化SingleMenu视图
     */
    private void initSingleMenuView() {
        mSingleMenuView = new SingleMenuView(activity, mLinear, mFrame);
        mSingleMenuView.setMenuView(R.layout.fragment_joke_singleview, 20, 100, 0);

        mViewPager = (NoScrollViewPager) mSingleMenuView.getMenuView().findViewById(R.id.vp_content);
    }

    /**
     * 初始化SingleMenu视图监听
     */
    private void initSingleMenuViewListener() {
        ((ImageButton)(mSingleMenuView.getMenuView().findViewById(R.id.btn_close))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSingleMenuView.closeMenuView();
            }
        });
        ((Button)(mSingleMenuView.getMenuView().findViewById(R.id.btn_pre))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewPager.getCurrentItem() <= 0) {
                    Toast.makeText(activity, "No Previous Page!", Toast.LENGTH_LONG).show();
                }
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, false);
            }
        });
        ((Button)(mSingleMenuView.getMenuView().findViewById(R.id.btn_checkmore))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startJokeReplaceAnim((int) view.getTag());
                mSingleMenuView.closeMenuView();
            }
        });
        ((Button)(mSingleMenuView.getMenuView().findViewById(R.id.btn_next))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewPager.getCurrentItem() >= mViewPager.getAdapter().getCount() - 1) {
                    Toast.makeText(activity, "No Next Page,Put on the move button!", Toast.LENGTH_LONG).show();
                }
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, false);
            }
        });
    }

    /**
     * 访问网络获取并绑定数据
     */
    private void buildAdapterFromService() {
        JokeNetWorkManager.getInstance().sendNetworkRequestForJoke(new Response.Listener<JokeData>() {
            @Override
            public void onResponse(JokeData jokeData) {
                //获取网络最新JokeId与本地最新JokeId对比
                int old_id = PrefUtils.getInt(activity, "joke_max_number", 0);
                int new_id = Integer.parseInt(jokeData.detail.get(0).id);
                maxId = jokeId = new_id > old_id ? new_id : old_id;
                PrefUtils.putInt(activity, "joke_max_number", maxId);
                initAdapter(jokeData.detail);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, 1, 0);
    }

    /**
     * 填充CollectionView里的数据
     * @param jokes
     */
    private void initAdapter(List<Joke> jokes){
        final JokeAdapter adapter = buildAdapter(jokes);
        mCollectionView.setCollctionAdapter(adapter);
        mCollectionView.updateInventory(adapter.getInventory());
    }

    /**
     * 构建Joke的适配器,即每个Item内容
     * @return
     */
    protected JokeAdapter buildAdapter(List<Joke> jokes){
        return new JokeAdapter(getActivity(),buildMenuList(jokes),mDisplayCols,mItemCount,isShowHeader,mHeaderLable,mLayouList,this);
    }

    /**
     * 打开SingleMenu并访问网络
     * @param view
     */
    private void openSingleMenuViewAndNetWork(final View view) {
        mSingleMenuView.openMenuView(view);
        //从网络获取资源
        JokeNetWorkManager.getInstance().sendNetworkRequestForJoke(new Response.Listener<JokeData>() {
            @Override
            public void onResponse(JokeData jokeData) {
                removeMoreItem(jokeData);
                ((ViewPager) mSingleMenuView.getMenuView().findViewById(R.id.vp_content)).setAdapter(new SingleMenuAdapter(activity, jokeData.detail, (int) view.getTag(R.id.tag_second)));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, 20, (maxId - jokeId) / 20);
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
        AnimationUtils.RotateAnimation(imgView, 0, 360, 500, -1, new LinearInterpolator());

        if(itemView.getTag(R.id.tag_second) == JokeAdapter.ITEMVIEW_JOKE) {
            int old_id = PrefUtils.getInt(activity, "joke_max_number", 0);
            jokeId = (new Random()).nextInt(old_id);

            JokeNetWorkManager.getInstance().sendNetworkRequestForJoke(new Response.Listener<JokeData>() {
                @Override
                public void onResponse(JokeData jokeData) {
                    successDataAndRefresh(imgView, itemView, tvValue, imgValue, jokeData.detail.get(0).content);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            }, 1, (maxId - jokeId));
        }else if(itemView.getTag(R.id.tag_second) == JokeAdapter.ITEMVIEW_IMAGE){
            //模拟从网上获取资源
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            successDataAndRefresh(imgView,itemView,tvValue,imgValue,null);
                        }
                    });
                }
            }).start();
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
        ObjectAnimator objAnim = ObjectAnimator.ofFloat(itemView, "rotationY", 0, 360);
        objAnim.setDuration(500);
        objAnim.setRepeatCount(0);
        objAnim.start();

        objAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(itemView.getTag(R.id.tag_second) == JokeAdapter.ITEMVIEW_JOKE) {
                    tvValue.setText(content);
                }else if(itemView.getTag(R.id.tag_second) == JokeAdapter.ITEMVIEW_IMAGE) {
                    imgValue.setBackgroundResource(R.drawable.image_commend_004);
                }
                imgView.clearAnimation();
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
     * 构建每个Item的数据
     * @return
     */
    private List<MenuEntry> buildMenuList(List<Joke> jokes){
        ArrayList<MenuEntry> list = new ArrayList<MenuEntry>();

        list.add(new MenuEntry(R.string.joke_item_title_neihan,activity.getString(R.string.joke_item_value_text)));
        list.add(new MenuEntry(R.string.joke_item_title_jiongtu,activity.getString(R.string.joke_item_value)));
        list.add(new MenuEntry(R.string.joke_item_title_xiaohua, jokes.get(0).content));
        list.add(new MenuEntry(R.string.joke_item_title_xinwen, activity.getString(R.string.joke_item_value)));

        return list;
    }

    public class MenuEntry{
        public int titleId;
        public String value;

        public MenuEntry(int titleId,String value) {
            this.titleId = titleId;
            this.value = value;
        }
    }
}
