package com.semperchen.goodfoodhealthyrecipes.mobile.core.listener;

import android.support.v7.widget.RecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Semper on 2015/9/30.
 */
public class RecycleViewPauseOnScrollListener extends RecyclerView.OnScrollListener {

    private ImageLoader imageLoader;

    private final boolean pauseOnScroll;
    private final boolean pauseOnFling;
    private final RecyclerView.OnScrollListener externalListener;


    public RecycleViewPauseOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
        this(imageLoader, pauseOnScroll, pauseOnFling, null);
    }


    public RecycleViewPauseOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling,
                                            RecyclerView.OnScrollListener customListener) {
        this.imageLoader = imageLoader;
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnFling = pauseOnFling;
        externalListener = customListener;
    }

    @Override
    public void onScrollStateChanged(RecyclerView view, int scrollState) {
        switch (scrollState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                imageLoader.resume();
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                if (pauseOnScroll) {
                    imageLoader.pause();
                }
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                if (pauseOnFling) {
                    imageLoader.pause();
                }
                break;
        }
        if (externalListener != null) {
            externalListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        if (externalListener != null) {
            externalListener.onScrolled(view, dx, dy);
        }
    }
}
