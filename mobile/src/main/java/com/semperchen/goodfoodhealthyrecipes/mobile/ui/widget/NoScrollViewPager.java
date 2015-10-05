package com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPager extends ViewPager{
	
	public NoScrollViewPager(Context context) {
		super(context);
	}
	
	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return false;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}
}
