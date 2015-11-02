package com.semperchen.goodfoodhealthyrecipes.mobile.core.entity;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Semper on 2015/9/26.
 */
@DatabaseTable(tableName = "table_reciperpreview")
public class RecipePreview implements Serializable{
    @DatabaseField(generatedId = true)
    @SerializedName("id")
    private int id;
    @DatabaseField(columnName = "title")
    @SerializedName("title")
    private String title;
    @DatabaseField(columnName = "author")
    @SerializedName("author")
    private String author;
    @DatabaseField(columnName = "authorIcon")
    @SerializedName("authorIcon")
    private String authorIcon;
    @DatabaseField(columnName = "image")
    @SerializedName("image")
    private String image;
    @DatabaseField(columnName = "recipeId")
    @SerializedName("recipeId")
    private int recipeId;
    @DatabaseField(columnName = "isRecommendation")
    @SerializedName("isRecommendation")
    private boolean isRecommendation;

    public RecipePreview(){}

    public RecipePreview(int id, String title, String author, String authorIcon, String image, int recipeId, boolean isRecommendation) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.authorIcon = authorIcon;
        this.image = image;
        this.recipeId = recipeId;
        this.isRecommendation = isRecommendation;
    }

    @Override
    public String toString() {
        return "[id:"+id+",title:"+title+",author:"+author+",authorIcon:"+authorIcon+",image:"+image+",recipeId:"+recipeId+",isRecommendation:"+isRecommendation+"]";
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

    public boolean isRecommendation() {
        return isRecommendation;
    }

    public void setIsRecommendation(boolean isRecommendation) {
        this.isRecommendation = isRecommendation;
    }
}
