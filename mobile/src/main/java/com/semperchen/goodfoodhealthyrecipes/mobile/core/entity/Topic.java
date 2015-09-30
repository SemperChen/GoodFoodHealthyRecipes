package com.semperchen.goodfoodhealthyrecipes.mobile.core.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Semper on 2015/9/27.
 */
public class Topic {
    private int id;
    @SerializedName("image")
    private String image;
    @SerializedName("title")
    private String title;

    public Topic(int id, String image, String title) {
        this.id = id;
        this.image = image;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
