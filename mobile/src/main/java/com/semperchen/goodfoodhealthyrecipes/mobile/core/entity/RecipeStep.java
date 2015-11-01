package com.semperchen.goodfoodhealthyrecipes.mobile.core.entity;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Semper on 2015/10/4.
 */
@DatabaseTable(tableName = "table_recipestep")
public class RecipeStep {
    @DatabaseField(generatedId = true)
    @SerializedName("id")
    private int id;
    @DatabaseField(columnName = "recipeId")
    @SerializedName("recipeId")
    private int recipeId;
    @DatabaseField(columnName = "step")
    @SerializedName("step")
    private String step;
    @DatabaseField(columnName = "image")
    @SerializedName("image")
    private String image;

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
    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
