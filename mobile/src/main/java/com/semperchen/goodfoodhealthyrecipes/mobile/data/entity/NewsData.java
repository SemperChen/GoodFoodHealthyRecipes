package com.semperchen.goodfoodhealthyrecipes.mobile.data.entity;

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
public class NewsData {
    @SerializedName("pageNum")
    private int page;
    @SerializedName("results")
    List<News> users=new ArrayList<>();

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<News> getUsers() {
        return users;
    }

    public void setUsers(List<News> users) {
        this.users = users;
    }
}
