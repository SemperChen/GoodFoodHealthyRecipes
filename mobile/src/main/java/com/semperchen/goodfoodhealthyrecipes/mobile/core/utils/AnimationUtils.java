package com.semperchen.goodfoodhealthyrecipes.mobile.core.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by abc on 2015/10/1.
 */
public class AnimationUtils {

    public static void RotateAnimation(View view,int startValue,int endValue,int duration,int repeat,Interpolator interpolator){
        RotateAnimation anim = new RotateAnimation(startValue,endValue, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        anim.setDuration(duration);
        anim.setRepeatCount(repeat);
        anim.setInterpolator(interpolator);
        view.startAnimation(anim);
    }
}
