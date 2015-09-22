package com.semperchen.goodfoodhealthyrecipes.mobile.data.entity;

/**
 * Created by Semper on 2015/9/21.
 */
public class Daily {
    private int id;
    private String title;
    private String category;
    private int image;
    private boolean isFavorite;

    public Daily(){}

    public Daily(int id, String title, String category, int image, boolean isFavorite) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.image = image;
        this.isFavorite = isFavorite;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}
