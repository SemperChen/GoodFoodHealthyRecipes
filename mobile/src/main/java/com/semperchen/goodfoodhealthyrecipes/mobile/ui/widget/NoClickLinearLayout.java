package com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by abc on 2015/10/9.
 */
public class NoClickLinearLayout extends LinearLayout{
    private boolean isClickable = true;

    public NoClickLinearLayout(Context context) {
        this(context,null);
    }

    public NoClickLinearLayout(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public NoClickLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void isClickable(boolean clickable){
        this.isClickable = clickable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(isClickable){
            return super.onInterceptTouchEvent(ev);
        }else{
            return true;
        }
    }
}
