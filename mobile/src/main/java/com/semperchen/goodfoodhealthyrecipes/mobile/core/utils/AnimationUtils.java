package com.semperchen.goodfoodhealthyrecipes.mobile.core.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by 卡你基巴 on 2015/10/1.
 */
public class AnimationUtils {
    public final static int ANIM_ROTATE=0x11;
    public final static int ANIM_TRANSLATE=0x22;
    public final static int ANIM_SCALE=0x33;
    public final static int ANIM_ALPHA=0x44;

    private static boolean mNeekShake;

    public static final float BIG_SHAKE = 3.0f;
    public static final float MIDDLE_SHAKE = 2.0f;
    public static final float SMALL_SHAKE = 1.5f;
    public static final float EXTRA_SMALL_SHAKE = 1.0f;

    public static void RotateAnimation(View view,int startValue,int endValue,int duration,int repeat,Interpolator interpolator){
        RotateAnimation anim = new RotateAnimation(startValue,endValue, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        anim.setDuration(duration);
        anim.setRepeatCount(repeat);
        anim.setInterpolator(interpolator);
        view.startAnimation(anim);
    }

    public static void ObjectAnimation(View view,String propertyName,float fromValue,float toValue,int duration,Animator.AnimatorListener listener){
        ObjectAnimator objAnim = ObjectAnimator.ofFloat(view,propertyName,fromValue,toValue);
        objAnim.setDuration(duration);
        objAnim.setRepeatCount(0);
        objAnim.start();
        objAnim.addListener(listener);
    }

    public static void openAnim(View view, int type, int duration) {
        switch (type) {
            case ANIM_ROTATE:
                Animation anim = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setDuration(duration);
                anim.setFillAfter(true);
                view.startAnimation(anim);
                break;
            case ANIM_TRANSLATE:
                ObjectAnimator animtor = ObjectAnimator.ofFloat(view,"translationY",-view.getHeight(),0);
                animtor.setDuration(duration);
                animtor.start();
                break;
        }
    }

    public static void closeAnim(View view, int type, int duration) {
        switch (type) {
            case ANIM_ROTATE:
                Animation anim = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setDuration(duration);
                anim.setFillAfter(true);
                view.startAnimation(anim);
                break;
            case ANIM_TRANSLATE:
                ObjectAnimator animtor = ObjectAnimator.ofFloat(view, "translationY", 0, -view.getHeight());
                animtor.setDuration(duration);
                animtor.start();
                break;
        }
    }

    public static void setShakeToView(final View view, final int duration,float shakeValue){
        mNeekShake = true;

        final RotateAnimation rma = new RotateAnimation(shakeValue, -shakeValue,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        final RotateAnimation rmb = new RotateAnimation(-shakeValue, shakeValue,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rma.setDuration(80);
        rmb.setDuration(80);

        rma.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                if (mNeekShake) {
                    rma.reset();
                    view.startAnimation(rmb);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
            }
        });
        rmb.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                if (mNeekShake) {
                    rmb.reset();
                    view.startAnimation(rma);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
            }
        });
        view.startAnimation(rma);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try{
                    Thread.sleep((duration/2));
                    mNeekShake = false;
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
