package com.semperchen.goodfoodhealthyrecipes.mobile.core.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Semper on 2015/10/4.
 */
public class Recipe {
    @SerializedName("headerImage")
    private String headerImage;
    @SerializedName("title")
    private String title;
    @SerializedName("authorIcon")
    private String authorIcon;
    @SerializedName("author")
    private String author;
    @SerializedName("time")
    private String time;
    @SerializedName("intro")
    private String intro;
    @SerializedName("ingredients")
    private List<String> ingredients;
    @SerializedName("steps")
    private List<RecipeStep> steps;
    @SerializedName("tips")
    private List<String> tip;

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorIcon() {
        return authorIcon;
    }

    public void setAuthorIcon(String authorIcon) {
        this.authorIcon = authorIcon;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<RecipeStep> getSteps() {
        return steps;
    }

    public void setSteps(List<RecipeStep> steps) {
        this.steps = steps;
    }

    public List<String> getTip() {
        return tip;
    }

    public void setTip(List<String> tip) {
        this.tip = tip;
    }
}
