package com.semperchen.goodfoodhealthyrecipes.mobile.core.utils;

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

    public static void RotateAnimation(View view,int startValue,int endValue,int duration,int repeat,Interpolator interpolator){
        RotateAnimation anim = new RotateAnimation(startValue,endValue, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        anim.setDuration(duration);
        anim.setRepeatCount(repeat);
        anim.setInterpolator(interpolator);
        view.startAnimation(anim);
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
}
