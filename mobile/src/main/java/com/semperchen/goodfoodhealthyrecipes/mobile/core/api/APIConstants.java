package com.semperchen.goodfoodhealthyrecipes.mobile.core.api;

import android.os.Environment;

/**
 * Created by Semper on 2015/10/2.
 */
public class APIConstants {
    public static final class Urls{
        public static final String RECIPE_PREVIEW_URL="http://1.oauthdeveloptest.sinaapp.com/index.php";
        public static final String TOPIC_DATA_URL="http://1.oauthdeveloptest.sinaapp.com/test.php";
        public static final String BASE_RECIPE="http://1.oauthdeveloptest.sinaapp.com/recipe.php";
    }

    public static final class Paths {
        public static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        public static final String IMAGE_LOADER_CACHE_PATH = "/GoodFoodHealthyRecipes/Images/";
    }
}
