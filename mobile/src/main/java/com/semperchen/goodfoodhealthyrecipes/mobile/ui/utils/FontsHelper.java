
package com.semperchen.goodfoodhealthyrecipes.mobile.ui.utils;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressWarnings(value = "unused")
public class FontsHelper {
    private static final String TAG = FontsHelper.class.getSimpleName();
    private static final boolean DEBUG = true;
    public static Typeface FONT_SHOUJINSHU;
    public static Typeface FONT_XIAOZHUAN;
    public static Typeface FONT_LISU;

    private FontsHelper() {
    }

    /**
     * 初始化字体工具，该方法应该在程序首个 Activity 的 onCreate 方法中调用，以保证到后续动态修改布局时能够使用该方法。
     *
     * @param activity 活动
     */
    public static void initialize(Activity activity) {
        AssetManager assetManager = activity.getAssets();
        FONT_SHOUJINSHU = Typeface.createFromAsset(assetManager, "fonts/ShouJinShu.ttf");
        FONT_LISU = Typeface.createFromAsset(assetManager, "fonts/LiSu.ttf");
    }


    /**
     * 应用字体到布局
     *
     * @param root 根布局
     * @param font 字体
     */
    public static void applyFont(final View root, final Typeface font) {
        try {
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                for (int i = 0; i < viewGroup.getChildCount(); i++)
                    applyFont(viewGroup.getChildAt(i), font);
            } else if (root instanceof TextView)
                ((TextView) root).setTypeface(font);
        } catch (Exception e) {
            Log.e(TAG, String.format("Error occurred when trying to apply font for %s mView", root));
            e.printStackTrace();
        }
    }

    /**
     * 应用字体到视图
     *
     * @param view 视图
     * @param font 字体
     */
    public static void setFont(final View view, final Typeface font) {
        try {
            ((TextView) view).setTypeface(font);
        } catch (Exception e) {
            Log.e(TAG, String.format("Error occurred when trying to apply font for %s mView", view));
            e.printStackTrace();
        }
    }
}
