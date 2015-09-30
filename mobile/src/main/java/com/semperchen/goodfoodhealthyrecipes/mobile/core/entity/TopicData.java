package com.semperchen.goodfoodhealthyrecipes.mobile.core.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Semper on 2015/9/27.
 */
public class TopicData {
    private int id;
    @SerializedName("results")
    private List<Topic> topics=new ArrayList<>();

    public TopicData(int id, List<Topic> topics) {
        this.id = id;
        this.topics = topics;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }
}
