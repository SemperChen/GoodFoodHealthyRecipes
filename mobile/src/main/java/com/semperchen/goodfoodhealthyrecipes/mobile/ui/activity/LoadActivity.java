package com.semperchen.goodfoodhealthyrecipes.mobile.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.common.OnCommonAnimationListener;

/**
 * <p></p>
 * <p/>
 * <p>Project: GoodFoodHealthyRecipes.</p>
 * <p>Date: 2015/10/5.</p>
 * <p>Description:
 *
 * @author SemperChen
 * @version 1.0-SNAPSHOT
 */
public class LoadActivity extends Activity {

    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        image= (ImageView) findViewById(R.id.load_image);

        final Animation animation= AnimationUtils.loadAnimation(this,R.anim.splash);
        animation.setAnimationListener(new OnCommonAnimationListener(){
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startMainActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        image.startAnimation(animation);
    }

    /**
     * 跳转到MainActivity
     */
    private void startMainActivity(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
//
}
