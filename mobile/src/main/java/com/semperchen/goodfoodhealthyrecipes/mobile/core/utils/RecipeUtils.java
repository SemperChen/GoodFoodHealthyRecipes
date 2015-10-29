package com.semperchen.goodfoodhealthyrecipes.mobile.core.utils;

import android.content.Context;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.repository.PreferenceManager;

/**
 * Created by 卡你基巴 on 2015/10/29.
 */
public class RecipeUtils {
    private static final String key = "RECIPEID";

    public static void putRecipeIdInPreference(String recipeId){
        String recipeIds = getRecipeIdInPreference();
        if("".equals(recipeIds)){
            PreferenceManager.getInstance().putString(key,recipeId);
        }else{
            recipeIds = recipeIds+","+recipeId;
            PreferenceManager.getInstance().putString(key,recipeIds);
        }

    }

    public static String getRecipeIdInPreference(){
        return PreferenceManager.getInstance().getString(key);
    }

    public static String[] getRecipeIdsInPreference(){
        String recipeId = PreferenceManager.getInstance().getString(key);
        String[] recipeIds = recipeId.split(",");
        return recipeIds;
    }

    public static void clearRecipeIdInPreference(String recipeId){
        String[] recipeIds = getRecipeIdsInPreference();
        String recipeIdPut  = "";
        for(int i=0;i<recipeIds.length;i++){
            if(recipeIds[i].equals(recipeId)){
                continue;
            }
            recipeIdPut += recipeIds[i]+",";
        }
        if(recipeIdPut.endsWith(",")){
            recipeIdPut = recipeIdPut.substring(0,recipeIdPut.length()-1);
        }
        PreferenceManager.getInstance().putString(key,recipeIdPut);
    }

    public static boolean isItemShow(String recipeId){
        String[] recipeIds = getRecipeIdsInPreference();
        for(int i=0;i<recipeIds.length;i++){
            if(recipeIds[i].equals(String.valueOf(recipeId))){
                return true;
            }
        }
        return false;
    }
}
