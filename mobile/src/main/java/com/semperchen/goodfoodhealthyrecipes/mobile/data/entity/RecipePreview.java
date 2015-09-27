package com.semperchen.goodfoodhealthyrecipes.mobile.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Semper on 2015/9/26.
 */
public class RecipePreview {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("author")
    private String author;
    @SerializedName("authorIcon")
    private String authorIcon;
    @SerializedName("image")
    private String image;
    @SerializedName("recipeId")
    private int recipeId;

    public RecipePreview(){}

    public RecipePreview(int id, String title, String author, String authorIcon, String image, int recipeId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.authorIcon = authorIcon;
        this.image = image;
        this.recipeId = recipeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorIcon() {
        return authorIcon;
    }

    public void setAuthorIcon(String authorIcon) {
        this.authorIcon = authorIcon;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}
