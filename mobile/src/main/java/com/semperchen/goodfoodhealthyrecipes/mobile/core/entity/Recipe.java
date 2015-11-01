package com.semperchen.goodfoodhealthyrecipes.mobile.core.entity;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Semper on 2015/10/4.
 */
@DatabaseTable(tableName = "table_recipe")
public class Recipe {
    @DatabaseField(generatedId = true)
    @SerializedName("id")
    private int id;
    @DatabaseField(columnName = "recipeId")
    @SerializedName("recipeId")
    private int recipeId;
    @DatabaseField(columnName = "headerImage")
    @SerializedName("headerImage")
    private String headerImage;
    @DatabaseField(columnName = "title")
    @SerializedName("title")
    private String title;
    @DatabaseField(columnName = "authorIcon")
    @SerializedName("authorIcon")
    private String authorIcon;
    @DatabaseField(columnName = "author")
    @SerializedName("author")
    private String author;
    @DatabaseField(columnName = "time")
    @SerializedName("time")
    private String time;
    @DatabaseField(columnName = "intro")
    @SerializedName("intro")
    private String intro;
    @DatabaseField(columnName = "ingredients",dataType = DataType.SERIALIZABLE)
    @SerializedName("ingredients")
    private ArrayList<String> ingredients;
    @SerializedName("steps")
    private List<RecipeStep> steps;
    @DatabaseField(columnName = "tip",dataType = DataType.SERIALIZABLE)
    @SerializedName("tips")
    private ArrayList<String> tip;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getRecipeId(){
        return recipeId;
    }

    public void setRecipeId(int recipeId){
        this.recipeId = recipeId;
    }

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

    public void setIngredients(ArrayList<String> ingredients) {
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

    public void setTip(ArrayList<String> tip) {
        this.tip = tip;
    }
}
