package com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.utils.DensityUtils;

public class ProgressView extends View implements Runnable{
	private Context mContext;
	private Random random;
	private int[] colors = new int[]{
			(new Color()).argb(255, 255, 00, 00), //红
			(new Color()).argb(255, 255, 128, 00),//橙
			(new Color()).argb(255, 255, 255, 00),//黄
			(new Color()).argb(255, 00, 255, 00), //绿
			(new Color()).argb(255, 00, 255, 128),//青
			(new Color()).argb(255, 00, 00, 255), //蓝
			(new Color()).argb(255, 128, 00, 255) //紫
	};
	
	private boolean isRun;
	
	private int mColor;
	private int mAlpha;
	private int STROKE_WIDTH;
	
	private Paint mPaint;
	private int mRadius;
	private int SCREEN_WIDTH,SCREEN_HEIGHT;
	
	private Paint mTextPaint;
	private String text = "00%";
	private Rect mRect;
	private float textWidth,textHeight;
	
	public ProgressView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	
	public ProgressView(Context context, AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
		initPaint();
		initTextPaint();
	}

	private void init() {
		isRun = true;
	}

	@Override
	public void setVisibility(int visibility) {
		if(visibility == View.VISIBLE){
			isRun = true;
			new Thread(this).start();
		}else{
			isRun = false;
		}
		super.setVisibility(visibility);
	}

	private void initPaint() {
		mColor = colors[0];
		mAlpha = 255;
		STROKE_WIDTH = 10;
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.STROKE);
		
		random = new Random();
		mRadius = 20;

		SCREEN_WIDTH = DensityUtils.getDisplayWidth(mContext);
		SCREEN_HEIGHT = DensityUtils.getDisplayHeight(mContext);
	}
	
	private void initTextPaint() {
		mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		int color = (new Color()).argb(255, 255, 255, 255);
		mTextPaint.setColor(color);
		mTextPaint.setTextSize(39);
		mTextPaint.setStyle(Paint.Style.FILL);
		
		mRect = new Rect();
		mTextPaint.getTextBounds(text, 0, text.length(), mRect);
		textWidth = mRect.width();
		textHeight = mRect.height();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		mPaint.setColor(mColor);
		mPaint.setStrokeWidth(STROKE_WIDTH);
		mPaint.setAlpha(mAlpha);
		canvas.drawCircle(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, mRadius, mPaint);
		
		canvas.drawText(text, SCREEN_WIDTH/2 - textWidth/2, SCREEN_HEIGHT/2 + textHeight/2, mTextPaint);
	}
	
	public void setProgress(int value){
		if(value <= 0){
			text = "00%";
//			startCricle();
		}else if(value < 10){
			text = "0"+value+"%";
		}else if(value >= 100){
			text = "100%";
//			clearCricle();
		}else{
			text = value+"%";
		}
//		postInvalidate();
	}

	private void startCricle(){
		STROKE_WIDTH = 10;
		mAlpha = 255;
		postInvalidate();
	}

	private void clearCricle() {
		STROKE_WIDTH = 1;
		mAlpha = 0;
		postInvalidate();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isRun){
			try{
				if(mRadius >= SCREEN_HEIGHT/2){
					mRadius = 20;
				}else{
					mRadius += (random.nextInt(15)+1);
					
					if(mRadius<=(SCREEN_HEIGHT/14)){
						mColor = colors[0];
						STROKE_WIDTH = 10;
						mAlpha = 255;
					}else if(mRadius>(SCREEN_HEIGHT/14) && mRadius<=(SCREEN_HEIGHT/7)){
						mColor = colors[1];
						STROKE_WIDTH = 23;
						mAlpha = (int) (((6/7)+0.1)*255);
					}else if(mRadius>(SCREEN_HEIGHT/7) && mRadius<=((3*SCREEN_HEIGHT)/14)){
						mColor = colors[2];
						STROKE_WIDTH = 36;
						mAlpha = (int) (((5/7)+0.1)*255);
					}else if(mRadius>((3*SCREEN_HEIGHT)/14) && mRadius<=((2*SCREEN_HEIGHT)/7)){
						mColor = colors[3];
						STROKE_WIDTH = 49;
						mAlpha = (int) (((4/7)+0.1)*255);
					}else if(mRadius>((2*SCREEN_HEIGHT)/7) && mRadius<=((5*SCREEN_HEIGHT)/14)){
						mColor = colors[4];
						STROKE_WIDTH = 62;
						mAlpha = (int) (((3/7)+0.1)*255);
					}else if(mRadius>((5*SCREEN_HEIGHT)/14) && mRadius<=((3*SCREEN_HEIGHT)/7)){
						mColor = colors[5];
						STROKE_WIDTH = 75;
						mAlpha = (int) (((2/7)+0.1)*255);
					}else if(mRadius>((3*SCREEN_HEIGHT)/7) && mRadius<(SCREEN_HEIGHT/2)){
						mColor = colors[6];
						STROKE_WIDTH = 88;
						mAlpha = (int) (((1/7)+0.1)*255);
					}
					
					postInvalidate();
				}
				
				Thread.sleep(80);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
