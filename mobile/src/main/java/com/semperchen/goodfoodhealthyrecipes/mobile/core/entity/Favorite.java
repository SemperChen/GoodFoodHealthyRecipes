package com.semperchen.goodfoodhealthyrecipes.mobile.core.entity;

/**
 * Created by Semper on 2015/9/26.
 */
public class Favorite {
    private int id;
    private int recipePreviewId;
    private int recipeId;

    public Favorite(int id, int recipePreviewId, int recipeId) {
        this.id = id;
        this.recipePreviewId = recipePreviewId;
        this.recipeId = recipeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipePreviewId() {
        return recipePreviewId;
    }

    public void setRecipePreviewId(int recipePreviewId) {
        this.recipePreviewId = recipePreviewId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}
