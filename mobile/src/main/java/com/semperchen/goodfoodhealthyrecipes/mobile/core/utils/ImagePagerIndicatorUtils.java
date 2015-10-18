package com.semperchen.goodfoodhealthyrecipes.mobile.core.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 卡你基巴 on 2015/10/8.
 *
 * 图片头页面ViewPager与Indicator切换的辅助类
 */
public class ImagePagerIndicatorUtils {
    private final static Map<Integer, View> views = new HashMap<Integer,View>();

    public static void setOnPagerChangListener(ViewPager pager,RelativeLayout indicator){
        View viewOne = indicator.findViewById(R.id.point_one);
        views.put(0, viewOne);
        View viewTwo = indicator.findViewById(R.id.point_two);
        views.put(1, viewTwo);
        View viewThree = indicator.findViewById(R.id.point_three);
        views.put(2, viewThree);
        View viewFour = indicator.findViewById(R.id.point_four);
        views.put(3, viewFour);
        View viewFive = indicator.findViewById(R.id.point_five);
        views.put(4, viewFive);
        View viewSix = indicator.findViewById(R.id.point_six);
        views.put(5, viewSix);
        View viewSeven = indicator.findViewById(R.id.point_seven);
        views.put(6, viewSeven);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i=0;i<views.size();i++){
                    if(i == position){
                        views.get(i).setVisibility(View.VISIBLE);
                    }else{
                        views.get(i).setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
