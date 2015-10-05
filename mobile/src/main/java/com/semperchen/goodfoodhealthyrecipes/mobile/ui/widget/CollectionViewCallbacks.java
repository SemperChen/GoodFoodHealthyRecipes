package com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by abc on 2015/9/21.
 */
public interface CollectionViewCallbacks {
    /**
     * 新建头视图并返回
     * @param context
     * @param parent
     * @return
     */
    View newCollectionHeaderView(Context context, ViewGroup parent);

    /**
     * 绑定头视图的数据
     * @param context
     * @param view
     * @param groupId
     * @param headerLabel
     */
    void bindCollectionHeaderView(Context context, View view, int groupId, String headerLabel);

    /**
     * 新建Item的视图并返回
     * @param context
     * @param groupId
     * @param parent
     * @param indexInGroup
     * @return
     */
    View newCollectionItemView(Context context, int groupId, ViewGroup parent, int indexInGroup);

    /**
     * 绑定Item的数据
     * @param context
     * @param view
     * @param groupId
     * @param indexInGroup
     * @param dataIndex
     * @param tag
     */
    void bindCollectionItemView(Context context, View view, int groupId, int indexInGroup, int dataIndex, Object tag);

}
