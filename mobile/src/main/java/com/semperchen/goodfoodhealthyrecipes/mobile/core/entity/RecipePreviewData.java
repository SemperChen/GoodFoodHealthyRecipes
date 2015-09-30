package com.semperchen.goodfoodhealthyrecipes.mobile.core.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 * <p/>
 * <p>Project HealthExpress.</p>
 * <p>Date 2015/8/29.</p>
 *
 * @author SemperChen
 * @version 1.0-SNAPSHOT
 */
public class RecipePreviewData {
    @SerializedName("pageNum")
    private int page;
    @SerializedName("results")
    List<RecipePreview> recipePreviews=new ArrayList<>();

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<RecipePreview> getRecipePreviews() {
        return recipePreviews;
    }

    public void setRecipePreviews(List<RecipePreview> recipePreviews) {
        this.recipePreviews = recipePreviews;
    }
}
