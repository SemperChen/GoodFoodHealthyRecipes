package com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget;

import java.lang.reflect.Field;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;

public class SingleMenuView implements AnimatorListener{
	private final static int MENUVIEW_SHOW=0x1;
	private final static int MENUVIEW_CLOSE=0x2;
	
	private int[] location;
	private int viewWidth,viewHeight;
	private int windowWidth ,windowHeight;
	private int viewMoveX,viewMoveY;
	private float toValueX,toValueY;
	private int widthOffset,heightOffset;
	private int actionBarHeight;
	
	private View mView = null;
	private ViewGroup mRoot;
	private ViewGroup mContentView;
	private Activity mContext;
	
	private View mMenuViewSmall; 
	private View mMenuViewBig;
	private FrameLayout.LayoutParams paramsBig;
	private FrameLayout.LayoutParams paramsSmall;

	private int AnimState = MENUVIEW_CLOSE;
	private SingleMenuViewCallbacks mCallbacks;
	
	public SingleMenuView(Activity context,ViewGroup contentView,ViewGroup root) {
		mRoot = root;
		mContext = context;
		mContentView = contentView;
		
		initData();
	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		location = new int[2];
		
		windowWidth = mContext.getWindowManager().getDefaultDisplay().getWidth();
		windowHeight = mContext.getWindowManager().getDefaultDisplay().getHeight();
	}

	public void setCallbacks(SingleMenuViewCallbacks callbacks){
		mCallbacks = callbacks;
	}

	/**
	 * ������ʾ����ͼ
	 * @param layoutResId
	 * @param widthOffset       ����Ļ��ߵľ���
	 * @param heightOffset	   ����Ļ�ϱߵľ���
	 * @param actionBarHeight   һ��Ϊ0����
	 */
	public void setMenuView(int layoutResId,int widthOffset,int heightOffset,int actionBarHeight){
		mMenuViewBig = View.inflate(mContext, layoutResId, null);
//		mMenuViewSmall = new View(mContext);
//		mMenuViewSmall.setBackgroundDrawable(mMenuViewBig.getBackground());
		
		mMenuViewSmall = View.inflate(mContext, layoutResId, null);
		for(int i=0;i<((ViewGroup)mMenuViewSmall).getChildCount();i++){
			((ViewGroup)mMenuViewSmall).getChildAt(i).setVisibility(View.INVISIBLE);
		}
		
		
		this.widthOffset = widthOffset;
		this.heightOffset = heightOffset;
		this.actionBarHeight = actionBarHeight;
		
	}

	/**
	 * �ر���ͼ
	 */
	public void closeMenuView(){
		if(mView != null){
			AnimState = MENUVIEW_CLOSE;
			closeAnim();
		}
	}

	/**
	 * ����ͼ
	 * @param view �����view
	 */
	public void openMenuView(View view){
		mMenuViewBig.findViewById(R.id.btn_checkmore).setTag(view.getTag(R.id.tag_second));

		if(mView != view){
			this.mView = view;
//			mMenuViewSmall = view;
			mRoot.removeAllViews();
			compuleResult();
		}
		
		if(mMenuViewSmall != null && mMenuViewBig != null){
			if(mRoot.getChildCount() <= 1){
				if(mCallbacks!=null){
					mCallbacks.openMenuSetData(mContext, mMenuViewBig);
				}
				mRoot.addView(mMenuViewSmall, paramsSmall);
				mRoot.addView(mMenuViewBig, 1, paramsBig);
			}
			AnimState = MENUVIEW_SHOW;
			startAnim();
		}else{
			throw new RuntimeException();
		}
	}

	public View getMenuView(){
		if(mMenuViewBig != null){
			return mMenuViewBig; 
		}else{
			throw new RuntimeException("the menuview is not create!");
		}
	}

	/**
	 * �����򿪶���
	 */
	private void startAnim() {		
		ObjectAnimator tranAnimY = ObjectAnimator.ofFloat(mMenuViewSmall, "translationY", 0,-20);
		ObjectAnimator tranAnimPointX = ObjectAnimator.ofFloat(mMenuViewSmall, "translationX",0,viewMoveX);
		ObjectAnimator tranAnimPointY = ObjectAnimator.ofFloat(mMenuViewSmall, "translationY",-20,viewMoveY);
		ObjectAnimator scaleAnimX = ObjectAnimator.ofFloat(mMenuViewSmall, "scaleX", 1.0f, (toValueX+1.0f));
		ObjectAnimator scaleAnimY = ObjectAnimator.ofFloat(mMenuViewSmall, "scaleY", 1.0f, (toValueY+1.0f));
		AnimatorSet set = new AnimatorSet();
		set.setDuration(500);
		set.play(tranAnimY).before(tranAnimPointX).before(tranAnimPointY).before(scaleAnimX).before(scaleAnimY);
		
		scaleAnimX.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float value = (float) animation.getAnimatedValue();
//				mMenuViewSmall.setScaleX(value);
				mContentView.setAlpha((float)(1-((value-1)/toValueX)*0.5));
				mContentView.setScaleX((float)(1-((value-1)/toValueX)*0.2));
				mContentView.setScaleY((float)(1-((value-1)/toValueX)*0.2));
			}
		});
		
		set.addListener(this);
		set.start();
	}

	/**
	 * �����رն���
	 */
	private void closeAnim() {
		ObjectAnimator tranAnimY = ObjectAnimator.ofFloat(mMenuViewSmall, "translationY", -20,0);
		
		ObjectAnimator tranAnimPointX = ObjectAnimator.ofFloat(mMenuViewSmall, "translationX", viewMoveX, 0);
		ObjectAnimator tranAnimPointY = ObjectAnimator.ofFloat(mMenuViewSmall, "translationY", viewMoveY, -20);
		
		ObjectAnimator scaleAnimX = ObjectAnimator.ofFloat(mMenuViewSmall, "scaleX", toValueX+1.0f,1.0f);
		ObjectAnimator scaleAnimY = ObjectAnimator.ofFloat(mMenuViewSmall, "scaleY", toValueY+1.0f,1.0f);
		
		AnimatorSet set = new AnimatorSet();
		set.setDuration(500);
		set.play(tranAnimPointX).with(tranAnimPointY).with(scaleAnimX).with(scaleAnimY).before(tranAnimY);
		
		scaleAnimX.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float value = (float) animation.getAnimatedValue();
				float alphaValue = (float)(0.5/((((value-1)/toValueX)*0.5)+0.5));
				float scaleX = (float)(0.8/((((value-1)/toValueX)*0.2)+0.8));
				float scaleY = (float)(0.8/((((value-1)/toValueX)*0.2)+0.8));
//				mMenuViewSmall.setScaleX(value);
				mContentView.setAlpha(alphaValue<0.5f?0.5f:alphaValue);
				mContentView.setScaleX(scaleX>1?1:scaleX);
				mContentView.setScaleY(scaleY>1?1:scaleY);
				
			}
		});
		
		set.addListener(this);
		set.start();
	}

	/**
	 * ������
	 */
	private void compuleResult() {
		mView.getLocationInWindow(location);
		viewWidth = mView.getWidth();
		viewHeight = mView.getHeight();
		
		int menuWidth = windowWidth - widthOffset*2;
		int menuHeight = windowHeight - getStatusBarHeight() - heightOffset*2;
		
		int viewPonitX = location[0]+viewWidth/2;
		int viewPonitY = location[1]+viewHeight/2;
		int windowPonitX = windowWidth/2;
		int windowPonitY = (windowHeight)/2;
		
		paramsBig = new FrameLayout.LayoutParams(menuWidth,menuHeight);
		paramsBig.setMargins(widthOffset,heightOffset-actionBarHeight, menuWidth, menuHeight);
		
		paramsSmall = new FrameLayout.LayoutParams(viewWidth,viewHeight);
		paramsSmall.setMargins(location[0], location[1]-getStatusBarHeight()-actionBarHeight,
					location[0]+viewWidth, location[1]-getStatusBarHeight()-actionBarHeight+viewHeight);
			
		viewMoveX = windowPonitX - viewPonitX;
		viewMoveY = windowPonitY - viewPonitY;
		
		toValueX = ((((windowWidth-viewWidth)/2)-widthOffset)*1.0f)/(viewWidth/2);
		toValueY = ((((windowHeight-getStatusBarHeight()-viewHeight)/2)-heightOffset)*1.0f)/(viewHeight/2);
		
//		toMenuValueX = (viewWidth*1.0f)/menuWidth;
//		toMenuValueY = (viewHeight*1.0f)/menuHeight;
//		menuToViewX = widthOffset - location[0];
//		menuToViewY = heightOffset - location[1];
	}

	/**
	 * ��ȡ״̬���߶�
	 * @return
	 */
	private int getStatusBarHeight(){
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0,statusBarHeight = 0;
		try{
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = mContext.getResources().getDimensionPixelOffset(x);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return statusBarHeight;
	}

	@Override
	public void onAnimationStart(Animator animation) {
			mRoot.getChildAt(0).setVisibility(View.VISIBLE);
			mRoot.getChildAt(1).setVisibility(View.INVISIBLE);
	}

	@Override
	public void onAnimationEnd(Animator animation) {
		if(AnimState == MENUVIEW_CLOSE){
			mRoot.getChildAt(0).setVisibility(View.INVISIBLE);
			mRoot.getChildAt(1).setVisibility(View.INVISIBLE);
		}else if(AnimState == MENUVIEW_SHOW){
			mRoot.getChildAt(0).setVisibility(View.INVISIBLE);
			mRoot.getChildAt(1).setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onAnimationCancel(Animator animation) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onAnimationRepeat(Animator animation) {
		// TODO Auto-generated method stub
	}

}
