package com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget;

import android.content.Context;
import android.graphics.*;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by 卡你基巴 on 2015/10/29.
 */
public class CustomView extends FloatingActionButton{
    private Context mContext;
    private Paint mPaint;
    private boolean isShow;
    private int endColor;
    private int startColor;

    private int mWidth,mHeight;
    private int mMarginW,mMarginH;
    private int mPointX,mPointY;
    private int Xa,Ya;
    private int Xb,Yb;
    private int Xc,Yc;
    private int Xd,Yd;
    private int Xe,Ye;

    private Path mPath;

    public CustomView(Context context) {
        this(context,null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        init();
        initPaint();
    }

    private void init() {
        isShow = false;
        endColor = (new Color()).argb(255, 255, 152, 0);
        startColor = (new Color()).argb(184, 195, 195, 195);
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(startColor);

        setOnClickListener(new MyListener());
    }

    public void isShow(boolean value){
        if(value){
            mPaint.setColor(endColor);
        }else{
            mPaint.setColor(startColor);
        }
        this.isShow = value;
        invalidate();
    }

    public boolean getShow(){
        return isShow;
    }

    public void setColors(int startColor,int endColor){
        this.startColor = startColor;
        this.endColor = endColor;
        mPaint.setColor(startColor);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // TODO Auto-generated method stub
        super.onLayout(changed, left, top, right, bottom);
        initLayout(left,top,right,bottom);
        initPath();
    }

    private void initLayout(int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        mMarginW = layoutParams.leftMargin + layoutParams.rightMargin;
        mMarginH = layoutParams.topMargin + layoutParams.bottomMargin;

        mWidth = right - left - mMarginW;
        mHeight = bottom - top - mMarginH;
        mPointX = mWidth/2 + layoutParams.leftMargin;
        mPointY = mHeight/2 + layoutParams.topMargin;

        Xa = mWidth/2 + layoutParams.leftMargin;
        Ya = layoutParams.topMargin;
        Xb = mWidth/8 + layoutParams.leftMargin;
        Yb = (7*mHeight)/8 + layoutParams.topMargin;
        Xc = mWidth + layoutParams.leftMargin;
        Yc = (3*mHeight)/8 + layoutParams.topMargin;
        Xd = layoutParams.leftMargin;
        Yd = (3*mHeight)/8 + layoutParams.topMargin;
        Xe = (7*mWidth)/8 + layoutParams.leftMargin;
        Ye = (7*mHeight)/8 + layoutParams.topMargin;
    }

    private void initPath() {
        mPath = new Path();
        mPath.moveTo(Xa, Ya);
        mPath.lineTo(Xb, Yb);
        mPath.lineTo(Xc, Yc);
        mPath.lineTo(Xd, Yd);
        mPath.lineTo(Xe, Ye);
        mPath.close();
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
//		TODO Auto-generated method stub
        if(l instanceof MyListener){
            super.setOnClickListener(l);
        }else{
            super.setOnClickListener(new MyListener());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }


    public class MyListener implements View.OnClickListener{
        public void onClick(View v) {
            if(isShow){
                new Thread(new NoShowRun()).start();
            }else{
                new Thread(new ShowRun()).start();
            }
            isShow = !isShow;
        }
    }

    public class ShowRun implements Runnable{

        @Override
        public void run() {
            int start = 1;
            while(start < 100){
                try{
                    mPaint.setShader(new RadialGradient(mPointX, mPointY, start,
                            endColor, startColor, Shader.TileMode.CLAMP));
                    start += 1;
                    setClickable(false);
                    postInvalidate();
                    Thread.sleep(10);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            mPaint.setShader(null);
            mPaint.setColor(endColor);
            setClickable(true);
        }

    }

    public class NoShowRun implements Runnable{

        @Override
        public void run() {
            int start = 100;
            while(start > 0){
                try{
                    mPaint.setShader(new RadialGradient(mPointX, mPointY, start,
                            endColor, startColor, Shader.TileMode.CLAMP));
                    start -= 1;
                    setClickable(false);
                    postInvalidate();
                    Thread.sleep(10);
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
            mPaint.setShader(null);
            mPaint.setColor(startColor);
            setClickable(true);
        }

    }
}
