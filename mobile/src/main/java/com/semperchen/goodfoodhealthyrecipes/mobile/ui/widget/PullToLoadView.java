package com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;

public class PullToLoadView extends FrameLayout {

    private boolean hasMore = true;
    //是否正在刷新中
    private boolean isRefreshing = false;
    //是否正在加载更多
    private boolean isLoadingMore = false;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private PullCallback mPullCallback;
    private RecyclerViewPositionHelper mRecyclerViewHelper;
    protected ScrollDirection mCurScrollingDirection;
    protected int mPrevFirstVisibleItem = 0;
    private int mLoadMoreOffset = 4;

    public PullToLoadView(Context context) {
        this(context, null);
    }

    public PullToLoadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.pull_loadmore, this, true);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.pull_loadmore_swiperefreshlayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.pull_loadmore_recyclerview);
        mProgressBar = (ProgressBar) findViewById(R.id.pull_loadmore_progressbar);
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(mRecyclerView);
    }

    private void init() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (null != mPullCallback && !isRefreshing) {
                    setRefresh();
                }
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mCurScrollingDirection = null;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mCurScrollingDirection == null) {
                    mCurScrollingDirection = ScrollDirection.SAME;
                    mPrevFirstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                } else {
                    final int firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                    if (firstVisibleItem > mPrevFirstVisibleItem) {

                        mCurScrollingDirection = ScrollDirection.UP;
                    } else if (firstVisibleItem < mPrevFirstVisibleItem) {

                        mCurScrollingDirection = ScrollDirection.DOWN;
                    } else {
                        mCurScrollingDirection = ScrollDirection.SAME;
                    }
                    mPrevFirstVisibleItem = firstVisibleItem;
                }

                if (mCurScrollingDirection == ScrollDirection.UP){

                    final int totalItemCount = mRecyclerViewHelper.getItemCount();
                    final int firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                    final int visibleItemCount = Math.abs(mRecyclerViewHelper.findLastVisibleItemPosition() - firstVisibleItem);
                    final int lastAdapterPosition = totalItemCount - 1;
                    final int lastVisiblePosition = (firstVisibleItem + visibleItemCount) - 1;
                    if (lastVisiblePosition >= (lastAdapterPosition - mLoadMoreOffset)) {
                        if (null != mPullCallback && !isLoadingMore) {
                            setIsLoadMore(true);
                            mProgressBar.setVisibility(VISIBLE);
                            mPullCallback.onLoadMore();
                        }
                    }
                }
            }
        });
    }

    /**
     * 刷新或加载完成重置数据
     */
    public void setComplete() {
        mProgressBar.setVisibility(GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        setRefreshEnable(true);
        isRefreshing = false;
        isLoadingMore = false;
    }

    /**
     * 设置能否进行刷新
     */
    public void setRefreshEnable(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
    }

    /**
     * 刷新
     */
    private void setRefresh() {
        setIsRefresh(true);
        setRefreshEnable(false);
        mPullCallback.onRefresh();
    }

    /**
     * 初始化加载
     */
    public void initLoad() {
        if (null != mPullCallback) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
            if (null != mPullCallback && !isRefreshing) {
                setRefresh();
            }
        }
    }

    /**
     * 设置下拉刷新动画颜色
     *
     * @param colorResIds 颜色id集
     */
    public void setColorSchemeResources(int... colorResIds) {
        mSwipeRefreshLayout.setColorSchemeResources(colorResIds);
    }

    public RecyclerView getRecyclerView() {
        return this.mRecyclerView;
    }

    public void setPullCallback(PullCallback mPullCallback) {
        this.mPullCallback = mPullCallback;
    }

    public void setLoadMoreOffset(int mLoadMoreOffset) {
        this.mLoadMoreOffset = mLoadMoreOffset;
    }

    public boolean isLoadingMore() {
        return isLoadingMore;
    }

    public void setIsLoadMore(boolean isLoadMore) {
        this.isLoadingMore = isLoadMore;
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public void setIsRefresh(boolean isRefresh) {
        this.isRefreshing = isRefresh;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public enum ScrollDirection {
        UP,
        DOWN,
        SAME
    }
}
