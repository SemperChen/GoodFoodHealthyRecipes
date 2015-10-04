package com.semperchen.goodfoodhealthyrecipes.mobile.core.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Semper on 2015/10/4.
 */
public class RecipeStep {
    @SerializedName("step")
    private String step;
    @SerializedName("image")
    private String image;

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
