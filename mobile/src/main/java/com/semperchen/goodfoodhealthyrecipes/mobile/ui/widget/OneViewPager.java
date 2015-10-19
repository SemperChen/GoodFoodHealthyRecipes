//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class OneViewPager extends ViewGroup {
    private static final String TAG = "LazyViewPager";
    private static final boolean DEBUG = false;
    private static final boolean USE_CACHE = false;
    private static final int DEFAULT_OFFSCREEN_PAGES = 0;
    private static final int MAX_SETTLE_DURATION = 600;
    private static final Comparator<ItemInfo> COMPARATOR = new Comparator<ItemInfo>() {
        public int compare(ItemInfo lhs, ItemInfo rhs) {
            return lhs.position - rhs.position;
        }
    };
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            --t;
            return t * t * t * t * t + 1.0F;
        }
    };
    private final ArrayList<ItemInfo> mItems = new ArrayList();
    private PagerAdapter mAdapter;
    private int mCurItem;
    private int mRestoredCurItem = -1;
    private Parcelable mRestoredAdapterState = null;
    private ClassLoader mRestoredClassLoader = null;
    private Scroller mScroller;
    private PagerObserver mObserver;
    private int mPageMargin;
    private Drawable mMarginDrawable;
    private int mChildWidthMeasureSpec;
    private int mChildHeightMeasureSpec;
    private boolean mInLayout;
    private boolean mScrollingCacheEnabled;
    private boolean mPopulatePending;
    private boolean mScrolling;
    private int mOffscreenPageLimit = 0;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;
    private int mTouchSlop;
    private float mInitialMotionX;
    private float mLastMotionX;
    private float mLastMotionY;
    private int mActivePointerId = -1;
    private static final int INVALID_POINTER = -1;
    private VelocityTracker mVelocityTracker;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private float mBaseLineFlingVelocity;
    private float mFlingVelocityInfluence;
    private boolean mFakeDragging;
    private long mFakeDragBeginTime;
    private EdgeEffectCompat mLeftEdge;
    private EdgeEffectCompat mRightEdge;
    private boolean mFirstLayout = true;
    private OnPageChangeListener mOnPageChangeListener;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_SETTLING = 2;
    private int mScrollState = 0;

    public OneViewPager(Context context) {
        super(context);
        this.initViewPager();
    }

    public OneViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initViewPager();
    }

    void initViewPager() {
        this.setWillNotDraw(false);
        this.setDescendantFocusability(262144);
        this.setFocusable(true);
        Context context = this.getContext();
        this.mScroller = new Scroller(context, sInterpolator);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffectCompat(context);
        this.mRightEdge = new EdgeEffectCompat(context);
        float density = context.getResources().getDisplayMetrics().density;
        this.mBaseLineFlingVelocity = 2500.0F * density;
        this.mFlingVelocityInfluence = 0.4F;
    }

    private void setScrollState(int newState) {
        if(this.mScrollState != newState) {
            this.mScrollState = newState;
            if(this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageScrollStateChanged(newState);
            }

        }
    }

    public void setAdapter(PagerAdapter adapter) {
        if(this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mObserver);
            this.mAdapter.startUpdate(this);

            for(int i = 0; i < this.mItems.size(); ++i) {
                ItemInfo ii = (ItemInfo)this.mItems.get(i);
                this.mAdapter.destroyItem(this, ii.position, ii.object);
            }

            this.mAdapter.finishUpdate(this);
            this.mItems.clear();
            this.removeAllViews();
            this.mCurItem = 0;
            this.scrollTo(0, 0);
        }

        this.mAdapter = adapter;
        if(this.mAdapter != null) {
            if(this.mObserver == null) {
                this.mObserver = new PagerObserver();
            }

            this.mAdapter.registerDataSetObserver(this.mObserver);
            this.mPopulatePending = false;
            if(this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                this.setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            } else {
                this.populate();
            }
        }

    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    public void setCurrentItem(int item) {
        this.mPopulatePending = false;
        this.setCurrentItemInternal(item, !this.mFirstLayout, false);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        this.mPopulatePending = false;
        this.setCurrentItemInternal(item, smoothScroll, false);
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
        this.setCurrentItemInternal(item, smoothScroll, always, 0);
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
        if(this.mAdapter != null && this.mAdapter.getCount() > 0) {
            if(!always && this.mCurItem == item && this.mItems.size() != 0) {
                this.setScrollingCacheEnabled(false);
            } else {
                if(item < 0) {
                    item = 0;
                } else if(item >= this.mAdapter.getCount()) {
                    item = this.mAdapter.getCount() - 1;
                }

                int pageLimit = this.mOffscreenPageLimit;
                if(item > this.mCurItem + pageLimit || item < this.mCurItem - pageLimit) {
                    for(int dispatchSelected = 0; dispatchSelected < this.mItems.size(); ++dispatchSelected) {
                        ((ItemInfo)this.mItems.get(dispatchSelected)).scrolling = true;
                    }
                }

                boolean var8 = this.mCurItem != item;
                this.mCurItem = item;
                this.populate();
                int destX = (this.getWidth() + this.mPageMargin) * item;
                if(smoothScroll) {
                    this.smoothScrollTo(destX, 0, velocity);
                    if(var8 && this.mOnPageChangeListener != null) {
                        this.mOnPageChangeListener.onPageSelected(item);
                    }
                } else {
                    if(var8 && this.mOnPageChangeListener != null) {
                        this.mOnPageChangeListener.onPageSelected(item);
                    }

                    this.completeScroll();
                    this.scrollTo(destX, 0);
                }

            }
        } else {
            this.setScrollingCacheEnabled(false);
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public void setOffscreenPageLimit(int limit) {
        if(limit < 0) {
            Log.w("LazyViewPager", "Requested offscreen page limit " + limit + " too small; defaulting to " + 0);
            limit = 0;
        }

        if(limit != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = limit;
            this.populate();
        }

    }

    public void setPageMargin(int marginPixels) {
        int oldMargin = this.mPageMargin;
        this.mPageMargin = marginPixels;
        int width = this.getWidth();
        this.recomputeScrollPosition(width, width, marginPixels, oldMargin);
        this.requestLayout();
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    public void setPageMarginDrawable(Drawable d) {
        this.mMarginDrawable = d;
        if(d != null) {
            this.refreshDrawableState();
        }

        this.setWillNotDraw(d == null);
        this.invalidate();
    }

    public void setPageMarginDrawable(int resId) {
        this.setPageMarginDrawable(this.getContext().getResources().getDrawable(resId));
    }

    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.mMarginDrawable;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable d = this.mMarginDrawable;
        if(d != null && d.isStateful()) {
            d.setState(this.getDrawableState());
        }

    }

    float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5F;
        f = (float)((double)f * 0.4712389167638204D);
        return (float)Math.sin((double)f);
    }

    void smoothScrollTo(int x, int y) {
        this.smoothScrollTo(x, y, 0);
    }

    void smoothScrollTo(int x, int y, int velocity) {
        if(this.getChildCount() == 0) {
            this.setScrollingCacheEnabled(false);
        } else {
            int sx = this.getScrollX();
            int sy = this.getScrollY();
            int dx = x - sx;
            int dy = y - sy;
            if(dx == 0 && dy == 0) {
                this.completeScroll();
                this.setScrollState(0);
            } else {
                this.setScrollingCacheEnabled(true);
                this.mScrolling = true;
                this.setScrollState(2);
                float pageDelta = (float)Math.abs(dx) / (float)(this.getWidth() + this.mPageMargin);
                int duration = (int)(pageDelta * 100.0F);
                velocity = Math.abs(velocity);
                if(velocity > 0) {
                    duration = (int)((float)duration + (float)duration / ((float)velocity / this.mBaseLineFlingVelocity) * this.mFlingVelocityInfluence);
                } else {
                    duration += 100;
                }

                duration = Math.min(duration, 600);
                this.mScroller.startScroll(sx, sy, dx, dy, duration);
                this.invalidate();
            }
        }
    }

    void addNewItem(int position, int index) {
        ItemInfo ii = new ItemInfo();
        ii.position = position;
        ii.object = this.mAdapter.instantiateItem(this, position);
        if(index < 0) {
            this.mItems.add(ii);
        } else {
            this.mItems.add(index, ii);
        }

    }

    void dataSetChanged() {
        boolean needPopulate = this.mItems.size() < 3 && this.mItems.size() < this.mAdapter.getCount();
        int newCurrItem = -1;

        for(int i = 0; i < this.mItems.size(); ++i) {
            ItemInfo ii = (ItemInfo)this.mItems.get(i);
            int newPos = this.mAdapter.getItemPosition(ii.object);
            if(newPos != -1) {
                if(newPos == -2) {
                    this.mItems.remove(i);
                    --i;
                    this.mAdapter.destroyItem(this, ii.position, ii.object);
                    needPopulate = true;
                    if(this.mCurItem == ii.position) {
                        newCurrItem = Math.max(0, Math.min(this.mCurItem, this.mAdapter.getCount() - 1));
                    }
                } else if(ii.position != newPos) {
                    if(ii.position == this.mCurItem) {
                        newCurrItem = newPos;
                    }

                    ii.position = newPos;
                    needPopulate = true;
                }
            }
        }

        Collections.sort(this.mItems, COMPARATOR);
        if(newCurrItem >= 0) {
            this.setCurrentItemInternal(newCurrItem, false, true);
            needPopulate = true;
        }

        if(needPopulate) {
            this.populate();
            this.requestLayout();
        }

    }

    void populate() {
        if(this.mAdapter != null) {
            if(!this.mPopulatePending) {
                if(this.getWindowToken() != null) {
                    this.mAdapter.startUpdate(this);
                    int pageLimit = this.mOffscreenPageLimit;
                    int startPos = Math.max(0, this.mCurItem - pageLimit);
                    int N = this.mAdapter.getCount();
                    int endPos = Math.min(N - 1, this.mCurItem + pageLimit);
                    int lastPos = -1;

                    for(int curItem = 0; curItem < this.mItems.size(); ++curItem) {
                        ItemInfo currentFocused = (ItemInfo)this.mItems.get(curItem);
                        if((currentFocused.position < startPos || currentFocused.position > endPos) && !currentFocused.scrolling) {
                            this.mItems.remove(curItem);
                            --curItem;
                            this.mAdapter.destroyItem(this, currentFocused.position, currentFocused.object);
                        } else if(lastPos < endPos && currentFocused.position > startPos) {
                            ++lastPos;
                            if(lastPos < startPos) {
                                lastPos = startPos;
                            }

                            while(lastPos <= endPos && lastPos < currentFocused.position) {
                                this.addNewItem(lastPos, curItem);
                                ++lastPos;
                                ++curItem;
                            }
                        }

                        lastPos = currentFocused.position;
                    }

                    lastPos = this.mItems.size() > 0?((ItemInfo)this.mItems.get(this.mItems.size() - 1)).position:-1;
                    if(lastPos < endPos) {
                        ++lastPos;

                        for(lastPos = lastPos > startPos?lastPos:startPos; lastPos <= endPos; ++lastPos) {
                            this.addNewItem(lastPos, -1);
                        }
                    }

                    ItemInfo var12 = null;

                    for(int var11 = 0; var11 < this.mItems.size(); ++var11) {
                        if(((ItemInfo)this.mItems.get(var11)).position == this.mCurItem) {
                            var12 = (ItemInfo)this.mItems.get(var11);
                            break;
                        }
                    }

                    this.mAdapter.setPrimaryItem(this, this.mCurItem, var12 != null?var12.object:null);
                    this.mAdapter.finishUpdate(this);
                    if(this.hasFocus()) {
                        View var13 = this.findFocus();
                        ItemInfo ii = var13 != null?this.infoForAnyChild(var13):null;
                        if(ii == null || ii.position != this.mCurItem) {
                            for(int i = 0; i < this.getChildCount(); ++i) {
                                View child = this.getChildAt(i);
                                ii = this.infoForChild(child);
                                if(ii != null && ii.position == this.mCurItem && child.requestFocus(2)) {
                                    break;
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.position = this.mCurItem;
        if(this.mAdapter != null) {
            ss.adapterState = this.mAdapter.saveState();
        }

        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
        } else {
            SavedState ss = (SavedState)state;
            super.onRestoreInstanceState(ss.getSuperState());
            if(this.mAdapter != null) {
                this.mAdapter.restoreState(ss.adapterState, ss.loader);
                this.setCurrentItemInternal(ss.position, false, true);
            } else {
                this.mRestoredCurItem = ss.position;
                this.mRestoredAdapterState = ss.adapterState;
                this.mRestoredClassLoader = ss.loader;
            }

        }
    }

    public void addView(View child, int index, LayoutParams params) {
        if(this.mInLayout) {
            this.addViewInLayout(child, index, params);
            child.measure(this.mChildWidthMeasureSpec, this.mChildHeightMeasureSpec);
        } else {
            super.addView(child, index, params);
        }

    }

    ItemInfo infoForChild(View child) {
        for(int i = 0; i < this.mItems.size(); ++i) {
            ItemInfo ii = (ItemInfo)this.mItems.get(i);
            if(this.mAdapter.isViewFromObject(child, ii.object)) {
                return ii;
            }
        }

        return null;
    }

    ItemInfo infoForAnyChild(View child) {
        ViewParent parent;
        while((parent = child.getParent()) != this) {
            if(parent == null || !(parent instanceof View)) {
                return null;
            }

            child = (View)parent;
        }

        return this.infoForChild(child);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        this.mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight(), 1073741824);
        this.mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom(), 1073741824);
        this.mInLayout = true;
        this.populate();
        this.mInLayout = false;
        int size = this.getChildCount();

        for(int i = 0; i < size; ++i) {
            View child = this.getChildAt(i);
            if(child.getVisibility() != 8) {
                child.measure(this.mChildWidthMeasureSpec, this.mChildHeightMeasureSpec);
            }
        }

    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(w != oldw) {
            this.recomputeScrollPosition(w, oldw, this.mPageMargin, this.mPageMargin);
        }

    }

    private void recomputeScrollPosition(int width, int oldWidth, int margin, int oldMargin) {
        int widthWithMargin = width + margin;
        int scrollPos;
        if(oldWidth > 0) {
            scrollPos = this.getScrollX();
            int oldwwm = oldWidth + oldMargin;
            int oldScrollItem = scrollPos / oldwwm;
            float scrollOffset = (float)(scrollPos % oldwwm) / (float)oldwwm;
            int scrollPos1 = (int)(((float)oldScrollItem + scrollOffset) * (float)widthWithMargin);
            this.scrollTo(scrollPos1, this.getScrollY());
            if(!this.mScroller.isFinished()) {
                int newDuration = this.mScroller.getDuration() - this.mScroller.timePassed();
                this.mScroller.startScroll(scrollPos1, 0, this.mCurItem * widthWithMargin, 0, newDuration);
            }
        } else {
            scrollPos = this.mCurItem * widthWithMargin;
            if(scrollPos != this.getScrollX()) {
                this.completeScroll();
                this.scrollTo(scrollPos, this.getScrollY());
            }
        }

    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        this.mInLayout = true;
        this.populate();
        this.mInLayout = false;
        int count = this.getChildCount();
        int width = r - l;

        for(int i = 0; i < count; ++i) {
            View child = this.getChildAt(i);
            ItemInfo ii;
            if(child.getVisibility() != 8 && (ii = this.infoForChild(child)) != null) {
                int loff = (width + this.mPageMargin) * ii.position;
                int childLeft = this.getPaddingLeft() + loff;
                int childTop = this.getPaddingTop();
                child.layout(childLeft, childTop, childLeft + child.getMeasuredWidth(), childTop + child.getMeasuredHeight());
            }
        }

        this.mFirstLayout = false;
    }

    public void computeScroll() {
        if(!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            int oldX = this.getScrollX();
            int oldY = this.getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if(oldX != x || oldY != y) {
                this.scrollTo(x, y);
            }

            if(this.mOnPageChangeListener != null) {
                int widthWithMargin = this.getWidth() + this.mPageMargin;
                int position = x / widthWithMargin;
                int offsetPixels = x % widthWithMargin;
                float offset = (float)offsetPixels / (float)widthWithMargin;
                this.mOnPageChangeListener.onPageScrolled(position, offset, offsetPixels);
            }

            this.invalidate();
        } else {
            this.completeScroll();
        }
    }

    private void completeScroll() {
        boolean needPopulate = this.mScrolling;
        int i;
        if(needPopulate) {
            this.setScrollingCacheEnabled(false);
            this.mScroller.abortAnimation();
            i = this.getScrollX();
            int ii = this.getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if(i != x || ii != y) {
                this.scrollTo(x, y);
            }

            this.setScrollState(0);
        }

        this.mPopulatePending = false;
        this.mScrolling = false;

        for(i = 0; i < this.mItems.size(); ++i) {
            ItemInfo var6 = (ItemInfo)this.mItems.get(i);
            if(var6.scrolling) {
                needPopulate = true;
                var6.scrolling = false;
            }
        }

        if(needPopulate) {
            this.populate();
        }

    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
//        int action = ev.getAction() & 255;
//        if(action != 3 && action != 1) {
//            if(action != 0) {
//                if(this.mIsBeingDragged) {
//                    return true;
//                }
//
//                if(this.mIsUnableToDrag) {
//                    return false;
//                }
//            }
//
//            switch(action) {
//                case 0:
//                    this.mLastMotionX = this.mInitialMotionX = ev.getX();
//                    this.mLastMotionY = ev.getY();
//                    this.mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
//                    if(this.mScrollState == 2) {
//                        this.mIsBeingDragged = true;
//                        this.mIsUnableToDrag = false;
//                        this.setScrollState(1);
//                    } else {
//                        this.completeScroll();
//                        this.mIsBeingDragged = false;
//                        this.mIsUnableToDrag = false;
//                    }
//                case 1:
//                case 3:
//                case 4:
//                case 5:
//                default:
//                    break;
//                case 2:
//                    int activePointerId = this.mActivePointerId;
//                    if(activePointerId != -1) {
//                        int pointerIndex = MotionEventCompat.findPointerIndex(ev, activePointerId);
//                        float x = MotionEventCompat.getX(ev, pointerIndex);
//                        float dx = x - this.mLastMotionX;
//                        float xDiff = Math.abs(dx);
//                        float y = MotionEventCompat.getY(ev, pointerIndex);
//                        float yDiff = Math.abs(y - this.mLastMotionY);
//                        int scrollX = this.getScrollX();
//                        boolean var10000;
//                        if(dx > 0.0F && scrollX == 0 || dx < 0.0F && this.mAdapter != null && scrollX >= (this.mAdapter.getCount() - 1) * this.getWidth() - 1) {
//                            var10000 = true;
//                        } else {
//                            var10000 = false;
//                        }
//
//                        if(this.canScroll(this, false, (int)dx, (int)x, (int)y)) {
//                            this.mInitialMotionX = this.mLastMotionX = x;
//                            this.mLastMotionY = y;
//                            return false;
//                        }
//
//                        if(xDiff > (float)this.mTouchSlop && xDiff > yDiff) {
//                            this.mIsBeingDragged = true;
//                            this.setScrollState(1);
//                            this.mLastMotionX = x;
//                            this.setScrollingCacheEnabled(true);
//                        } else if(yDiff > (float)this.mTouchSlop) {
//                            this.mIsUnableToDrag = true;
//                        }
//                    }
//                    break;
//                case 6:
//                    this.onSecondaryPointerUp(ev);
//            }
//
//            return this.mIsBeingDragged;
//        } else {
//            this.mIsBeingDragged = false;
//            this.mIsUnableToDrag = false;
//            this.mActivePointerId = -1;
//            return false;
//        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        return false;
//        if(this.mFakeDragging) {
//            return true;
//        } else if(ev.getAction() == 0 && ev.getEdgeFlags() != 0) {
//            return false;
//        } else if(this.mAdapter != null && this.mAdapter.getCount() != 0) {
//            if(this.mVelocityTracker == null) {
//                this.mVelocityTracker = VelocityTracker.obtain();
//            }
//
//            this.mVelocityTracker.addMovement(ev);
//            int action = ev.getAction();
//            boolean needsInvalidate = false;
//            int index;
//            float x;
//            int nextPage;
//            switch(action & 255) {
//                case 0:
//                    this.completeScroll();
//                    this.mLastMotionX = this.mInitialMotionX = ev.getX();
//                    this.mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
//                    break;
//                case 1:
//                    if(this.mIsBeingDragged) {
//                        VelocityTracker index1 = this.mVelocityTracker;
//                        index1.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
//                        int x1 = (int)VelocityTrackerCompat.getXVelocity(index1, this.mActivePointerId);
//                        this.mPopulatePending = true;
//                        int widthWithMargin2 = this.getWidth() + this.mPageMargin;
//                        int scrollX1 = this.getScrollX();
//                        int currentPage1 = scrollX1 / widthWithMargin2;
//                        nextPage = x1 > 0?currentPage1:currentPage1 + 1;
//                        this.setCurrentItemInternal(nextPage, true, true, x1);
//                        this.mActivePointerId = -1;
//                        this.endDrag();
//                        needsInvalidate = this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
//                    }
//                    break;
//                case 2:
//                    float widthWithMargin;
//                    float scrollX;
//                    float currentPage;
//                    if(!this.mIsBeingDragged) {
//                        index = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);
//                        x = MotionEventCompat.getX(ev, index);
//                        widthWithMargin = Math.abs(x - this.mLastMotionX);
//                        scrollX = MotionEventCompat.getY(ev, index);
//                        currentPage = Math.abs(scrollX - this.mLastMotionY);
//                        if(widthWithMargin > (float)this.mTouchSlop && widthWithMargin > currentPage) {
//                            this.mIsBeingDragged = true;
//                            this.mLastMotionX = x;
//                            this.setScrollState(1);
//                            this.setScrollingCacheEnabled(true);
//                        }
//                    }
//
//                    if(this.mIsBeingDragged) {
//                        index = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);
//                        x = MotionEventCompat.getX(ev, index);
//                        widthWithMargin = this.mLastMotionX - x;
//                        this.mLastMotionX = x;
//                        scrollX = (float)this.getScrollX();
//                        currentPage = scrollX + widthWithMargin;
//                        nextPage = this.getWidth();
//                        int widthWithMargin1 = nextPage + this.mPageMargin;
//                        int lastItemIndex = this.mAdapter.getCount() - 1;
//                        float leftBound = (float)Math.max(0, (this.mCurItem - 1) * widthWithMargin1);
//                        float rightBound = (float)(Math.min(this.mCurItem + 1, lastItemIndex) * widthWithMargin1);
//                        float position;
//                        if(currentPage < leftBound) {
//                            if(leftBound == 0.0F) {
//                                position = -currentPage;
//                                needsInvalidate = this.mLeftEdge.onPull(position / (float)nextPage);
//                            }
//
//                            currentPage = leftBound;
//                        } else if(currentPage > rightBound) {
//                            if(rightBound == (float)(lastItemIndex * widthWithMargin1)) {
//                                position = currentPage - rightBound;
//                                needsInvalidate = this.mRightEdge.onPull(position / (float)nextPage);
//                            }
//
//                            currentPage = rightBound;
//                        }
//
//                        this.mLastMotionX += currentPage - (float)((int)currentPage);
//                        this.scrollTo((int)currentPage, this.getScrollY());
//                        if(this.mOnPageChangeListener != null) {
//                            int position1 = (int)currentPage / widthWithMargin1;
//                            int positionOffsetPixels = (int)currentPage % widthWithMargin1;
//                            float positionOffset = (float)positionOffsetPixels / (float)widthWithMargin1;
//                            this.mOnPageChangeListener.onPageScrolled(position1, positionOffset, positionOffsetPixels);
//                        }
//                    }
//                    break;
//                case 3:
//                    if(this.mIsBeingDragged) {
//                        this.setCurrentItemInternal(this.mCurItem, true, true);
//                        this.mActivePointerId = -1;
//                        this.endDrag();
//                        needsInvalidate = this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
//                    }
//                case 4:
//                default:
//                    break;
//                case 5:
//                    index = MotionEventCompat.getActionIndex(ev);
//                    x = MotionEventCompat.getX(ev, index);
//                    this.mLastMotionX = x;
//                    this.mActivePointerId = MotionEventCompat.getPointerId(ev, index);
//                    break;
//                case 6:
//                    this.onSecondaryPointerUp(ev);
//                    this.mLastMotionX = MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, this.mActivePointerId));
//            }
//
//            if(needsInvalidate) {
//                this.invalidate();
//            }
//
//            return true;
//        } else {
//            return false;
//        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        boolean needsInvalidate = false;
        int overScrollMode = ViewCompat.getOverScrollMode(this);
        if(overScrollMode != 0 && (overScrollMode != 1 || this.mAdapter == null || this.mAdapter.getCount() <= 1)) {
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
        } else {
            int restoreCount;
            int width;
            if(!this.mLeftEdge.isFinished()) {
                restoreCount = canvas.save();
                width = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                canvas.rotate(270.0F);
                canvas.translate((float)(-width + this.getPaddingTop()), 0.0F);
                this.mLeftEdge.setSize(width, this.getWidth());
                needsInvalidate |= this.mLeftEdge.draw(canvas);
                canvas.restoreToCount(restoreCount);
            }

            if(!this.mRightEdge.isFinished()) {
                restoreCount = canvas.save();
                width = this.getWidth();
                int height = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                int itemCount = this.mAdapter != null?this.mAdapter.getCount():1;
                canvas.rotate(90.0F);
                canvas.translate((float)(-this.getPaddingTop()), (float)(-itemCount * (width + this.mPageMargin) + this.mPageMargin));
                this.mRightEdge.setSize(height, width);
                needsInvalidate |= this.mRightEdge.draw(canvas);
                canvas.restoreToCount(restoreCount);
            }
        }

        if(needsInvalidate) {
            this.invalidate();
        }

    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(this.mPageMargin > 0 && this.mMarginDrawable != null) {
            int scrollX = this.getScrollX();
            int width = this.getWidth();
            int offset = scrollX % (width + this.mPageMargin);
            if(offset != 0) {
                int left = scrollX - offset + width;
                this.mMarginDrawable.setBounds(left, 0, left + this.mPageMargin, this.getHeight());
                this.mMarginDrawable.draw(canvas);
            }
        }

    }

    public boolean beginFakeDrag() {
        if(this.mIsBeingDragged) {
            return false;
        } else {
            this.mFakeDragging = true;
            this.setScrollState(1);
            this.mInitialMotionX = this.mLastMotionX = 0.0F;
            if(this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            } else {
                this.mVelocityTracker.clear();
            }

            long time = SystemClock.uptimeMillis();
            MotionEvent ev = MotionEvent.obtain(time, time, 0, 0.0F, 0.0F, 0);
            this.mVelocityTracker.addMovement(ev);
            ev.recycle();
            this.mFakeDragBeginTime = time;
            return true;
        }
    }

    public void endFakeDrag() {
        if(!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        } else {
            VelocityTracker velocityTracker = this.mVelocityTracker;
            velocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
            int initialVelocity = (int)VelocityTrackerCompat.getYVelocity(velocityTracker, this.mActivePointerId);
            this.mPopulatePending = true;
            if(Math.abs(initialVelocity) <= this.mMinimumVelocity && Math.abs(this.mInitialMotionX - this.mLastMotionX) < (float)(this.getWidth() / 3)) {
                this.setCurrentItemInternal(this.mCurItem, true, true);
            } else if(this.mLastMotionX > this.mInitialMotionX) {
                this.setCurrentItemInternal(this.mCurItem - 1, true, true);
            } else {
                this.setCurrentItemInternal(this.mCurItem + 1, true, true);
            }

            this.endDrag();
            this.mFakeDragging = false;
        }
    }

    public void fakeDragBy(float xOffset) {
        if(!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        } else {
            this.mLastMotionX += xOffset;
            float scrollX = (float)this.getScrollX() - xOffset;
            int width = this.getWidth();
            int widthWithMargin = width + this.mPageMargin;
            float leftBound = (float)Math.max(0, (this.mCurItem - 1) * widthWithMargin);
            float rightBound = (float)(Math.min(this.mCurItem + 1, this.mAdapter.getCount() - 1) * widthWithMargin);
            if(scrollX < leftBound) {
                scrollX = leftBound;
            } else if(scrollX > rightBound) {
                scrollX = rightBound;
            }

            this.mLastMotionX += scrollX - (float)((int)scrollX);
            this.scrollTo((int)scrollX, this.getScrollY());
            if(this.mOnPageChangeListener != null) {
                int time = (int)scrollX / widthWithMargin;
                int positionOffsetPixels = (int)scrollX % widthWithMargin;
                float ev = (float)positionOffsetPixels / (float)widthWithMargin;
                this.mOnPageChangeListener.onPageScrolled(time, ev, positionOffsetPixels);
            }

            long time1 = SystemClock.uptimeMillis();
            MotionEvent ev1 = MotionEvent.obtain(this.mFakeDragBeginTime, time1, 2, this.mLastMotionX, 0.0F, 0);
            this.mVelocityTracker.addMovement(ev1);
            ev1.recycle();
        }
    }

    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = MotionEventCompat.getActionIndex(ev);
        int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if(pointerId == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0?1:0;
            this.mLastMotionX = MotionEventCompat.getX(ev, newPointerIndex);
            this.mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
            if(this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }

    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        if(this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }

    }

    private void setScrollingCacheEnabled(boolean enabled) {
        if(this.mScrollingCacheEnabled != enabled) {
            this.mScrollingCacheEnabled = enabled;
        }

    }

    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if(v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)v;
            int scrollX = v.getScrollX();
            int scrollY = v.getScrollY();
            int count = group.getChildCount();

            for(int i = count - 1; i >= 0; --i) {
                View child = group.getChildAt(i);
                if(x + scrollX >= child.getLeft() && x + scrollX < child.getRight() && y + scrollY >= child.getTop() && y + scrollY < child.getBottom() && this.canScroll(child, true, dx, x + scrollX - child.getLeft(), y + scrollY - child.getTop())) {
                    return true;
                }
            }
        }

        return checkV && ViewCompat.canScrollHorizontally(v, -dx);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event) || this.executeKeyEvent(event);
    }

    public boolean executeKeyEvent(KeyEvent event) {
        boolean handled = false;
        if(event.getAction() == 0) {
            switch(event.getKeyCode()) {
                case 21:
                    handled = this.arrowScroll(17);
                    break;
                case 22:
                    handled = this.arrowScroll(66);
                    break;
                case 61:
                    if(KeyEventCompat.hasNoModifiers(event)) {
                        handled = this.arrowScroll(2);
                    } else if(KeyEventCompat.hasModifiers(event, 1)) {
                        handled = this.arrowScroll(1);
                    }
            }
        }

        return handled;
    }

    public boolean arrowScroll(int direction) {
        View currentFocused = this.findFocus();
        if(currentFocused == this) {
            currentFocused = null;
        }

        boolean handled = false;
        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        if(nextFocused != null && nextFocused != currentFocused) {
            if(direction == 17) {
                if(currentFocused != null && nextFocused.getLeft() >= currentFocused.getLeft()) {
                    handled = this.pageLeft();
                } else {
                    handled = nextFocused.requestFocus();
                }
            } else if(direction == 66) {
                if(currentFocused != null && nextFocused.getLeft() <= currentFocused.getLeft()) {
                    handled = this.pageRight();
                } else {
                    handled = nextFocused.requestFocus();
                }
            }
        } else if(direction != 17 && direction != 1) {
            if(direction == 66 || direction == 2) {
                handled = this.pageRight();
            }
        } else {
            handled = this.pageLeft();
        }

        if(handled) {
            this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
        }

        return handled;
    }

    boolean pageLeft() {
        if(this.mCurItem > 0) {
            this.setCurrentItem(this.mCurItem - 1, true);
            return true;
        } else {
            return false;
        }
    }

    boolean pageRight() {
        if(this.mAdapter != null && this.mCurItem < this.mAdapter.getCount() - 1) {
            this.setCurrentItem(this.mCurItem + 1, true);
            return true;
        } else {
            return false;
        }
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        int focusableCount = views.size();
        int descendantFocusability = this.getDescendantFocusability();
        if(descendantFocusability != 393216) {
            for(int i = 0; i < this.getChildCount(); ++i) {
                View child = this.getChildAt(i);
                if(child.getVisibility() == 0) {
                    ItemInfo ii = this.infoForChild(child);
                    if(ii != null && ii.position == this.mCurItem) {
                        child.addFocusables(views, direction, focusableMode);
                    }
                }
            }
        }

        if(descendantFocusability != 262144 || focusableCount == views.size()) {
            if(!this.isFocusable()) {
                return;
            }

            if((focusableMode & 1) == 1 && this.isInTouchMode() && !this.isFocusableInTouchMode()) {
                return;
            }

            if(views != null) {
                views.add(this);
            }
        }

    }

    public void addTouchables(ArrayList<View> views) {
        for(int i = 0; i < this.getChildCount(); ++i) {
            View child = this.getChildAt(i);
            if(child.getVisibility() == 0) {
                ItemInfo ii = this.infoForChild(child);
                if(ii != null && ii.position == this.mCurItem) {
                    child.addTouchables(views);
                }
            }
        }

    }

    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int count = this.getChildCount();
        int index;
        byte increment;
        int end;
        if((direction & 2) != 0) {
            index = 0;
            increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }

        for(int i = index; i != end; i += increment) {
            View child = this.getChildAt(i);
            if(child.getVisibility() == 0) {
                ItemInfo ii = this.infoForChild(child);
                if(ii != null && ii.position == this.mCurItem && child.requestFocus(direction, previouslyFocusedRect)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        int childCount = this.getChildCount();

        for(int i = 0; i < childCount; ++i) {
            View child = this.getChildAt(i);
            if(child.getVisibility() == 0) {
                ItemInfo ii = this.infoForChild(child);
                if(ii != null && ii.position == this.mCurItem && child.dispatchPopulateAccessibilityEvent(event)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static class SavedState extends BaseSavedState {
        int position;
        Parcelable adapterState;
        ClassLoader loader;
        public static final Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks() {
            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        });

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.position);
            out.writeParcelable(this.adapterState, flags);
        }

        public String toString() {
            return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
        }

        SavedState(Parcel in, ClassLoader loader) {
            super(in);
            if(loader == null) {
                loader = this.getClass().getClassLoader();
            }

            this.position = in.readInt();
            this.adapterState = in.readParcelable(loader);
            this.loader = loader;
        }
    }

    private class PagerObserver extends DataSetObserver {
        private PagerObserver() {
        }

        public void onChanged() {
            OneViewPager.this.dataSetChanged();
        }

        public void onInvalidated() {
            OneViewPager.this.dataSetChanged();
        }
    }

    public interface OnPageChangeListener {
        void onPageScrolled(int var1, float var2, int var3);

        void onPageSelected(int var1);

        void onPageScrollStateChanged(int var1);
    }

    static class ItemInfo {
        Object object;
        int position;
        boolean scrolling;
        float widthFactor;
        float offset;

        ItemInfo() {
        }
    }
}
