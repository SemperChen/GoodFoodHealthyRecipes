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
    @SerializedName("currentPage")
    private int currentPage;
    @SerializedName("nextPage")
    private int nextPage;
    @SerializedName("results")
    List<RecipePreview> recipePreviews=new ArrayList<>();

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public List<RecipePreview> getRecipePreviews() {
        return recipePreviews;
    }

    public void setRecipePreviews(List<RecipePreview> recipePreviews) {
        this.recipePreviews = recipePreviews;
    }
}
